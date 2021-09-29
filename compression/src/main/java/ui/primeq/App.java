package ui.primeq;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ui.primeq.optimizer.Adam;
import ui.primeq.optimizer.Function;

import java.util.Optional;
import java.util.Random;

import ui.primeq.optimizer.Optimizer;

import org.javatuples.Triplet;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        int maxiter = 10;
        int numVars = 5;
        Optional<Double> tol = Optional.of(1e-6);
        Optional<Double> lr = Optional.of(1.0);
        Optional<Double> beta1 = Optional.of(0.9);
        Optional<Double> beta2 = Optional.of(0.99);
        Optional<Double> noiseFactor = Optional.of(1e-10);
        Optional<Double> eps = Optional.of(1e-10);
        Optional<Boolean> amsgrad = Optional.of(false);
     
        Adam opt = new Adam(maxiter, tol, lr, beta1, beta2, noiseFactor, eps, amsgrad);
        
        quantum_circuit_runner runner = new quantum_circuit_runner();
        ArrayList<Double> vars =  new ArrayList<>();
        vars.add(6.0);
        vars.add(3.0);
        vars.add(1.0);

        vars.add(1.22);
        vars.add(6.0);
        vars.add(3.14);
        vars.add(6.0);
        vars.add(0.0);
        vars.add(6.0);
        vars.add(0.05);
        vars.add(6.0);
        vars.add(1.56);

        HashMap<String, Integer> counts = runner.run(vars);
        Function f = new Function("equation", 3);
        int x = f.objectivefunction(counts, 5);
        System.out.println(x);
        // ArrayList<Double> initialPoint = new ArrayList<Double>();

        // Random rand = new Random();
        // for(int i = 0; i < numVars; i++){
        //     initialPoint.add(rand.nextDouble() * Math.PI);
        // }

        // int t = 0;

        // while(t < 10){
        //     Triplet<List<Double>, Double, Double> res = opt.minimize(f, initialPoint);
        //     System.out.println(res);
        //     t++;
        // }
        
    }
}
