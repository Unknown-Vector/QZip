package ui.primeq;

import java.lang.System;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.*;
import java.io.File;

import org.ejml.data.Complex_F64;
import org.ejml.data.ZMatrixRMaj;
import org.ejml.dense.row.CommonOps_ZDRM;
import org.ejml.ops.ComplexMath_F64;

public class QuantumCircuitRunner {
        
    private static String stringify(ArrayList<Double> input){
        String listString = "";

        for(int i = 0; i < input.size(); i++){
            listString += input.get(i) + " ";
        }
        return listString;
    }

    private static ArrayList<ArrayList<ZMatrixRMaj>> refineStatevectorResults(String results){
        ArrayList<ArrayList<ZMatrixRMaj>> statvector_list = new ArrayList<>();
        ArrayList<ZMatrixRMaj> current_statevector = new ArrayList<ZMatrixRMaj>();
        Pattern MY_PATTERN = Pattern.compile("\\[(.*?)\\]");
        Matcher m = MY_PATTERN.matcher(results);
        while (m.find()) {
            String s = m.group(0);
            
            if(s.contains("[[") && !s.contains("[[[")){
                // System.out.println(s);
                statvector_list.add(current_statevector);
                current_statevector = new ArrayList<ZMatrixRMaj>();
            }

            s = s.replaceAll("\\[|\\]", "");
            String[] vals = s.split("\\),");
            ZMatrixRMaj sv_temp = new ZMatrixRMaj(vals.length, 1);

            for(int i = 0; i < vals.length; i++){
                String val = (vals[i].replaceAll("\\(|\\)", "")).trim();
                String[] Zval = val.split(",");
                sv_temp.set(i, 0, Double.valueOf(Zval[0]), Double.valueOf(Zval[1]));
            }
            current_statevector.add(sv_temp);

        }

        statvector_list.add(current_statevector);

        return statvector_list;
    }

    private static double[] refineHamiltonianResults(String results){
        String [] result_gradients = (results.replaceAll("\\[|\\]", "").trim()).split("\\s+");
        double [] hamiltonian_diagonals = new double[result_gradients.length * 2];

        for(int i = 0; i < result_gradients.length; i++){
            hamiltonian_diagonals[i * 2] = Double.valueOf(result_gradients[i]);
            hamiltonian_diagonals[(i * 2) + 1] =  0.0;
        }
        
        return hamiltonian_diagonals;
    }

    public static void flush(){
        File python_resource = new File("compression/src/main/python/Qcir_current.qpy"); 
        if (python_resource.delete()) { 
        } else {
                System.out.println("Failed to delete Circuit file.");
        }
    }

    public static ZMatrixRMaj generateCircuitFiles(int n, int no_primes, int no_layers){
        final String CircuitFile = "./compression/src/main/python/generate_qpy_circuit.py";
        final String HamilFile = System.getProperty("user.dir") + "/compression/src/main/python/generate_OpMatrix.py";
        String diagonals = "";

        // TODO: Change to multiprocessing if possible, currently uses python multiprocessing
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
                diagonals += o;
            }
        } catch(Exception e){
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
            while((o = out.readLine()) != null){}
        } catch(Exception e){
            e.printStackTrace();
        }

        double[] h = refineHamiltonianResults(diagonals);
        ZMatrixRMaj Hamiltonian = CommonOps_ZDRM.diag(h);
        System.out.println("Generated Hamiltonian and Circuit Files...");
        return Hamiltonian;
    }

    public static double[] Gradients(ArrayList<Double> input, ZMatrixRMaj H, double[] cir_coeffs) throws IOException{
        double[] graidents = new double[input.size()];
        ArrayList<ArrayList<ZMatrixRMaj>> statvectors = stateVectors(input);

        assert(statvectors.size() == 2 && statvectors.get(0).size() == cir_coeffs.length && cir_coeffs.length == statvectors.get(1).size());
     
        ArrayList<ZMatrixRMaj> p_shift = statvectors.get(0);
        ArrayList<ZMatrixRMaj> m_shift = statvectors.get(1);
        
        for(int i = 0; i < p_shift.size(); i++){
            ZMatrixRMaj p_pi = p_shift.get(i);
            ZMatrixRMaj m_pi = m_shift.get(i);
            
            ZMatrixRMaj con_pPi = CommonOps_ZDRM.transposeConjugate(p_pi, null);
            ZMatrixRMaj con_mPi = CommonOps_ZDRM.transposeConjugate(m_pi, null);

            ZMatrixRMaj Hp_Pi = p_pi.createLike();
            ZMatrixRMaj Hm_Pi = m_pi.createLike();
            ZMatrixRMaj obserMpi = new ZMatrixRMaj(1, 1);
            ZMatrixRMaj obserPpi = new ZMatrixRMaj(1 ,1);

            CommonOps_ZDRM.mult(H, p_pi, Hp_Pi);
            CommonOps_ZDRM.mult(H, m_pi, Hm_Pi);

            CommonOps_ZDRM.mult(con_pPi, Hp_Pi, obserPpi);
            CommonOps_ZDRM.mult(con_mPi, Hm_Pi, obserMpi);

            Complex_F64 p_PI = new Complex_F64();
            Complex_F64 m_PI = new Complex_F64();
            Complex_F64 diff = new Complex_F64();

            obserPpi.get(0, 0, p_PI);
            obserMpi.get(0, 0, m_PI);

            ComplexMath_F64.minus(p_PI, m_PI, diff);
            double grad = cir_coeffs[i] * 0.5 * diff.real;
            graidents[i] = (double) grad;
        }

        return graidents;
    }

    public static  ArrayList<ArrayList<ZMatrixRMaj>> stateVectors(ArrayList<Double> input) throws IOException{
        final String svFile = System.getProperty("user.dir") + "/compression/src/main/python/quantum_statevector.py";
        String var = stringify(input);
        String states = "";

        try{
            String[] cmd = new String[3];
            cmd[0] = "python";
            cmd[1] = svFile;
            cmd[2] = var;

            Runtime r = Runtime.getRuntime();
            Process p = r.exec(cmd);
            
            String o = "";

            BufferedReader out = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while((o = out.readLine()) != null){
                states += o;
            }
            
        } catch(Exception e){
            e.printStackTrace();
        }

        return refineStatevectorResults(states);
    }

    private static HashMap<String, Integer> refineCircuitResults(String results){
        HashMap<String, Integer> dict = new HashMap<String, Integer>();

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
            e.printStackTrace();
        }
        
        return refineCircuitResults(output);
    }
}