package ui.primeq.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ui.primeq.optimizer.AdamSettings;

public class Config {

    public static final int MAXREMAINDERS = 15;
    public static final int MAXPRIMES = 10;
    public static final int MAXREMAINDER = 15;
    public static final int[] primes = {2,3,5,7,11,13,17,19,23,29,31};

    private Path settingsPath = Paths.get("./config.json");

    protected int[] numVars = {5, 9, 14, 20, 27, 35, 44, 54, 65};
    protected int numLayers;
    protected int noOfTimes;
    protected int noPrimes;
    protected AdamSettings adamSettings;

    public Config() {}

    public Config(int maxiter, int numLayers, int noOfTimes, int noPrimes, double tol, 
        double lr, double beta1, double beta2, double noiseFactor, double eps, boolean amsgrad) {
            this.numLayers = numLayers;
            this.noOfTimes = noOfTimes;
            this.noPrimes = noPrimes;
            setAdamSettings(maxiter, numLayers, noOfTimes, noPrimes, tol, lr, beta1, beta2, noiseFactor, eps, amsgrad);
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
            return JsonUtil.deserializeObjectFromJsonFile(settingsPath, config);
        }
    }

    public void setAdamSettings(int maxiter, int numLayers, int noOfTimes, int noPrimes, double tol, 
        double lr, double beta1, double beta2, double noiseFactor, double eps, boolean amsgrad) {
            this.adamSettings = new AdamSettings(maxiter, tol, lr, beta1, beta2, noiseFactor, eps, amsgrad);
    }

    public int[] getNumVars() {
        return this.numVars;
    }

    @JsonIgnore
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

    @JsonIgnore
    public double getTol() {
        return this.adamSettings.getTol();
    }
    
    public void setTol(double tol) {
        this.adamSettings.setTol(tol);
    }

    @JsonIgnore
    public double getLr() {
        return this.adamSettings.getLr();
    }
    
    public void setLr(double lr) {
        this.adamSettings.setLr(lr);
    }

    @JsonIgnore
    public double getBeta1() {
        return this.adamSettings.getBeta1();
    }

    public void setBeta1(double beta1) {
        this.adamSettings.setBeta1(beta1);
    }

    @JsonIgnore
    public double getBeta2() {
        return this.adamSettings.getBeta2();
    }

    public void setBeta2(double beta2) {
        this.adamSettings.setBeta2(beta2);
    }

    @JsonIgnore
    public double getNoiseFactor() {
        return this.adamSettings.getNoiseFactor();
    }

    public void setNoiseFactor(double noiseFactor) {
        this.adamSettings.setNoiseFactor(noiseFactor);
    }

    @JsonIgnore
    public double getEps() {
        return this.adamSettings.getEps();
    }

    public void setEps(double eps) {
        this.adamSettings.setEps(eps);
    }

    @JsonIgnore
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
        sb.append("numVars: " + this.numVars);
        sb.append("numLayers: " + this.numLayers);
        sb.append("noOfTimes: " + this.noOfTimes);
        sb.append("noPrimes: " + this.noPrimes);
        sb.append("adamSettings: " + this.adamSettings.toString());
        return sb.toString();
    }
}
