import sys
from qiskit import *
import numpy as np
from qiskit.circuit import qpy_serialization


def main(arg):
    hyperparams = list(map(float, arg.split(" "))) 
    statevec = []

    for shift in [np.pi/2, -np.pi/2]:
        temp = []
        for j in range(len(free_params)):
            with open('./Qcir_current.qpy', 'rb') as fd:
                cir = qpy_serialization.load(fd)[0]

            free_params = list(cir.parameters)   

            shift_param = cir._parameter_table[free_params[j]][0][0]
            shift_param.params[0] = shift_param.params[0] + shift

            p_dict = {free_params[i] : hyperparams[i] for i in range(len(free_params))}
            cir.assign_parameters(p_dict, inplace=True)

            sim = Aer.get_backend('aer_simulator')
            cir.save_statevector()
            qobj = assemble(cir)     # Create a Qobj from the circuit for the simulator to run
            result = sim.run(qobj).result()
            sv = np.round(result.get_statevector(cir), 15) # round to 15 decimal places
            vec = [(float(c.real), float(c.imag)) for c in sv]
            temp.append(vec)
    statevec.append(temp)

    print(statevec)
    

if __name__ == "__main__":
    main(sys.argv[1].strip())