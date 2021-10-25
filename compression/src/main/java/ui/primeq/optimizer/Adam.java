package ui.primeq.optimizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.io.IOException;
import java.util.stream.DoubleStream;
import java.util.stream.Collectors;

import org.apache.commons.math3.util.Combinations;
import org.apache.commons.math3.analysis.function.Log;
import org.apache.commons.math3.analysis.function.Sqrt;
import org.ejml.data.ZMatrixRMaj;
import org.ejml.dense.row.CommonOps_ZDRM;
import org.ejml.dense.row.NormOps_ZDRM;

import ui.primeq.QuantumCircuitRunner;
import ui.primeq.config.Config;
import ui.primeq.storage.Storage;

public class Adam implements Optimizer {
    private AdamSettings adamSettings;
    private OptimizerSupportLevel gradientSupportLevel;
    private OptimizerSupportLevel boundsSupportLevel;
    private OptimizerSupportLevel initialpointSupportLevel;
    private double[] m;
    private double[] v;
    private double[] mTemp;
    private double[] vTemp;
    private double[] zeros;
    private Log ln;
    private Sqrt sqrt;
    private static final int[] primes = {2,3,5,7,11,13,17,19,23,29,31};
    private static int maxsize = 400; 


    public Adam(Config config){
        this.adamSettings = config.getAdamSettings();
        this.gradientSupportLevel = OptimizerSupportLevel.SUPPORTED;
        this.boundsSupportLevel = OptimizerSupportLevel.IGNORED;
        this.initialpointSupportLevel = OptimizerSupportLevel.SUPPORTED;
        this.ln = new Log();
        this.sqrt = new Sqrt();
        
        zeros = new double[maxsize];
        for(int i = 0; i < zeros.length; i++){
            zeros[i] = 0.0;
        }
    }

    public ArrayList<OptimizerSupportLevel> getSupportLevel() {
        ArrayList<OptimizerSupportLevel> dictionary = new ArrayList<>();
        dictionary.add(gradientSupportLevel);
        dictionary.add(boundsSupportLevel);
        dictionary.add(initialpointSupportLevel);
        return dictionary;
    }

    public ArrayList<Double> minimize(FunctionManager functionManager, ArrayList<Double> initialPoint, ZMatrixRMaj H, int n, int noPrimes, int numLayers) throws IOException {
        //Generate circuit ceofficients
        double[] cir_coeffs = this.generateCircuitCoeffs(n, noPrimes, numLayers);

        int param_size = initialPoint.size();
        double[] params = initialPoint.stream().mapToDouble(Double::doubleValue).toArray();
        double[] paramsNew = Arrays.copyOf(params, param_size);
        
        double[] dx = functionManager.gradientfunction(params, H, cir_coeffs);

        double t = 0;
        double beta1 = this.adamSettings.getBeta1();
        double beta2 = this.adamSettings.getBeta2();
        double lr = this.adamSettings.getLr();
        double noise = this.adamSettings.getNoiseFactor();
        
        m = Arrays.copyOf(zeros, param_size);
        v = Arrays.copyOf(zeros, param_size);
        mTemp = Arrays.copyOf(zeros, param_size);
        vTemp = Arrays.copyOf(zeros, param_size);

        double[] vEff = Arrays.copyOf(zeros, param_size);
        

        while (t < this.adamSettings.getMaxIter()) {
            if ( t % 100 == 0 && t > 0) {
                System.out.println("\n");
            }
                System.out.print(". ");

            if (t > 0) {
                dx = functionManager.gradientfunction(params, H, cir_coeffs);
            } 

            t += 1.0;
            double temp1 = 1.000 - beta1;
            double[] tempBeta1 = ListOperation.mul(dx, temp1);
            m = ListOperation.mul(m, beta1);
            m = ListOperation.add(m, tempBeta1);

            double temp2 = 1.000 - beta2;
            double[] tempBeta2 = ListOperation.mul(dx, dx);
            tempBeta2 = ListOperation.mul(tempBeta2, temp2);
            v = ListOperation.mul(v, beta2);
            v = ListOperation.add(v, tempBeta2);

            double lrEff = lr * Math.sqrt(1.000 - Math.pow(beta2, t)) / (1 - Math.pow(beta1, t));

            mTemp = Arrays.copyOf(m, param_size);
            vTemp = Arrays.copyOf(v, param_size);

            if (this.adamSettings.isAmsgrad() == false) {
                vTemp = ListOperation.sqrt(vTemp);
                vTemp = ListOperation.add(vTemp, noise);

                mTemp = ListOperation.mul(mTemp, lrEff);
                mTemp = ListOperation.divide(mTemp, vTemp);

                paramsNew = ListOperation.minus(params, mTemp);
            } else {
                vEff = ListOperation.maximum(vEff, vTemp);
                vTemp = ListOperation.sqrt(vEff);
                vTemp = ListOperation.add(vTemp, noise);
                mTemp = ListOperation.mul(mTemp, lrEff);
                mTemp = ListOperation.divide(mTemp, vTemp);
                paramsNew = ListOperation.minus(params, mTemp);
            }
            if (ListOperation.norm(ListOperation.minus(params, paramsNew)) < this.adamSettings.getTol()) {
                ArrayList<Double> result = DoubleStream.of(params).boxed().collect(Collectors.toCollection(ArrayList::new));
                return result;
            } else {
                params = Arrays.copyOf(paramsNew, param_size);
            }
        }
        ArrayList<Double> result =  DoubleStream.of(params).boxed().collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    private double[] generateCircuitCoeffs(int n, int noPrimes, int numLayers){
        int[] cir_primes =Arrays.copyOf(primes, noPrimes);
        
        int root_coeff = 1;
        for(int p : cir_primes){
            root_coeff *= p;
        }
        
        ArrayList<Double> circuit_coeffs = new ArrayList<Double>();

        for(int i = 0; i < numLayers; i++){
            
            for(int j = 0; j < noPrimes; j++){
               double coeff = 2 * this.ln.value((n) / this.sqrt.value(root_coeff)) * this.ln.value(cir_primes[j]) / (i+1); 
               circuit_coeffs.add(coeff);
            }

            Iterator<int[]> combinations = new Combinations(noPrimes, 2).iterator();
            while(combinations.hasNext()){
                int[] c  = combinations.next();
                double comb_coeff = this.ln.value(cir_primes[c[0]]) * this.ln.value(cir_primes[c[1]]) / (i+1);
                circuit_coeffs.add(comb_coeff);
            }

            for(int k = 0; k < noPrimes; k++){
                circuit_coeffs.add(1.0 / (i+1));
            }
        }

        double[] cir_coeffs = circuit_coeffs.stream().mapToDouble(Double::doubleValue).toArray();

        return cir_coeffs;
    }

    public void processUniqueValues(Config config, FunctionManager functionManager, Storage storage) throws IOException {
        
        int[] data = storage.getData();
        int[] numVars = config.getNumVars();
        int[] unique = Arrays.stream(data).distinct().toArray();
        // ArrayList<Integer> remainders = storage.getRemainders();
        System.out.println("Unique Values size = " + unique.length);

        ArrayList<Double> initialPoint =  new ArrayList<>();
        // HashMap<Integer, String> unique_map =  storage.getUniqueMap();

        int n = 0;
        int j = 0;
        int t = 0;

        while(j < unique.length){
            n = unique[j];
            if(n != 0){

                System.out.println("Running n = " + n + ":");
                ZMatrixRMaj H = QuantumCircuitRunner.generateCircuitFiles(n, config.getNoPrimes(), config.getNumLayers());
                double norm = NormOps_ZDRM.normF(H);
                ZMatrixRMaj H_normalized = CommonOps_ZDRM.elementDivide(H, norm, 0, null);

                while(t < config.getNoOfTimes()){
                    Random rand = new Random();
                    initialPoint.clear();
                    for(int i = 0; i < (numVars[config.getNoPrimes() - 2] * config.getNumLayers()); i++){
                        initialPoint.add(rand.nextDouble() * Math.PI);
                    }

                    ArrayList<Double> params = this.minimize(functionManager, initialPoint,  H, n, config.getNoPrimes(), config.getNumLayers());

                    String loss = functionManager.objectivefunction(QuantumCircuitRunner.run(params), n);
                    // System.out.println(loss);

                    String[] results = loss.split(",");
                    int r = n - Integer.valueOf(results[1]);
                    System.out.println("Remainder = " + r);
                    String sign = "0";
                    if (r < 0){
                        sign = "1";
                    }
                    // if (!remainders.contains(r)) {
                    //     remainders.add(Math.abs(r));
                    // }
                    storage.addRemainder(r);

                    String remainder = Integer.toBinaryString(Math.abs(r));
                    System.out.println(remainder);

                    String compressedData = results[0].trim() + sign + remainder;
                    // unique_map.put(n, compressed_data);
                    storage.addElementsToUniqueMap(n, compressedData);
                    System.out.println(compressedData);
                    t++;
                }
            }
            QuantumCircuitRunner.flush();
            System.out.println();
            j ++;
            t = 0;
        }
        return;
    }

    public OptimizerSupportLevel gradientSupportLevel() {
        return this.gradientSupportLevel;
    }

    public boolean isGradientIgnored() {
        return this.gradientSupportLevel.equals(OptimizerSupportLevel.IGNORED);
    }

    public boolean isGradientSupported() {
        return this.gradientSupportLevel.equals(OptimizerSupportLevel.SUPPORTED);
    }

    public boolean isGradientRequired() {
        return this.gradientSupportLevel.equals(OptimizerSupportLevel.REQUIRED);
    }

    public OptimizerSupportLevel boundsSupportLevel() {
        return this.boundsSupportLevel;
    }

    public boolean isBoundsIgnored() {
        return this.boundsSupportLevel.equals(OptimizerSupportLevel.IGNORED);
    }

    public boolean isBoundsSupported() {
        return this.boundsSupportLevel.equals(OptimizerSupportLevel.SUPPORTED);
    }

    public boolean isBoundsRequired() {
        return this.boundsSupportLevel.equals(OptimizerSupportLevel.REQUIRED);
    }
    public OptimizerSupportLevel initialPointSupportLevel() {
        return this.initialpointSupportLevel;
    }

    public boolean isInitialPointIgnored() {
        return this.initialpointSupportLevel.equals(OptimizerSupportLevel.IGNORED);
    }

    public boolean isInitialPointSupported() {
        return this.initialpointSupportLevel.equals(OptimizerSupportLevel.SUPPORTED);
    }

    public boolean isInitialPointRequired() {
        return this.initialpointSupportLevel.equals(OptimizerSupportLevel.REQUIRED);
    }

}
