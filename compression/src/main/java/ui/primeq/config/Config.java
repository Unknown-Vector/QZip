package ui.primeq.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import ui.primeq.optimizer.AdamSettings;

public class Config {

    private Path settingsPath = Paths.get("./config.json");

    private int[] numVars = {5, 9, 14, 20, 27, 35, 44, 54, 65};
    private int numLayers;
    private int noOfTimes;
    private int noPrimes;
    private AdamSettings adamSettings;

    public Config() {}

    public Config(int maxiter, int numLayers, int noOfTimes, int noPrimes, double tol, 
        double lr, double beta1, double beta2, double noiseFactor, double eps, boolean amsgrad) {
            this.numLayers = numLayers;
            this.noOfTimes = noOfTimes;
            this.noPrimes = noPrimes;
            Optional<Integer> opMaxIter = Optional.of(maxiter);
            Optional<Double> opTol = Optional.of(tol);
            Optional<Double> opLr = Optional.of(lr);
            Optional<Double> opBeta1 = Optional.of(beta1);
            Optional<Double> opBeta2 = Optional.of(beta2);
            Optional<Double> opNoiseFactor = Optional.of(noiseFactor);
            Optional<Double> opEps = Optional.of(eps);
            Optional<Boolean> opAmsgrad = Optional.of(amsgrad);
            this.adamSettings = new AdamSettings(opMaxIter.orElse(100), opTol.orElse(1e-6), opLr.orElse(0.001), opBeta1.orElse(0.9), 
                opBeta2.orElse(0.99), opNoiseFactor.orElse(1e-8), opEps.orElse(1e-10), opAmsgrad.orElse(false));
    }

    public Config initConfig() throws IOException{
        return readJson();
    }

    public Config DefaultConfig() {
        return new Config(100, 1, 1, 2, 1e-6, 0.001, 0.9, 0.99, 1e-8, 1e-10, false);
    }

    private Config readJson() throws IOException{
        Config config = DefaultConfig();
        if(!Files.exists(this.settingsPath)) {
            // Create new json with default values
            JsonUtil.serializeObjectToJsonFile(settingsPath, config);
            return this.readJson();
        } else {
            // Deserialize json
            System.out.println("HHHHHH");
            return JsonUtil.deserializeObjectFromJsonFile(settingsPath, config);
        }
    }

    public int[] getNumVars() {
        return this.numVars;
    }

    public int getMaxIter() {
        return this.adamSettings.getMaxIter();
    }

    public void setMaxIter(int maxiter) {
        this.adamSettings.setMaxIter(maxiter);
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

    public void setAmsgrad(boolean value) {
        this.adamSettings.setAmsgrad(value);
    }

    public AdamSettings getAdamSettings() {
        return this.adamSettings;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("numLayers: " + this.numLayers);
        sb.append("noOfTimes: " + this.noOfTimes);
        sb.append("noPrimes: " + this.noPrimes);
        sb.append("adamSettings: " + this.adamSettings.toString());
        return sb.toString();
    }
}
