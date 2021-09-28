package ui.primeq.optimizer;

public class AdamSettings extends OptimizerSettings {
    private double tol;
    private double lr;
    private double beta1;
    private double beta2;
    private double noiseFactor;
    private double eps;
    private boolean amsgrad;

    public AdamSettings(int maxiter, double tol, double lr, double beta1, double beta2, double noiseFactor, double eps, boolean amsgrad){
        super(maxiter);
        this.tol = tol;
        this.lr = lr;
        this.beta1 = beta1;
        this.beta2 = beta2;
        this.noiseFactor = noiseFactor;
        this.eps = eps;
        this.amsgrad = amsgrad;
    }

    public double getTol() {
        return this.tol;
    }

    public double getLr() {
        return this.lr;
    }

    public double getBeta1() {
        return this.beta1;
    }

    public double getBeta2() {
        return this.beta2;
    }

    public double getNoiseFactor() {
        return this.noiseFactor;
    }

    public double getEps() {
        return this.eps;
    }

    public boolean isAmsgrad() {
        return this.amsgrad;
    }
}
