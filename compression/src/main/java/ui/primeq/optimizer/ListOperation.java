package ui.primeq.optimizer;

import org.apache.commons.math3.util.MathArrays;

import java.util.ArrayList;
import java.util.stream.DoubleStream;
import java.util.stream.Collectors;

public class ListOperation extends ArrayList<Double>{

    public static ArrayList<Double> add(ArrayList<Double> a, ArrayList<Double> b){
        if (a.size() != b.size()) {
            System.out.println("Error: " + a.size() + "/" + b.size());
        }

        double[] aArray = new double[a.size()];
        double[] bArray = new double[b.size()];

        aArray = a.stream().mapToDouble(Double::doubleValue).toArray();
        bArray = b.stream().mapToDouble(Double::doubleValue).toArray();
        
        double[] rArray = MathArrays.ebeAdd(aArray, bArray);

        ArrayList<Double> result = new ArrayList<>();
        result = DoubleStream.of(rArray).boxed().collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    public static ArrayList<Double> add(ArrayList<Double> a, double b){
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) + b);
        }
        return result;
    }

    public static ArrayList<Double> minus(ArrayList<Double> a, ArrayList<Double> b){
        if (a.size() != b.size()) {
            System.out.println("Error: " + a.size() + "/" + b.size());
        }

        double[] aArray = new double[a.size()];
        double[] bArray = new double[b.size()];

        aArray = a.stream().mapToDouble(Double::doubleValue).toArray();
        bArray = b.stream().mapToDouble(Double::doubleValue).toArray();
        
        double[] rArray = MathArrays.ebeSubtract(aArray, bArray);

        ArrayList<Double> result = new ArrayList<Double>();
        result = DoubleStream.of(rArray).boxed().collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    public static ArrayList<Double> minus(ArrayList<Double> a, double b){
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) - b);
        }
        return result;
    }

    public static ArrayList<Double> mul(ArrayList<Double> a, ArrayList<Double> b){
        if (a.size() != b.size()) {
            System.out.println("Error: " + a.size() + "/" + b.size());
        }
        double[] aArray = new double[a.size()];
        double[] bArray = new double[b.size()];

        aArray = a.stream().mapToDouble(Double::doubleValue).toArray();
        bArray = b.stream().mapToDouble(Double::doubleValue).toArray();
        
        double[] rArray = MathArrays.ebeMultiply(aArray, bArray);

        ArrayList<Double> result = new ArrayList<>();
        result = DoubleStream.of(rArray).boxed().collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    public static ArrayList<Double> mul(ArrayList<Double> a, double b){
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) * b);
        }
        return result;
    }

    public static ArrayList<Double> divide(ArrayList<Double> a, ArrayList<Double> b){
        if (a.size() != b.size()) {
            System.out.println("Error: " + a.size() + "/" + b.size());
        }
        double[] aArray = new double[a.size()];
        double[] bArray = new double[b.size()];

        aArray = a.stream().mapToDouble(Double::doubleValue).toArray();
        bArray = b.stream().mapToDouble(Double::doubleValue).toArray();
        
        double[] rArray = MathArrays.ebeDivide(aArray, bArray);

        ArrayList<Double> result = new ArrayList<>();
        result = DoubleStream.of(rArray).boxed().collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    public static ArrayList<Double> divide(ArrayList<Double> a, double b){
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) / b);
        }
        return result;
    }

    public static ArrayList<Double> sqrt(ArrayList<Double> a){
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(Math.sqrt(a.get(i)));
        }
        return result;
    }

    public static ArrayList<Double> maximum(ArrayList<Double> a, ArrayList<Double> b){
        if (a.size() != b.size()) {
            System.out.println("Error: " + a.size() + "/" + b.size());
        }
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) <= b.get(i)) {
                result.add(b.get(i));
            } else {
                result.add(a.get(i));
            }
        }
        return result;
    }

    public static double norm(ArrayList<Double> a){
        double result = 0.0;
        for (int i = 0; i < a.size(); i++) {
            result += Math.pow(Math.abs(a.get(i)), 2.0);
        }
        result = Math.sqrt(result);
        return result;
    }    
}
