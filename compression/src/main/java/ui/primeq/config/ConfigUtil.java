package ui.primeq.config;

import ui.primeq.optimizer.AdamSettings;

public class ConfigUtil {
    public static AdamSettings setAdamSettings(int maxiter, int numLayers, int noOfTimes, int noPrimes, double tol, 
        double lr, double beta1, double beta2, double noiseFactor, double eps, boolean amsgrad) {
            return new AdamSettings(maxiter, tol, lr, beta1, beta2, noiseFactor, eps, amsgrad);
    }

    public static int[] getNumVars(Config config) {
        return config.numVars;
    }

    public static int getMaxIter(Config config) {
        return config.adamSettings.getMaxIter();
    }

    public static void setMaxIter(Config config, int maxiter) {
        config.adamSettings.setMaxIter(maxiter);
    }

    public static int getNumLayers(Config config) {
        return config.numLayers;
    }

    public static void setNumLayer(Config config, int numLayers) {
        config.numLayers = numLayers;
    }

    public static int getNoOfTimes(Config config) {
        return config.noOfTimes;
    }

    public static void setNoOfTimes(Config config, int noOfTimes) {
        config.noOfTimes = noOfTimes;
    }

    public static int getNoPrimes(Config config) {
        return config.noPrimes;
    }

    public static void setNoPrimes(Config config, int noPrimes) {
        config.noPrimes = noPrimes;
    }

    public static double getTol(Config config) {
        return config.adamSettings.getTol();
    }

    public static void setTol(Config config, double tol) {
        config.adamSettings.setTol(tol);
    }

    public static double getLr(Config config) {
        return config.adamSettings.getLr();
    }

    public static void setLr(Config config, double lr) {
        config.adamSettings.setLr(lr);
    }

    public static double getBeta1(Config config) {
        return config.adamSettings.getBeta1();
    }

    public static void setBeta1(Config config, double beta1) {
        config.adamSettings.setBeta1(beta1);
    }

    public static double getBeta2(Config config) {
        return config.adamSettings.getBeta2();
    }

    public static void setBeta2(Config config, double beta2) {
        config.adamSettings.setBeta2(beta2);
    }

    public static double getNoiseFactor(Config config) {
        return config.adamSettings.getNoiseFactor();
    }

    public static void setNoiseFactor(Config config, double noiseFactor) {
        config.adamSettings.setNoiseFactor(noiseFactor);
    }

    public static double getEps(Config config) {
        return config.adamSettings.getEps();
    }

    public static void setEps(Config config, double eps) {
        config.adamSettings.setEps(eps);
    }

    public static boolean isAmsgrad(Config config) {
        return config.adamSettings.isAmsgrad();
    }

    public static void setAmsgrad(Config config, boolean value) {
        config.adamSettings.setAmsgrad(value);
    }

    public static AdamSettings getAdamSettings(Config config) {
        return config.adamSettings;
    }
}
