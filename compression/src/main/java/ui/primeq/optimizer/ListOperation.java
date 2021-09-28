package ui.primeq.optimizer;

import java.util.ArrayList;
import java.util.List;

public class ListOperation extends ArrayList<Double>{

    public static List<Double> add(List<Double> a, List<Double> b){
        if (a.size() != b.size()) {
            System.out.println("Error: " + a.size() + "/" + b.size());
        }
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) + b.get(i));
        }
        return result;
    }

    public static List<Double> add(List<Double> a, double b){
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) + b);
        }
        return result;
    }

    public static List<Double> minus(List<Double> a, List<Double> b){
        if (a.size() != b.size()) {
            System.out.println("Error: " + a.size() + "/" + b.size());
        }
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) - b.get(i));
        }
        return result;
    }

    public static List<Double> minus(List<Double> a, double b){
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) - b);
        }
        return result;
    }

    public static List<Double> mul(List<Double> a, List<Double> b){
        if (a.size() != b.size()) {
            System.out.println("Error: " + a.size() + "/" + b.size());
        }
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) * b.get(i));
        }
        return result;
    }

    public static List<Double> mul(List<Double> a, double b){
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) * b);
        }
        return result;
    }

    public static List<Double> divide(List<Double> a, List<Double> b){
        if (a.size() != b.size()) {
            System.out.println("Error: " + a.size() + "/" + b.size());
        }
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) / b.get(i));
        }
        return result;
    }

    public static List<Double> divide(List<Double> a, double b){
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(a.get(i) / b);
        }
        return result;
    }

    public static List<Double> sqrt(List<Double> a){
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            result.add(Math.sqrt(a.get(i)));
        }
        return result;
    }

    public static List<Double> maximum(List<Double> a, List<Double> b){
        if (a.size() != b.size()) {
            System.out.println("Error: " + a.size() + "/" + b.size());
        }
        List<Double> result = new ArrayList<>();
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i) <= b.get(i)) {
                result.add(b.get(i));
            } else {
                result.add(a.get(i));
            }
        }
        return result;
    }

    public static double norm(List<Double> a){
        double result = 0.0;
        for (int i = 0; i < a.size(); i++) {
            result += Math.pow(Math.abs(a.get(i)), 2.0);
        }
        result = Math.sqrt(result);
        return result;
    }    
}
