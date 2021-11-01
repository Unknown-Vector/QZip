import numpy as np
import math
import sys
from itertools import combinations as cm
from qiskit import *
from qiskit.opflow import CircuitStateFn, StateFn, I, Z 
from qiskit.opflow.gradients import Gradient
from numpy import log as ln
from qiskit.circuit import qpy_serialization

def main(arg):
    hyperparams = list(map(float, arg.split(" ")))

    with open('./src/main/python/Qcir_current.qpy', 'rb') as fd:
        cir = qpy_serialization.load(fd)[0]

    free_params = list(cir.parameters)   
    p_dict = {free_params[i] : hyperparams[i] for i in range(len(free_params))}
    cir.assign_parameters(p_dict, inplace=True)
    cir.measure_all()
    backend = Aer.get_backend('aer_simulator')
    job_sim = execute(cir, backend = backend, shots = 2048).result()
    counts = job_sim.get_counts(cir)

    print(counts)
 
    

if __name__ == "__main__":
    main(sys.argv[1].strip())