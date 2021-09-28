package ui.primeq.optimizer;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.javatuples.Triplet;

public class Adam implements Optimizer {
    AdamSettings adamSettings;
    OptimizerSupportLevel gradientSupportLevel;
    OptimizerSupportLevel boundsSupportLevel;
    OptimizerSupportLevel initialpointSupportLevel;
    double t;
    List<Double> m;
    List<Double> v;


    public Adam(int maxiter, Optional<Double> tol, Optional<Double> lr, Optional<Double> beta1, Optional<Double> beta2, Optional<Double> noiseFactor, Optional<Double> eps, Optional<Boolean> amsgrad){
        this.adamSettings = new AdamSettings(maxiter, tol.orElse(1e-6), lr.orElse(0.001), beta1.orElse(0.9), beta2.orElse(0.99), noiseFactor.orElse(1e-08), eps.orElse(1e-10), amsgrad.orElse(false));
        this.gradientSupportLevel = OptimizerSupportLevel.SUPPORTED;
        this.boundsSupportLevel = OptimizerSupportLevel.IGNORED;
        this.initialpointSupportLevel = OptimizerSupportLevel.SUPPORTED;
        t = 0;
        m = new ArrayList<>();
        v = new ArrayList<>();
    }

    public List<OptimizerSupportLevel> getSupportLevel() {
        List<OptimizerSupportLevel> dictionary = new ArrayList<>();
        dictionary.add(gradientSupportLevel);
        dictionary.add(boundsSupportLevel);
        dictionary.add(initialpointSupportLevel);
        return dictionary;
    }

    public Triplet<List<Double>,Double,Double> minimize(Function function, ArrayList<Double> initialPoint) {

        List<Double> derivative = function.gradientfunction(initialPoint);
        double t = 0;
        double beta1 = this.adamSettings.getBeta1();
        double beta2 = this.adamSettings.getBeta2();
        double lr = this.adamSettings.getLr();
        double noise = this.adamSettings.getNoiseFactor();

        while ((m.size() < initialPoint.size()) && (v.size() < initialPoint.size())) {
            m.add(0.00);
            v.add(0.00);
        }

        List<Double> params = new ArrayList<>();
        params.addAll(initialPoint);
        
        List<Double> paramsNew = new ArrayList<>();
        paramsNew.addAll(initialPoint);

        List<Double> vEff = new ArrayList<>();
        

        while (t < this.adamSettings.getMaxIter()) {
            if (t > 0) {
                derivative = function.gradientfunction(params);
            } 

            t += 1.0;
            double temp = 1.000 - beta1;
            List<Double> tempBeta1 = ListOperation.mul(derivative, temp);
            m = ListOperation.mul(m, beta1);
            m = ListOperation.add(m, tempBeta1);

            temp = 1.000 - beta2;
            List<Double> tempBeta2 = ListOperation.mul(derivative, temp);
            tempBeta2 = ListOperation.mul(tempBeta2, derivative);
            v = ListOperation.mul(v, beta2);
            v = ListOperation.add(v, tempBeta2);

            double lrEff = lr * Math.sqrt(1.000 - Math.pow(beta2, t) / (1 - Math.pow(beta1, t)));

            if (this.adamSettings.isAmsgrad() == false) {
                v = ListOperation.sqrt(v);
                v = ListOperation.add(v, noise);
                m = ListOperation.mul(m, lrEff);
                m = ListOperation.divide(m, v);
                paramsNew = ListOperation.minus(params, m);
            } else {
                vEff = ListOperation.maximum(vEff, v);
                v = ListOperation.sqrt(vEff);
                v = ListOperation.add(v, noise);
                m = ListOperation.mul(m, lrEff);
                m = ListOperation.divide(m, v);
                paramsNew = ListOperation.minus(params, m);
            }
            if (ListOperation.norm(ListOperation.minus(params, paramsNew)) < this.adamSettings.getTol()) {
                Triplet<List<Double>,Double,Double> result = new Triplet<List<Double>,Double,Double>(paramsNew, function.objectivefunction(paramsNew), t);
                return result;
            } else {
                params.clear();
                params.addAll(paramsNew);
            }
        }
        Triplet<List<Double>,Double,Double> result = new Triplet<List<Double>,Double,Double>(paramsNew, function.objectivefunction(paramsNew), t);
        return result;
    }

    public OptimizerSupportLevel gradientSupportLevel() {
        return this.gradientSupportLevel;
    }

    public boolean isGradientIgnored() {
        return this.gradientSupportLevel.equals(OptimizerSupportLevel.IGNORED);
    }

    public boolean isGradientSupported() {
        return this.gradientSupportLevel.equals(OptimizerSupportLevel.SUPPORTED);
    }

    public boolean isGradientRequired() {
        return this.gradientSupportLevel.equals(OptimizerSupportLevel.REQUIRED);
    }

    public OptimizerSupportLevel boundsSupportLevel() {
        return this.boundsSupportLevel;
    }

    public boolean isBoundsIgnored() {
        return this.boundsSupportLevel.equals(OptimizerSupportLevel.IGNORED);
    }

    public boolean isBoundsSupported() {
        return this.boundsSupportLevel.equals(OptimizerSupportLevel.SUPPORTED);
    }

    public boolean isBoundsRequired() {
        return this.boundsSupportLevel.equals(OptimizerSupportLevel.REQUIRED);
    }
    public OptimizerSupportLevel initialPointSupportLevel() {
        return this.initialpointSupportLevel;
    }

    public boolean isInitialPointIgnored() {
        return this.initialpointSupportLevel.equals(OptimizerSupportLevel.IGNORED);
    }

    public boolean isInitialPointSupported() {
        return this.initialpointSupportLevel.equals(OptimizerSupportLevel.SUPPORTED);
    }

    public boolean isInitialPointRequired() {
        return this.initialpointSupportLevel.equals(OptimizerSupportLevel.REQUIRED);
    }
}
