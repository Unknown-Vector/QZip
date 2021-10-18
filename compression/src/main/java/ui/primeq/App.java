package ui.primeq;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.Arrays;
import ui.primeq.optimizer.Adam;
import ui.primeq.optimizer.FunctionManager;

public class App {
    public static void main( String[] args ) throws IOException {
        for (String elem : args)
            System.out.println(elem);
        int maxiter = 100;
        int[] numVars = {5, 9, 14, 20, 27, 35, 44};
        int numLayers = 1;
        int noOfTimes = 1;
        int noPrimes = 5;
        int n = 0;
        Optional<Double> tol = Optional.of(1e-8);
        Optional<Double> lr = Optional.of(0.1);
        Optional<Double> beta1 = Optional.of(0.9);
        Optional<Double> beta2 = Optional.of(0.99);
        Optional<Double> noiseFactor = Optional.of(1e-10);
        Optional<Double> eps = Optional.of(1e-8);
        Optional<Boolean> amsgrad = Optional.of(false);
        
        FileManager fileManager =  new FileManager();
        byte[] data = fileManager.readFileAsBytes("./weed.jpeg");
        int[] data_int = new int[data.length];
        for(int i = 0; i < data.length; i++){
            data_int[i] = (int) (data[i] & 0xFF);
        }

        int[] unique = Arrays.stream(data_int).distinct().toArray();
        System.out.println("Unique Values size = " + unique.length);
        // for(int i = 0; i < unique.length; i++){
        //     System.out.println(unique[i]);
        // }


        Adam opt = new Adam(maxiter, tol, lr, beta1, beta2, noiseFactor, eps, amsgrad);
        FunctionManager functionManager = new FunctionManager(noPrimes);
        
        ArrayList<Double> initialPoint =  new ArrayList<>();
        int j = 0;
        int t = 0;
        while(j < unique.length){
            n = unique[j];
            System.out.println("Running n = " + n + ":");
            QuantumCircuitRunner.generateCircuitFiles(n, noPrimes, numLayers);

            while(t < noOfTimes){
                if(n == 0){
                    break;
                }
                Random rand = new Random();
                initialPoint.clear();
                for(int i = 0; i < (numVars[noPrimes - 2] * numLayers); i++){
                    initialPoint.add(Math.PI);
                }
                
                ArrayList<Double> params = opt.minimize(functionManager, initialPoint);

                int loss = functionManager.objectivefunction(QuantumCircuitRunner.run(params), n);
                System.out.println(loss);
                t++;
            }

            QuantumCircuitRunner.flush();
            System.out.println();
            j ++;
            t = 0;
        }

    }
}
