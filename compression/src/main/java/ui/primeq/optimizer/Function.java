package ui.primeq.optimizer;

import java.util.ArrayList;
import java.util.List;

public class Function {
    String equation;


    public Function(String equation) {
        this.equation = equation;
    }

    public List<Double> gradientfunction(List<Double> x) {
        List<Double> result = new ArrayList<>();
        result.addAll(x);
        return result;
    }

    public double objectivefunction(List<Double> x) {
        double result = 0.3;
        return result;
    }
}
