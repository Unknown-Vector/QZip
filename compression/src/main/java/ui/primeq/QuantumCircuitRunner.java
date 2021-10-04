package ui.primeq;

import java.lang.System;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File; 

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

    public static void flush(){
        File python_resource = new File("compression/src/main/python/Hamiltonian.npy"); 
            if (python_resource.delete()) { 
                System.out.println("Flushed : " + python_resource.getName());
            } else {
                System.out.println("Failed to delete the file.");
            }
            python_resource = new File("compression/src/main/python/Qcir_current.qpy"); 
            if (python_resource.delete()) { 
                System.out.println("Flushed :  " + python_resource.getName());
            } else {
                System.out.println("Failed to delete the file.");
            }
    }

    public static void generateCircuitFiles(int n, int no_primes, int no_layers){
        final String CircuitFile = "./compression/src/main/python/generate_qpy_circuit.py";
        final String HamilFile = System.getProperty("user.dir") + "/compression/src/main/python/generate_OpMatrix.py";
        // Please optimize this process with multithreading later!
        try{
            String[] cmd = new String[3];
            cmd[0] = "python";
            cmd[1] = HamilFile;
            cmd[2] = String.valueOf(n) + " " + String.valueOf(no_primes);
            
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(cmd);
            
            String o = "";
            
            BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((o = out.readLine()) != null){
                System.out.println(o);
            }
        } catch(Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }
        
        try{
            String[] cmd = new String[3];
            cmd[0] = "python";
            cmd[1] = CircuitFile;
            cmd[2] = String.valueOf(n) + " " + String.valueOf(no_primes) + " " + String.valueOf(no_layers);
            
            Runtime r = Runtime.getRuntime();
            Process p = r.exec(cmd);
            
            String o = "";
            
            BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((o = out.readLine()) != null){
                System.out.println(o);
            }
        } catch(Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }

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
            long startTime = System.nanoTime();
            BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            System.out.println("bufferedreader* : " + duration);
            startTime = System.nanoTime();
            while((o = out.readLine()) != null){
                grad += o;
            }
            endTime = System.nanoTime();
            duration = (endTime - startTime);
            System.out.println("readline* : " + duration);
            
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