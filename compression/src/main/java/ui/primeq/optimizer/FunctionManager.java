package ui.primeq.optimizer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.DoubleStream;
import java.util.stream.Collectors;
import java.io.IOException;

import org.apache.commons.lang3.ArrayUtils;
import org.ejml.data.ZMatrixRMaj;

import ui.primeq.QuantumCircuitRunner;

public class FunctionManager {
    private int[] primes = {2,3,5,7,11,13,17,19,23,29,31};
    private int no_primes = 0;


    public FunctionManager(int no_primes) {
        this.no_primes = no_primes;
    }

    public double[] gradientfunction(double[] parameters, ZMatrixRMaj H, double[] cir_coeffs) throws IOException {
        //generate circuit coeffs.

        // z.addAll(y);
        // z.addAll(x);
        // System.out.println(z);
        // long startTime = System.nanoTime();
        ArrayList<Double> params = DoubleStream.of(parameters).boxed().collect(Collectors.toCollection(ArrayList::new));
        // System.out.println("PARAMS = " + params);
        double[] graidents = QuantumCircuitRunner.Gradients(params, H, cir_coeffs);
        // for(int l = 0; l < graidents.length; l++){
        //     System.out.println("DX = " + graidents[l]);
        // }
        // long endTime = System.nanoTime();

        // long duration = (endTime - startTime);
        // System.out.println("gradient : " + duration);
        return graidents;
    }

    public String objectivefunction(HashMap<String, Integer> m, int n) {
        assert no_primes > 0;
        int[] circuit_primes = Arrays.copyOf(primes, this.no_primes);
        ArrayUtils.reverse(circuit_primes);
    
        int result = 1;
        String max_bit = m.entrySet().stream().max((entry1, entry2) -> Integer.compare(entry1.getValue(), entry2.getValue())).get().getKey();
        // max_bit = new StringBuilder(max_bit).toString();
        
        char[] bit_string =  max_bit.toCharArray();//String.valueOf(max_bit).toCharArray();

        for(int i = 0; i < bit_string.length; i++){
            int exp = Character.getNumericValue(bit_string[i]);
            result *= (int)Math.pow(circuit_primes[i], exp);
        }
        System.out.println("Est. n = " + result + "   Bit String = " + max_bit);

        return max_bit + "," + Integer.toString(result);
    }
}
