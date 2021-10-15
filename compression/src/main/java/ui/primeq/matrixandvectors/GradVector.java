// package ui.primeq.matrixandvectors;

// import java.util.ArrayList;
// import org.apache.commons.math3.complex.Complex;

// public class GradVector {
//     ArrayList<Complex[][]> stateVectorList;

//     public GradVector(ArrayList<Complex[][]> stateVectorList) {
//         this.stateVectorList = stateVectorList;
//     }

//     public Complex[][] getStateVector(int index) {
//         return this.stateVectorList.get(index);
//     }

//     public Complex[][] getStateVectorConjugate(int index) {
//         Complex[][] stateVector = this.getStateVector(index);
//         Complex[][] results = new Complex[stateVector.length][stateVector[0].length];
//         for (int i = 0; i < stateVector.length; i++) {
//             for (int j = 0; j < stateVector[i].length; j++){
//                 results[i][j] = stateVector[i][j].conjugate();
//             }
//         }
//         return results;
//     }

//     public Complex[][] applyStateVectorToHamiltonion(Hamiltonian h, int index){
//         Complex[][] stateVector = this.getStateVector(index);
//         Complex[][] stateVectorConjugate = this.getStateVectorConjugate(index);
//         return 
//     }

//     public Complex[][] calculate() {
//         Complex[][] results 

//         return results;
//     }

// }
