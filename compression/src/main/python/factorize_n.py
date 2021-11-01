import sys
import random
import numpy as np
from qiskit import *
from qiskit.circuit import qpy_serialization
from qiskit.algorithms.optimizers import COBYLA
from itertools import combinations as cm

primes = [2,3,5,7,11,13,17,19,23,29,31]

def obj_function(counts, n, penalty):
    max_c = 0
    max_bit = ""
   
    for c in counts:
        if counts[c] >= max_c:
            max_c =  counts[c]
            max_bit = c
    comp_primes = 1
    build_primes = primes[::-1]
    for i in range(len(max_bit)):
        comp_primes *= build_primes[i]**int(max_bit[i])
    p = 0
    if penalty == 1:
        if abs(comp_primes-n) > n:
            p += comp_primes
#     print("N = ", n,  " Expectation = ", abs((n - comp_primes) + p))
    return abs((n - comp_primes) + p)

def obj_function2(counts, n, penalty):
    max_c = 0
    max_bit = ""
    expectation = 0
    build_primes = primes[::-1]
    for c in counts:      
        comp_primes = 1       
        for i in range(len(c)):
            comp_primes *= build_primes[i]**int(c[i])
        p = 0
        if penalty == 1:
            if abs(comp_primes-n) > n:
                p += comp_primes    
        expectation += abs(n - comp_primes + p) * (counts[c]/1024)
    p = 0
    
    return expectation

def gen_f(n):
    
    def f(theta):
        with open('./src/main/python/Qcir_current.qpy', 'rb') as fd:
            qc = qpy_serialization.load(fd)[0]
        free_params = list(qc.parameters)   
        p_dict = {free_params[i] : theta[i] for i in range(len(free_params))}
        qc.assign_parameters(p_dict, inplace=True)
        qc.measure_all()
        back = Aer.get_backend('aer_simulator')
        job_sim = execute(qc, backend = back, shots = 1024).result()
        counts = job_sim.get_counts(qc)
        
        return obj_function2(counts, n, 1)

    return f

# def _convert_to_gradient_function2(gradient_object, layers, n):

#         def gradient_function2(current_point):
#             combinations = list(cm(range(len(primes)), 2))
#             circuit, free_params, param_coeffs = generate_circuit(n, primes, combinations, layers, current_point, assigned=False)
#             circuit.remove_final_measurements()
#             c = hamiltonian(n, primes).to_matrix()
            
#             statevec = []
#             for shift in [np.pi/2, -np.pi/2]:
#                 temp = []
#                 for j in range(len(free_params)):
#                     cir = deepcopy(circuit)                   
#                     hyparams = deepcopy(current_point) 
#                     free_params = list(cir.parameters)
                    
#                     hyparams[j] += (shift / param_coeffs[j])

#                     p_dict = {free_params[i] : hyparams[i] for i in range(len(free_params))}
#                     cir.assign_parameters(p_dict, inplace=True)
                 
#                     sim = Aer.get_backend('aer_simulator')
#                     cir.save_statevector()
#                     qobj = assemble(cir)     # Create a Qobj from the circuit for the simulator to run
#                     result = sim.run(qobj).result()
#                     sv = np.round((result.get_statevector(cir)).reshape(2 ** len(primes),1), 15)
#                     temp.append(sv)
#                 statevec.append(temp)
#             g2 = []
            
#             for i in range(len(free_params)):
#                 p_shift = np.dot(np.asmatrix(statevec[0][i]).H, np.dot(c, statevec[0][i]))
#                 n_shift = np.dot(np.asmatrix(statevec[1][i]).H, np.dot(c, statevec[1][i]))
#                 dx = param_coeffs[i] * 0.5 * (p_shift - n_shift)
                
#                 g2.append(np.real(dx.item()))
#             return np.asarray(g2)
        
#         return gradient_function2

def run(optimizer, func):
    # g_f = _convert_to_gradient_function2(gradient_function, layers, n)
    with open('./src/main/python/Qcir_current.qpy', 'rb') as fd:
            qc = qpy_serialization.load(fd)[0]
    free_params = list(qc.parameters) 

    global primes
    no_primes = qc.num_qubits
    primes = primes[0: no_primes]
    combinations = list(cm(range(len(primes)), 2))
    layers = int(len(free_params) / ((2 * len(primes)) + len(combinations)))

    theta = [random.uniform(1.0E-10, 2 * np.pi) for _ in range(layers * ((2 * len(primes)) + len(combinations)))]
    theta, loss, _ = optimizer.optimize(
                num_vars=len(theta),
                objective_function=func,
                initial_point=theta,
                gradient_function=None
                )

    p_dict = {free_params[i] : theta[i] for i in range(len(free_params))}
    qc.assign_parameters(p_dict, inplace=True)
    qc.measure_all()
    back = Aer.get_backend('aer_simulator')
    job_sim = execute(qc, backend = back, shots = 1024).result()
    counts = job_sim.get_counts(qc)
    # loss = obj_function(counts, n, 0)
    return counts

def main(arg): 
    n = int(arg)
    optimizer = COBYLA(maxiter=1050)
    obj_fun = gen_f(n)

    counts = run(optimizer, obj_fun)

    print(counts)


if __name__ == "__main__":
    main(sys.argv[1].strip())
