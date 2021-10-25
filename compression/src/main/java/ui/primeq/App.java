package ui.primeq;

import java.io.IOException;
// import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import ui.primeq.config.Config;
import ui.primeq.optimizer.Adam;
import ui.primeq.optimizer.FunctionManager;
import org.ejml.data.ZMatrixRMaj;
import org.ejml.dense.row.CommonOps_ZDRM;
import org.ejml.dense.row.NormOps_ZDRM;

public class App {
    public static void main( String[] args ) throws IOException {
        // for (String elem : args)
        //     System.out.println(elem);

        //Initialize Config
        Config config = new Config();
        config = config.initConfig();

        // Initialize constants
        int[] numVars = config.getNumVars();
        int n = 0;
        String nameOfFile = "sample";
        
        // Initialize Managers
        FileManager fileManager =  new FileManager();
        FunctionManager functionManager = new FunctionManager(config.getNoPrimes());

        // Initialize Optimizer
        Adam opt = new Adam(config.getAdamSettings());

        // Initialize initialPoint and HashMap to contain compressed integers and remainder list
        ArrayList<Double> initialPoint =  new ArrayList<>();
        ArrayList<Integer> remainders = new ArrayList<>();
        HashMap<Integer, String> unique_map =  new HashMap<>();
        // Stack<Integer> zeros_stack = new Stack<>();
        // Convert File into int array to be processed
        int[] data = fileManager.readFile("./" + nameOfFile + ".txt");
        // System.out.println("DATA RAW:");
        // for(int j = 0; j < data.length; j++){
        //    zeros_stack.push(data[j]);
        // }
        // int c = 0;
        // while(zeros_stack.pop() == 0){
        //     c++;
        // }
        // System.out.println(c);
        int[] unique = Arrays.stream(data).distinct().toArray();
        System.out.println("Unique Values size = " + unique.length);

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

                    ArrayList<Double> params = opt.minimize(functionManager, initialPoint,  H, n, config.getNoPrimes(), config.getNumLayers());

                    String loss = functionManager.objectivefunction(QuantumCircuitRunner.run(params), n);
                    // System.out.println(loss);

                    String[] results = loss.split(",");
                    int r = n - Integer.valueOf(results[1]);
                    System.out.println("Remainder = " + r);
                    String sign = "0";
                    if (r < 0){
                        sign = "1";
                    }
                    if (!remainders.contains(r)) {
                        remainders.add(Math.abs(r));
                    }

                    String remainder = Integer.toBinaryString(Math.abs(r));

                    String compressed_data = results[0].trim() + sign + remainder;
                    unique_map.put(n, compressed_data);
                    t++;
                }
            }
            QuantumCircuitRunner.flush();
            System.out.println();
            j ++;
            t = 0;
        }

        // System.out.println(unique_map);
        fileManager.generateCompressedFile(config.getNoPrimes(), nameOfFile, unique_map, data, remainders);
        // fileManager.readFile("sample.txt");
        // System.out.println("Running Decompression");
        // fileManager.decompress("test2_sampleCompressed.txt");
        // System.out.println("reading Decompression");
        // fileManager.readFile("test2_sampleCompressedDeCompressed.txt");
    }
}
