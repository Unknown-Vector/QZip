package ui.primeq;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import ui.primeq.optimizer.Adam;
import ui.primeq.optimizer.FunctionManager;
import ui.primeq.optimizer.OptimizerSupportLevel;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;


public class AdamTest {
    Adam adam;
    int maxiter = 100;
    int noPrimes = 2;
    Optional<Double> tol = Optional.of(1e-8);
    Optional<Double> lr = Optional.of(0.1);
    Optional<Double> beta1 = Optional.of(0.9);
    Optional<Double> beta2 = Optional.of(0.99);
    Optional<Double> noiseFactor = Optional.of(1e-10);
    Optional<Double> eps = Optional.of(1e-8);
    Optional<Boolean> amsgrad = Optional.of(false);
    OptimizerSupportLevel gradientSupportLevel = OptimizerSupportLevel.SUPPORTED;
    OptimizerSupportLevel boundsSupportLevel = OptimizerSupportLevel.IGNORED;
    OptimizerSupportLevel initialpointSupportLevel = OptimizerSupportLevel.SUPPORTED;

    @BeforeEach
    void setUp() {
        adam = new Adam(maxiter, tol, lr, beta1, beta2, noiseFactor, eps, amsgrad);
    }

    @Test
    void testGetSupportLevel() {
        ArrayList<OptimizerSupportLevel> supportLevel = new ArrayList();
        supportLevel.add(gradientSupportLevel);
        supportLevel.add(boundsSupportLevel);
        supportLevel.add(initialpointSupportLevel);
        
        assertEquals(supportLevel, adam.getSupportLevel());
    }

    @Test
    void testMinimize() throws IOException{
        ArrayList<Double> answer = new ArrayList<>();
        // Key in answer here.
        FunctionManager functionManager = new FunctionManager(noPrimes);
        ArrayList<Double> initialPoint = new ArrayList<>();
        // Initialize initialPoint here.
        assertEquals(answer, adam.minimize(functionManager, initialPoint));
    }

    @Test 
    void testGradientSupportLevel() {
        assertEquals(gradientSupportLevel, adam.gradientSupportLevel());
    }

    @Test
    void testIsGradientIgnored() {
        assertEquals(false, adam.isGradientIgnored());
    }

    @Test
    void testIsGradientSupported() {
        assertEquals(true, adam.isGradientSupported());
    }

    @Test
    void testIsGradientRequired() {
        assertEquals(false, adam.isGradientRequired());
    }

    @Test
    void testBoundsSupportLevel() {
        assertEquals(boundsSupportLevel, adam.boundsSupportLevel());
    }

    @Test
    void testIsBoundsIgnored() {
        assertEquals(true, adam.isBoundsIgnored());
    }

    @Test
    void testIsBoundsSupported() {
        assertEquals(false, adam.isBoundsSupported());
    }

    @Test
    void testIsBoundsRequired() {
        assertEquals(false, adam.isBoundsRequired());
    }

    @Test
    void testInitialPointSupportLevel() {
        assertEquals(initialpointSupportLevel, adam.initialPointSupportLevel());
    }

    @Test
    void testIsInitialPointIgnored() {
        assertEquals(false, adam.isInitialPointIgnored());
    }

    @Test
    void testisInitialPointSupport() {
        assertEquals(true, adam.isInitialPointSupported());
    }

    @Test
    void testIsInitiailPointRequired() {
        assertEquals(false, adam.isInitialPointRequired());
    }

}
