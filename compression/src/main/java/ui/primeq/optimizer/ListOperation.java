package ui.primeq.optimizer;

import org.apache.commons.math3.util.MathArrays;

import java.util.ArrayList;
import java.util.stream.DoubleStream;
import java.util.stream.Collectors;

public class ListOperation {

    public static double[] add(double[] a, double[] b){
        if (a.length != b.length) {
            System.out.println("Error: " + a.length + "/" + b.length);
        }

        // double[] aArray = new double[a.length];
        // double[] bArray = new double[b.length];

        // aArray = a.stream().mapToDouble(Double::doubleValue).toArray();
        // bArray = b.stream().mapToDouble(Double::doubleValue).toArray();
        long startTime = System.nanoTime();
        double[] r = MathArrays.ebeAdd(a, b);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("add : " + duration);
        
        // double[] result = new ArrayList<>();
        // result = DoubleStream.of(result).boxed().collect(Collectors.toCollection(ArrayList::new));
        return r;
    }

    public static double[] add(double[] a, double b){
        double[] result = new double[a.length];

        long startTime = System.nanoTime();
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] + b;
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("add* : " + duration);
        return result;
    }

    public static double[] minus(double[] a, double[] b){
        if (a.length != b.length) {
            System.out.println("Error: " + a.length + "/" + b.length);
        }

        // double[] aArray = new double[a.length];
        // double[] bArray = new double[b.length];

        // aArray = a.stream().mapToDouble(Double::doubleValue).toArray();
        // bArray = b.stream().mapToDouble(Double::doubleValue).toArray();
    
        long startTime = System.nanoTime();
        double[] result = MathArrays.ebeSubtract(a, b);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("minus : " + duration);
        // double[] result = new double[]();
        // result = DoubleStream.of(result).boxed().collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    public static double[] minus(double[] a, double b){
        double[] result = new double[a.length];

        long startTime = System.nanoTime();
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] - b;
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("minus* : " + duration);
        return result;
    }

    public static double[] mul(double[] a, double[] b){
        if (a.length != b.length) {
            System.out.println("Error: " + a.length + "/" + b.length);
        }
        // double[] aArray = new double[a.length];
        // double[] bArray = new double[b.length];

        // aArray = a.stream().mapToDouble(Double::doubleValue).toArray();
        // bArray = b.stream().mapToDouble(Double::doubleValue).toArray();
    
        long startTime = System.nanoTime();
        double[] result = MathArrays.ebeMultiply(a, b);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("mul : " + duration);
        // double[] result = new ArrayList<>();
        // result = DoubleStream.of(result).boxed().collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    public static double[] mul(double[] a, double b){
        double[] result = new double[a.length];

        long startTime = System.nanoTime();
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] * b;
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("mul* : " + duration);
        return result;
    }

    public static double[] divide(double[] a, double[] b){
        if (a.length != b.length) {
            System.out.println("Error: " + a.length + "/" + b.length);
        }
        // double[] aArray = new double[a.length];
        // double[] bArray = new double[b.length];

        // aArray = a.stream().mapToDouble(Double::doubleValue).toArray();
        // bArray = b.stream().mapToDouble(Double::doubleValue).toArray();
        
        long startTime = System.nanoTime();
        double[] result = MathArrays.ebeDivide(a, b);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("divide : " + duration);

        // result = DoubleStream.of(result).boxed().collect(Collectors.toCollection(ArrayList::new));
        return result;
    }

    public static double[] divide(double[] a, double b){
        double[] result = new double[a.length];

        long startTime = System.nanoTime();
        for (int i = 0; i < a.length; i++) {
            result[i] = a[i] / b;
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("divide* : " + duration);
        return result;
    }

    public static double[] sqrt(double[] a){
        double[] result = new double[a.length];

        long startTime = System.nanoTime();
        for (int i = 0; i < a.length; i++) {
            result[i] = Math.sqrt(a[i]);
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("sqrt : " + duration);
        return result;
    }

    public static double[] maximum(double[] a, double[] b){
        double[] result = new double[a.length];

        long startTime = System.nanoTime();
        for (int i = 0; i < a.length; i++) {
            if (a[i] <= b[i]) {
                result[i] = b[i];
            } else {
                result[i] = a[i];
            }
        }
        long endTime = System.nanoTime();

        long duration = (endTime - startTime);
        System.out.println("max : " + duration);
        return result;
    }

    public static double norm(double[] a){
        double result = 0.0;
        long startTime = System.nanoTime();
        for (int i = 0; i < a.length; i++) {
            result += Math.pow(Math.abs(a[i]), 2.0);
        }
        result = Math.sqrt(result);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("norm : " + duration);
        return result;
    
    }    
}
