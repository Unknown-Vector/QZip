import sys
from qiskit.opflow import I, Z 
from numpy import log as ln
from numpy import save
from numpy import diagonal, round, real
import pickle

def save_obj(obj, name ):
    with open(name + '.pkl', 'wb') as f:
        pickle.dump(obj, f, pickle.HIGHEST_PROTOCOL)

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

def main(n, no_primes):
    primes = [2,3,5,7,11,17,19,23,29,31]
    primes = (primes[0 : no_primes])[::-1]

    ham = hamiltonian(n, primes)
    Ham_matrix = ham.to_matrix()
    # save("compression/src/main/python/Hamiltonian.npy", Ham_matrix)
    # save_obj(Ham_matrix,"compression/src/main/python/Hamiltonian")
    print(round(diagonal(real(Ham_matrix)), 15))
    # print("Generated Hamiltonian File......")


if __name__ == "__main__":
    input = sys.argv[1].split(" ")
    main(int(input[0]), int(input[1]))