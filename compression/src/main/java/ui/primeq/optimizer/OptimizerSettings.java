package ui.primeq.optimizer;

public class OptimizerSettings {
    private int maxiter;

    public OptimizerSettings(int maxiter){
        this.maxiter = maxiter;
    }

    public int getMaxIter() {
        return maxiter;
    }
}
