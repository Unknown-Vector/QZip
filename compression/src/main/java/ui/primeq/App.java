package ui.primeq;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import ui.primeq.optimizer.Adam;
import ui.primeq.optimizer.FunctionManager;

public class App {
    public static void main( String[] args ) throws IOException {
        int maxiter = 100;
        int numVars = 5;
        int noOfTimes = 1;
        int noPrimes = 2;
        int n = 2;
        Optional<Double> tol = Optional.of(1e-8);
        Optional<Double> lr = Optional.of(1.0);
        Optional<Double> beta1 = Optional.of(0.9);
        Optional<Double> beta2 = Optional.of(0.99);
        Optional<Double> noiseFactor = Optional.of(1e-10);
        Optional<Double> eps = Optional.of(1e-8);
        Optional<Boolean> amsgrad = Optional.of(false);
        
        Adam opt = new Adam(maxiter, tol, lr, beta1, beta2, noiseFactor, eps, amsgrad);
        FunctionManager functionManager = new FunctionManager(noPrimes);
        
        ArrayList<Double> initialPoint =  new ArrayList<>();
      
        int t = 0;
        while(n < 7){
            System.out.println("Running n = " + n + ":");
            QuantumCircuitRunner.generateCircuitFiles(n, noPrimes, 1);

            while(t < noOfTimes){

                Random rand = new Random();
                initialPoint.clear();
                for(int i = 0; i < numVars; i++){
                    initialPoint.add(rand.nextDouble() * Math.PI);
                }
                
                ArrayList<Double> params = opt.minimize(functionManager, initialPoint);

                int loss = functionManager.objectivefunction(QuantumCircuitRunner.run(params), n);
                System.out.println(loss);
                t++;
            }

            QuantumCircuitRunner.flush();
            System.out.println();
            n ++;
            t = 0;
        }

    }
}
