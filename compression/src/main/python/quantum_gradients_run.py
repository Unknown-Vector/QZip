import sys
from itertools import combinations as cm
from qiskit import *
from qiskit.opflow import CircuitStateFn, StateFn
from qiskit.opflow.gradients import Gradient
from numpy import log as ln
from numpy import load 
import numpy as np
from qiskit.opflow.primitive_ops import PauliSumOp, PrimitiveOp
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
    # print(arg)
    c = load("./compression/src/main/python/Hamiltonian.npy")
    # If you can, try to run this on java, this is the slowest part!
    h = PrimitiveOp(Operator(c))
    op = ~StateFn(h) @ CircuitStateFn(primitive=cir, coeff=1.)
    # print("HEY")
    grad_object = grad_obj.convert(operator=op, params=free_params)
    value_dict = {free_params[i]: hyperparams[i] for i in range(len(free_params))}
    x = grad_object.assign_parameters(value_dict).eval()
    
    print(np.real(x))
    

if __name__ == "__main__":
    main(sys.argv[1].strip())
