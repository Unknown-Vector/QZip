package ui.primeq;

import java.lang.System;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.Random;

public class QuantumCircuitRunner {
        
    private static String stringify(ArrayList<Double> input){
        String listString = "";

        for(int i = 0; i < input.size(); i++){
            listString += input.get(i) + " ";
        }
        return listString;
    }

    private static ArrayList<Double> refineGradientResults(String results){
        ArrayList<Double> gradient_list = new ArrayList<Double>();

        // Extract graidents from result string
        String [] result_gradients = (results.replaceAll("\\[|\\]", "").trim()).split("\\s+");
        for(int i = 0; i < result_gradients.length; i++){
            gradient_list.add(Double.valueOf(result_gradients[i]));
        }

                
        return gradient_list;
    }

    public static ArrayList<Double> Gradients(ArrayList<Double> input) throws IOException{
        final String gradientFile = System.getProperty("user.dir") + "/compression/src/main/python/quantum_gradients_run.py";
        String var = stringify(input);
        String grad = "";

        try{
            String[] cmd = new String[3];
            cmd[0] = "python";
            cmd[1] = gradientFile;
            cmd[2] = var;

            Runtime r = Runtime.getRuntime();
            Process p = r.exec(cmd);
            
            String o = "";
            BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((o = out.readLine()) != null){
                grad += o;
            }
        } catch(Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }

        return refineGradientResults(grad);
    }

    private static HashMap<String, Integer> refineCircuitResults(String results){
        HashMap<String, Integer> dict = new HashMap<String, Integer>();
        
        // Extract counts from result string
        String results_counts = results.replaceAll("[{}]", "");
        String[] res_array = results_counts.split(",");
        
        for(int i = 0; i < res_array.length; i++){
            String temp = res_array[i];
            String[] temp_arr = temp.split(":");    
            dict.put(temp_arr[0].replaceAll("'", "").trim(), Integer.valueOf(temp_arr[1].trim()));
        }
        
        return dict;
    }

    public static HashMap<String, Integer> run(ArrayList<Double> input) throws IOException{
        final String circuitFile = System.getProperty("user.dir") + "/compression/src/main/python/quantum_circuit_run.py";
        String var = stringify(input);
        
        // Clear all previous results for new results
        String output = "";
        
        try{
            String[] cmd = new String[3];
            cmd[0] = "python";
            cmd[1] = circuitFile;
            cmd[2] = var;

            Runtime r = Runtime.getRuntime();
            Process p = r.exec(cmd);
            
            String o = "";
            BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((o = out.readLine()) != null){
                output += o;
            }
        } catch(Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }

        return refineCircuitResults(output);
    }
}
