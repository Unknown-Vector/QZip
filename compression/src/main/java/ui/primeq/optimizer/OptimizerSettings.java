package ui.primeq.optimizer;

public class OptimizerSettings {
    private int maxiter;

    public OptimizerSettings(){}

    public OptimizerSettings(int maxiter){
        this.maxiter = maxiter;
    }

    public int getMaxIter() {
        return maxiter;
    }

    public void setMaxIter(int maxiter) {
        this.maxiter = maxiter;
    }
}
