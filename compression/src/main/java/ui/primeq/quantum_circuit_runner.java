package ui.primeq;

import java.lang.System;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class quantum_circuit_runner {
    private String output = ""; 
    private String circuitFile = System.getProperty("user.dir") + "/compression/src/main/python/quantum_circuit_run.py";

    public quantum_circuit_runner(){}
    
    private String stringify(ArrayList<Double> input){
        String listString = "";

        for(int i = 0; i < input.size(); i++){
            listString += input.get(i) + " ";
        }
        return listString;
    }

    private HashMap<String, Integer> refineResults(String results){
        HashMap<String, Integer> dict = new HashMap<String, Integer>();

        results = results.replaceAll("[{}]", "");
        String[] res_array = results.split(",");
        
        for(int i = 0; i < res_array.length; i++){
            String temp = res_array[i];
            String[] temp_arr = temp.split(":");    
            dict.put(temp_arr[0].replaceAll("'", "").trim(), Integer.valueOf(temp_arr[1].trim()));
        }
        
        this.output = "";

        return dict;
    }

    public HashMap<String, Integer> run(ArrayList<Double> input) throws IOException{
        String var = this.stringify(input);
        
        try{
            String[] cmd = new String[3];
            cmd[0] = "python3";
            cmd[1] = this.circuitFile;
            cmd[2] = var;

            Runtime r = Runtime.getRuntime();
            Process p = r.exec(cmd);
            
            String o = "";
            BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((o = out.readLine()) != null){
                this.output += o;
            }
        } catch(Exception e){
            System.out.println(e.toString());
            e.printStackTrace();
        }

        return this.refineResults(this.output);
    }
}
