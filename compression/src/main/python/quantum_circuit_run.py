import numpy as np
import math
import sys
from itertools import combinations as cm
from qiskit import *
from qiskit.opflow import CircuitStateFn, StateFn, I, Z 
from qiskit.opflow.gradients import Gradient
from numpy import log as ln

def X_generator(primes, identity):
    #Generate X value for hamiltonian
    constant = 0

    for i in range(len(primes)):
        #Build Constant value iteratively
        constant += ln(primes[i])
        
        if i == 0:
            z_value = Z
        else:
            z_value = I
        #Build rest of X
        for j in range(1, len(primes)):
            if j == i:
                z_value = z_value ^ Z
            else:
                z_value = z_value ^ I
        if i == 0:
            x = -ln(primes[i]) * z_value
        else:
            x -= ln(primes[i]) * z_value
    #Combine Both to form X
    x += constant * identity
    
    return x

def hamiltonian(n, primes):
    #Generate Hamitonian according to number of primes
    identity = I
    
    for i in range(len(primes) - 1):
        identity = identity ^ I
        
    x = X_generator(primes, identity)
    
    lnn = float(ln(n))
    
    h = ((lnn**2) * identity) - (lnn * x) + (1 / 4 * (x ** 2))
    
    return h


def gradient_function(n, primes, combinations, layers, current_point, gradient_object):

    cir, free_params = generate_circuit(n, primes, combinations, layers, current_point, assigned=False)
    cir.remove_final_measurements()
    c = hamiltonian(n, primes)
    op = ~StateFn(c) @ CircuitStateFn(primitive=cir, coeff=1.)
    grad_object = gradient_object.convert(operator=op, params=free_params)
    value_dict = {free_params[i]: current_point[i] for i in range(len(free_params))}
    x = grad_object.assign_parameters(value_dict).eval()

    return np.real(x)
        

""" This Section onwards is the main part to run the circuit and get the counts of the system, the section above with the functions are used to get the gradients of the circuit. Need to know how the python script call works with 
maven projects....."""

def generate_circuit(n, circuit_primes, combinations, no_layers, hyperparams, assigned=True):
    # Generate some circuit coefficients
    root_arg = 1
    for p in circuit_primes:
        root_arg *= p

   
    thetas = [qiskit.circuit.Parameter(str(i)) for i in range(no_layers * ((2 * len(circuit_primes)) + len(combinations)))]
    thetas_rshp = np.reshape(np.asarray(thetas),(no_layers, ((2 * len(circuit_primes)) + len(combinations))))

    assert len(hyperparams) == len(thetas)

    # Generate circuit
    Qcir = QuantumCircuit(len(circuit_primes))
    Qcir.h(range(len(circuit_primes)))

    for l in range(no_layers):
        # Set parameters
        lngth = len(thetas_rshp[l])
        gamma1 = thetas_rshp[l][0: len(circuit_primes)]
        gamma2 = thetas_rshp[l][len(circuit_primes): (len(circuit_primes) + len(combinations))]
        beta   = thetas_rshp[l][(len(circuit_primes) + len(combinations)) : lngth]

        for i in range(len(circuit_primes)):
            Qcir.rz(gamma1[i] * 2 * ln(n / math.sqrt(root_arg)) * ln(circuit_primes[i]), i)

        i = 0
        for j,k in combinations:
            Qcir.rzz(gamma2[i] * ln(circuit_primes[j]) * ln(circuit_primes[k]), j, k)
            i+=1

        for m in range(len(circuit_primes)):
            Qcir.rx(beta[m], m)

        if assigned == True:
            p_dict = {thetas[i] : hyperparams[i] for i in range(len(thetas))}
            Qcir.assign_parameters(p_dict, inplace=True)

    Qcir.measure_all()
    
    return Qcir, thetas

def main(arg):
    # Default list of primes (MAX_No Of Qubits = 10)
    primes = [2,3,5,7,11,17,19,23,29,31]
    #grad_obj = Gradient(grad_method="param_shift")
  
    # Get the number of n, qubits, layers and all the hyperparams required
    circuit_params = list(map(float, arg.split()))
    n = int(circuit_params[0])
    no_qubits = int(circuit_params[1])
    no_layers = int(circuit_params[2])
    hyperparams = circuit_params[3: len(circuit_params)]
    # Define the number of primes being used and the combination of qubit ZZ gates
    circuit_primes =  primes[0: no_qubits]
    combinations = list(cm(range(len(circuit_primes)), 2))
    Qcir, free_params = generate_circuit(n, circuit_primes, combinations, no_layers, hyperparams)

    backend = Aer.get_backend('aer_simulator')
    # backend.set_options(device='GPU') DO not use for now until dependencies have been solved
    job_sim = execute(Qcir, backend = backend, shots = 2048).result()
    counts = job_sim.get_counts(Qcir)

    print(counts)
    return counts
    

if __name__ == "__main__":
    input =  ' '.join(map(str, sys.argv[1:]))
    main(input)