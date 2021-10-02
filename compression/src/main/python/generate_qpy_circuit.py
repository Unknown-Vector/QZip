import numpy as np
import math
import sys
from itertools import combinations as cm
from qiskit import *
from qiskit.opflow import CircuitStateFn, StateFn, I, Z 
from qiskit.opflow.gradients import Gradient
from numpy import log as ln
from qiskit.circuit import qpy_serialization

def main(n, no_primes , no_layers):
    circuit_primes = [2,3,5,7,11,17,19,23,29,31]
    circuit_primes = circuit_primes[0: no_primes]
    combinations = combinations = list(cm(range(len(circuit_primes)), 2))
    # Generate some circuit coefficients
    root_arg = 1
    for p in circuit_primes:
        root_arg *= p

    thetas = [qiskit.circuit.Parameter(str(i)) for i in range(no_layers * ((2 * len(circuit_primes)) + len(combinations)))]
    thetas_rshp = np.reshape(np.asarray(thetas),(no_layers, ((2 * len(circuit_primes)) + len(combinations))))
   
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
    
    with open('./compression/src/main/python/Qcir_current.qpy', 'wb') as fd:
        qpy_serialization.dump(Qcir, fd)

    print("Generated Circuit File......")

if __name__ == "__main__":
    input = sys.argv[1].split(" ")
    main(int(input[0]), int(input[1]), int(input[2]))