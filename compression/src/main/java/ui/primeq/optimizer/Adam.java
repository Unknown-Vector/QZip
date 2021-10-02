package ui.primeq.optimizer;

import java.util.ArrayList;
import java.util.Optional;
import java.io.IOException;

public class Adam implements Optimizer {
    AdamSettings adamSettings;
    OptimizerSupportLevel gradientSupportLevel;
    OptimizerSupportLevel boundsSupportLevel;
    OptimizerSupportLevel initialpointSupportLevel;
    double t;
    ArrayList<Double> m;
    ArrayList<Double> v;
    ArrayList<Double> mTemp;
    ArrayList<Double> vTemp;
    // final int maxsize = 100; 


    public Adam(int maxiter, Optional<Double> tol, Optional<Double> lr, Optional<Double> beta1, Optional<Double> beta2, Optional<Double> noiseFactor, Optional<Double> eps, Optional<Boolean> amsgrad){
        this.adamSettings = new AdamSettings(maxiter, tol.orElse(1e-6), lr.orElse(0.001), beta1.orElse(0.9), beta2.orElse(0.99), noiseFactor.orElse(1e-08), eps.orElse(1e-10), amsgrad.orElse(false));
        this.gradientSupportLevel = OptimizerSupportLevel.SUPPORTED;
        this.boundsSupportLevel = OptimizerSupportLevel.IGNORED;
        this.initialpointSupportLevel = OptimizerSupportLevel.SUPPORTED;
        t = 0;
        m = new ArrayList<>();
        v = new ArrayList<>();
        mTemp = new ArrayList<>();
        vTemp = new ArrayList<>();

        // double[] zeros = new double[maxsize];
        // for(int i = 0; i < zeros.length; i++){
        //     zeros[i] = 0.0;
        // }
    }

    public ArrayList<OptimizerSupportLevel> getSupportLevel() {
        ArrayList<OptimizerSupportLevel> dictionary = new ArrayList<>();
        dictionary.add(gradientSupportLevel);
        dictionary.add(boundsSupportLevel);
        dictionary.add(initialpointSupportLevel);
        return dictionary;
    }

    public ArrayList<Double> minimize(FunctionManager functionManager, ArrayList<Double> initialPoint) throws IOException {
        // System.out.println(initialPoint);
        // System.out.println(paramList);
        ArrayList<Double> derivative = functionManager.gradientfunction(initialPoint);
        // double[] dx = derivative.stream().mapToDouble(Double::doubleValue).toArray();
        double t = 0;
        double beta1 = this.adamSettings.getBeta1();
        double beta2 = this.adamSettings.getBeta2();
        double lr = this.adamSettings.getLr();
        double noise = this.adamSettings.getNoiseFactor();
        // System.out.println(m);
        // System.out.println(v);
        // System.out.println(mTemp);
        // System.out.println(vTemp);
        m.clear();
        v.clear();
        mTemp.clear();
        vTemp.clear();

        while ((m.size() < initialPoint.size()) && (v.size() < initialPoint.size())) {
            m.add(0.00);
            v.add(0.00);
            mTemp.add(0.00);
            vTemp.add(0.00);
        }

        ArrayList<Double> params = new ArrayList<>();
        params.addAll(initialPoint);
        
        ArrayList<Double> paramsNew = new ArrayList<>();
        paramsNew.addAll(initialPoint);

        ArrayList<Double> vEff = new ArrayList<>();
        

        while (t < this.adamSettings.getMaxIter()) {
            // System.out.println(t + ":params: " + params);
            System.out.print(". ");
            mTemp.clear();
            vTemp.clear();
            if (t > 0) {
                derivative = functionManager.gradientfunction(params);
                // System.out.println("Gradients = " + derivative);
                // System.out.println("Params = " + params);
            } 

            t += 1.0;
            double temp1 = 1.000 - beta1;
            ArrayList<Double> tempBeta1 = ListOperation.mul(derivative, temp1);
            m = ListOperation.mul(m, beta1);
            m = ListOperation.add(m, tempBeta1);
            // System.out.println(t + ":m: " + m);

            double temp2 = 1.000 - beta2;
            ArrayList<Double> tempBeta2 = ListOperation.mul(derivative, derivative);
            tempBeta2 = ListOperation.mul(tempBeta2, temp2);
            v = ListOperation.mul(v, beta2);
            v = ListOperation.add(v, tempBeta2);

            // System.out.println(t + ":v: " + v);

            double lrEff = lr * Math.sqrt(1.000 - Math.pow(beta2, t)) / (1 - Math.pow(beta1, t));
            // System.out.println(t + ":lrEff: " + lrEff);

            mTemp.addAll(m);
            vTemp.addAll(v);
            // System.out.println(vTemp);

            if (this.adamSettings.isAmsgrad() == false) {
                vTemp = ListOperation.sqrt(vTemp);
                vTemp = ListOperation.add(vTemp, noise);
                // System.out.println(t + ":v2: " + vTemp);
                mTemp = ListOperation.mul(mTemp, lrEff);
                mTemp = ListOperation.divide(mTemp, vTemp);
                // System.out.println(t + ":m2: " + mTemp);
                paramsNew = ListOperation.minus(params, mTemp);
                // System.out.println(t + ":paramsNew" + paramsNew);
            } else {
                vEff = ListOperation.maximum(vEff, vTemp);
                vTemp = ListOperation.sqrt(vEff);
                vTemp = ListOperation.add(vTemp, noise);
                mTemp = ListOperation.mul(mTemp, lrEff);
                mTemp = ListOperation.divide(mTemp, vTemp);
                paramsNew = ListOperation.minus(params, mTemp);
            }
            if (ListOperation.norm(ListOperation.minus(params, paramsNew)) < this.adamSettings.getTol()) {
                ArrayList<Double> result = paramsNew;
                return result;
            } else {
                params.clear();
                params.addAll(paramsNew);
                // System.out.println(t + ":params2" + paramsNew);
            }
        }
        ArrayList<Double> result = paramsNew;
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
