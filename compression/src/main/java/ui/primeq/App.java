package ui.primeq;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import ui.primeq.optimizer.Adam;
import ui.primeq.optimizer.FunctionManager;

public class App {
    public static void main( String[] args ) throws IOException
    {
        int maxiter = 10;
        int numVars = 44;
        int noOfTimes = 10;
        int noPrimes = 8;
        int n = 5;
        Optional<Double> tol = Optional.of(1e-6);
        Optional<Double> lr = Optional.of(1.0);
        Optional<Double> beta1 = Optional.of(0.9);
        Optional<Double> beta2 = Optional.of(0.99);
        Optional<Double> noiseFactor = Optional.of(1e-10);
        Optional<Double> eps = Optional.of(1e-10);
        Optional<Boolean> amsgrad = Optional.of(false);
     
        Adam opt = new Adam(maxiter, tol, lr, beta1, beta2, noiseFactor, eps, amsgrad);
        FunctionManager functionManager = new FunctionManager(noPrimes);
        
        
        ArrayList<Double> initialPoint =  new ArrayList<>();
        ArrayList<Double> paramList = new ArrayList<>();
        paramList.add(6.0);
        paramList.add((double) noPrimes);
        paramList.add(1.0);

        // System.out.println(initialPoint);

        int t = 0;

        while(t < noOfTimes){

            Random rand = new Random();
            initialPoint.clear();
            for(int i = 0; i < numVars; i++){
                initialPoint.add(rand.nextDouble() * Math.PI);
            }

            ArrayList<Double> res = new ArrayList<>();
            // System.out.println("2");
            // ArrayList<Double> params = opt.minimize(functionManager, initialPoint, paramList);
            res.addAll(paramList);
            res.addAll(initialPoint);
            // int loss = functionManager.objectivefunction(QuantumCircuitRunner.run(res), n);
            // System.out.println(loss);
            long startTime = System.nanoTime();
            ArrayList<Double> x = QuantumCircuitRunner.Gradients(res);
            long stopTime = System.nanoTime();
            double elapsedTimeInSecond = (double) (stopTime - startTime) / 1_000_000_000;
            System.out.println(elapsedTimeInSecond);
            t++;
        }
        
    }
}
