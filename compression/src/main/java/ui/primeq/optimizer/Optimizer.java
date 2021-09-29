package ui.primeq.optimizer;

import java.util.List;
// import ai.djl.ndarray.NDArray;

public interface Optimizer {
    // Base Class for optimization algorithms

    public List<OptimizerSupportLevel> getSupportLevel();
    // Retrieve support level

    public OptimizerSupportLevel gradientSupportLevel();
    // Retrieve gradient support level

    public boolean isGradientIgnored();
    // Retrieve if gradient ignored

    public boolean isGradientSupported();
    // Retrieve if gradient supported

    public boolean isGradientRequired();
    // Retrieve if gradient required

    public OptimizerSupportLevel boundsSupportLevel();
    // Retrieve bounds support level

    public boolean isBoundsIgnored();
    // Retrieve if bounds ignored

    public boolean isBoundsSupported();
    // Retrieve if bounds supported

    public boolean isBoundsRequired();
    // Retrieve if bounds required

    public OptimizerSupportLevel initialPointSupportLevel();
    // Retrieve initial point support level

    public boolean isInitialPointIgnored();
    // Retrieve if initial point ignored

    public boolean isInitialPointSupported();
    // Retrieve if initial point supported

    public boolean isInitialPointRequired();
    // Retrieve if initial point required

    // public String toString();
    // // Prints Optimizer to String

    // public void setMaxEvalsGrouped(int limit);
    // // Set max evals grouped

    // public double optimize(int numVars, Function objectiveFunction, Function gradientFunction, NDArray initialPoint);
    // // Optimize function to be implemented in optimizers

    // public NDArray gradientNumDiff(NDArray xCenter, Function f, double epsilon, int maxEvalsGrouped);
    // // Compute the gradient with the numeric differentiation in the parallel way, around the point x_center.
}
