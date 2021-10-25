import sys
from qiskit import *
from copy import deepcopy
from numpy import round
from numpy import pi as PI
from qiskit.circuit import qpy_serialization
import timeit
## For Multiprocessing
import multiprocessing

def stateVectorForj(j, shift, hyperparams, quantum_circuit):
    sim = Aer.get_backend('statevector_simulator')
    cir = deepcopy(quantum_circuit)    
    free_params = list(cir.parameters)   

    shift_param = cir._parameter_table[free_params[j]][0][0]
    shift_param.params[0] = shift_param.params[0] + shift
            
    p_dict = {free_params[i] : hyperparams[i] for i in range(len(free_params))}
    cir.assign_parameters(p_dict, inplace=True)

            # cir.save_statevector()
    result = sim.run(cir).result()
    sv = round(result.get_statevector(cir), 10) # round to 15 decimal places
    vec = [(float(c.real), float(c.imag)) for c in sv]

    return vec

def main(arg):
    hyperparams = list(map(float, arg.split(" "))) 
    statevec = []
    # main_direcotry = "/compression/src/main/python/"
    with open('./compression/src/main/python/Qcir_current.qpy', 'rb') as fd:
        quantum_circuit = qpy_serialization.load(fd)[0]

    for shift in [PI/2, -PI/2]:
        pool = multiprocessing.Pool(processes = 15)
        stateIter = [(i, shift, hyperparams, quantum_circuit,) for i in range(len(hyperparams))]
        vec = pool.starmap(stateVectorForj, stateIter)
        statevec.append(vec)
    
    print(statevec)
    

if __name__ == "__main__":
    main(sys.argv[1].strip())
    