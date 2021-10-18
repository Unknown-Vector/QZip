package ui.primeq;

import java.io.IOException;
// import java.util.HashMap;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.HashMap;

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
        // Initialize constants
        int[] numVars = {5, 9, 14, 20, 27, 35, 44, 54, 65};
        int numLayers = 5;
        int noOfTimes = 1;
        int noPrimes = 5;
        int n = 0;
        String nameOfFile = "sample";
        
        // Initialize Managers
        FileManager fileManager =  new FileManager();
        FunctionManager functionManager = new FunctionManager(noPrimes);
        
        //Initialize Config
        Config config = new Config();
        config = config.initConfig();

        // Initialize Optimizer
        Adam opt = new Adam(config.getAdamSettings());

        // Initialize initialPoint and HashMap to contain compressed integers
        ArrayList<Double> initialPoint =  new ArrayList<>();
        HashMap<Integer, String> unique_map =  new HashMap<>();

        // Convert File into int array to be processed
        int[] data = fileManager.readFile("./" + nameOfFile + ".txt");
        int[] unique = Arrays.stream(data).distinct().toArray();
        System.out.println("Unique Values size = " + unique.length);
        
        

        int j = 0;
        int t = 0;

        while(j < unique.length){
            n = unique[j];
            if(n != 0){

                System.out.println("Running n = " + n + ":");
                ZMatrixRMaj H = QuantumCircuitRunner.generateCircuitFiles(n, noPrimes, numLayers);
                double norm = NormOps_ZDRM.normF(H);
                ZMatrixRMaj H_normalized = CommonOps_ZDRM.elementDivide(H, norm, 0, null);

                while(t < noOfTimes){
                    Random rand = new Random();
                    initialPoint.clear();
                    for(int i = 0; i < (numVars[noPrimes - 2] * numLayers); i++){
                        initialPoint.add( rand.nextDouble() * Math.PI);
                    }

                    ArrayList<Double> params = opt.minimize(functionManager, initialPoint,  H_normalized, n, noPrimes, numLayers);

                    String loss = functionManager.objectivefunction(QuantumCircuitRunner.run(params), n);
                    // System.out.println(loss);

                    String[] results = loss.split(",");
                    int r = n - Integer.valueOf(results[1]);
                    System.out.println("Remainder = " + r);
                    String sign = "0";
                    if (r < 0){
                        sign = "1";
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

        System.out.println(unique_map);
        fileManager.generateCompressedFile(nameOfFile, unique_map, data);

    }
}
