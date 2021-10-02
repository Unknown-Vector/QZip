import numpy as np
import sys
from math import sqrt
from itertools import combinations as cm
from qiskit import *
from qiskit.opflow import CircuitStateFn, StateFn #, I, Z 
from qiskit.opflow.gradients import Gradient
from numpy import log as ln
from qiskit.opflow.primitive_ops import PauliSumOp
from qiskit.quantum_info.operators import Operator
from qiskit.circuit import qpy_serialization
import pickle

def load_obj(name):
    with open(name + '.pkl', 'rb') as f:
        return pickle.load(f)

def main(arg):
    hyperparams = list(map(float, arg.split(" ")))

    # get the gradients of the circuit
    grad_obj = Gradient(grad_method="param_shift")

    with open('compression/src/main/python/Qcir_current.qpy', 'rb') as fd:
        cir = qpy_serialization.load(fd)[0]

    free_params = list(cir.parameters)   

    c = load_obj("./compression/src/main/python/Hamiltonian")
    # If you can try to run this on java this is the slowest part!
    op = ~StateFn(c) @ CircuitStateFn(primitive=cir, coeff=1.)
    grad_object = grad_obj.convert(operator=op, params=free_params)
    value_dict = {free_params[i]: hyperparams[i] for i in range(len(free_params))}
    x = grad_object.assign_parameters(value_dict).eval()
    
    print(np.real(x))
    

if __name__ == "__main__":
    main(sys.argv[1].strip())
