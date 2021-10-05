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

    with open('compression/src/main/python/Qcir_current.qpy', 'rb') as fd:
        cir = qpy_serialization.load(fd)[0]

    free_params = list(cir.parameters)   
    statevec = []

    for shift in [np.pi/2, -np.pi/2]:
        temp = []
        for j in range(len(free_params)):
            with open('./Qcir_current.qpy', 'rb') as fd:
                cir = qpy_serialization.load(fd)[0]

            free_params = list(cir.parameters)   
            hyperparams[j] += shift
            p_dict = {free_params[i] : hyperparams[i] for i in range(len(free_params))}
            cir.assign_parameters(p_dict, inplace=True)

            sim = Aer.get_backend('aer_simulator')
            cir.save_statevector()
            qobj = assemble(cir)     # Create a Qobj from the circuit for the simulator to run
            result = sim.run(qobj).result()
            sv = result.get_statevector(cir)
            vec = [(float(c.real), float(c.imag)) for c in sv]
            temp.append(vec)
    statevec.append(temp)

    print(statevec)
    

if __name__ == "__main__":
    main(sys.argv[1].strip())