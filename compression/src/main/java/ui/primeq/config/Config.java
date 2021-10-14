package ui.primeq.config;

import java.util.Optional;

import ui.primeq.optimizer.AdamSettings;

public class Config {
    int maxiter;
    int[] numVars = {5, 9, 14, 20, 27, 35, 44, 54, 65};
    int numLayers;
    int noOfTimes;
    int noPrimes;
    AdamSettings adamSettings;

    public Config(int maxiter, int numLayers, int noOfTimes, int noPrimes, double tol, 
        double lr, double beta1, double beta2, double noiseFactor, double eps, boolean amsgrad) {
            this.maxiter = maxiter;
            this.numLayers = numLayers;
            this.noOfTimes = noOfTimes;
            this.noPrimes = noPrimes;
            Optional<Double> opTol = Optional.of(tol);
            Optional<Double> opLr = Optional.of(lr);
            Optional<Double> opBeta1 = Optional.of(beta1);
            Optional<Double> opBeta2 = Optional.of(beta2);
            Optional<Double> opNoiseFactor = Optional.of(noiseFactor);
            Optional<Double> opEps = Optional.of(eps);
            Optional<Boolean> opAmsgrad = Optional.of(amsgrad);
            this.adamSettings = new AdamSettings(this.maxiter, opTol.orElse(1e-6), opLr.orElse(0.001), opBeta1.orElse(0.9), 
                opBeta2.orElse(0.99), opNoiseFactor.orElse(1e-8), opEps.orElse(1e-10), opAmsgrad.orElse(false));
    }

    public int getMaxIter() {
        return this.maxiter;
    }

    public void setMaxIter(int maxiter) {
        this.maxiter = maxiter;
    }

    public int getNumLayers() {
        return this.numLayers;
    }

    public void setNumLayer(int numLayers) {
        this.numLayers = numLayers;
    }

    public int getNoOfTimes() {
        return this.noOfTimes;
    }

    public void setNoOfTimes(int noOfTimes) {
        this.noOfTimes = noOfTimes;
    }

    public int getNoPrimes() {
        return this.noPrimes;
    }

    public void setNoPrimes(int noPrimes) {
        this.noPrimes = noPrimes;
    }

    public double getTol() {
        return this.adamSettings.getTol();
    }

    public void setTol(double tol) {
        this.adamSettings.setTol(tol);
    }

    public double getLr() {
        return this.adamSettings.getLr();
    }

    public void setLr(double lr) {
        this.adamSettings.setLr(lr);
    }

    public double getBeta1() {
        return this.adamSettings.getBeta1();
    }

    public void setBeta1(double beta1) {
        this.adamSettings.setBeta1(beta1);
    }

    public double getBeta2() {
        return this.adamSettings.getBeta2();
    }

    public void setBeta2(double beta2) {
        this.adamSettings.setBeta2(beta2);
    }

    public double getNoiseFactor() {
        return this.adamSettings.getNoiseFactor();
    }

    public void setNoiseFactor(double noiseFactor) {
        this.adamSettings.setNoiseFactor(noiseFactor);
    }

    public double getEps() {
        return this.adamSettings.getEps();
    }

    public void setEps(double eps) {
        this.adamSettings.setEps(eps);
    }

    public boolean isAmsgrad() {
        return this.adamSettings.isAmsgrad();
    }

    public void setAmsgradTrue() {
        this.adamSettings.setAmsgradTrue();
    }

    public AdamSettings getAdamSettings() {
        return this.adamSettings;
    }
}
