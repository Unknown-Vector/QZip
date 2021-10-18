package ui.primeq;

import java.io.IOException;
// import java.util.HashMap;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;
import java.util.Arrays;
import java.util.HashMap;

import ui.primeq.optimizer.Adam;
import ui.primeq.optimizer.FunctionManager;
import org.ejml.data.ZMatrixRMaj;
import org.ejml.dense.row.CommonOps_ZDRM;
import org.ejml.dense.row.NormOps_ZDRM;
import java.nio.ByteBuffer;

public class App {
    public static void main( String[] args ) throws IOException {
        for (String elem : args)
            System.out.println(elem);
        int maxiter = 1000;
        int[] numVars = {5, 9, 14, 20, 27, 35, 44, 54, 65};
        int numLayers = 5;
        int noOfTimes = 1;
        int noPrimes = 5;
        int n = 0;
        Optional<Double> tol = Optional.of(1e-10);
        Optional<Double> lr = Optional.of(Math.PI / 1000);
        Optional<Double> beta1 = Optional.of(0.9);
        Optional<Double> beta2 = Optional.of(0.99);
        Optional<Double> noiseFactor = Optional.of(1e-7);
        Optional<Double> eps = Optional.of(1e-10);
        Optional<Boolean> amsgrad = Optional.of(false);
        String nameOfFile = "sample";
        
        FileManager fileManager =  new FileManager();
        byte[] data = fileManager.readFileAsBytes("./" + nameOfFile + ".txt");

        int len = data.length;
        ByteBuffer bytebuf;
        // if(data.length % 2 != 0){
        //     byte[] data_temp = new byte[data.length + 1];
        //     System.arraycopy(data, 0, data_temp, 0, data.length);
        //     bytebuf = ByteBuffer.wrap(data_temp);
        //     len ++;
        // }else{
            bytebuf = ByteBuffer.wrap(data);
        // }
        
        int[] data_int = new int[len];

        int k = 0;
        while(bytebuf.remaining() > 0){          
            Byte x = bytebuf.get();
            // String b = x.toString();
            System.out.println((int) x);
            data_int[k] = (int) x;
            k++;
        }
        // for(int i = 0; i < len; i++){
        //     data_int[i] = (int) data[i];
        // }

        int[] unique = Arrays.stream(data_int).distinct().toArray();
        System.out.println("Unique Values size = " + unique.length);


        Adam opt = new Adam(maxiter, tol, lr, beta1, beta2, noiseFactor, eps, amsgrad);
        FunctionManager functionManager = new FunctionManager(noPrimes);
        
        ArrayList<Double> initialPoint =  new ArrayList<>();
        HashMap<Integer, String> unique_map =  new HashMap<>();

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
        fileManager.generateCompressedFile(nameOfFile, unique_map, data_int);

    }
}
