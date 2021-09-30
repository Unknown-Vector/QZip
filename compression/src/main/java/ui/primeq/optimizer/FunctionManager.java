package ui.primeq.optimizer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import java.io.IOException;

import ui.primeq.QuantumCircuitRunner;

public class FunctionManager {
    private int[] primes = {2,3,5,7,11,17,19,23,29,31};
    private int no_primes = 0;


    public FunctionManager(int no_primes) {
        this.no_primes = no_primes;
    }

    public ArrayList<Double> gradientfunction(ArrayList<Double> x, ArrayList<Double> y) throws IOException {
        ArrayList<Double> z = new ArrayList<>();
        z.addAll(y);
        z.addAll(x);
        // System.out.println(z);
        return QuantumCircuitRunner.Gradients(z);
    }

    public int objectivefunction(HashMap<String, Integer> m, int n) {
        assert no_primes > 0;

        int result = 1;
        String max_bit = m.entrySet().stream().max((entry1, entry2) -> Integer.compare(entry1.getValue(), entry2.getValue())).get().getKey();
        max_bit = new StringBuilder(max_bit).reverse().toString();

        char[] bit_string = String.valueOf(max_bit).toCharArray();

        for(int i = 0; i < bit_string.length; i++){
            int exp = Character.getNumericValue(bit_string[i]);
            result *= (int)Math.pow(primes[i], exp);
        }
        
        return Math.abs(n - result);
    }
}
