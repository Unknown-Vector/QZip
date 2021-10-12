package ui.primeq;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import ui.primeq.optimizer.ListOperation;

public class ListTest {
    double[] a;
    double[] b;
    double c;

    @BeforeEach
    void setUp() {
        double[] testSample = {1.0, 2.0, 3.0};
        a = testSample.clone();
        b = testSample.clone();
        c = 0.5;  
    }

    @Test
    void testElementAddition() {
        double[] elementAddition = {2.0, 4.0, 6.0};
        assertEquals(elementAddition, ListOperation.add(this.a, this.b));
    }

    @Test
    void testAddition() {
        double[] addition = {1.5, 2.5, 3.5};
        assertEquals(addition, ListOperation.add(this.a, this.c));
    }

    @Test
    void testElementMinus() {
        double[] elementMinus = {0.0, 0.0, 0.0};
        assertEquals(elementMinus, ListOperation.minus(this.a, this.b));
    }

    @Test
    void testMinus() {
        double[] minus = {0.5, 1.5, 2.5};
        assertEquals(minus, ListOperation.minus(this.a, this.c));
    }

    @Test
    void testElementMultiply() {
        double[] elementMultiply = {1.0, 4.0, 9.0};
        assertEquals(elementMultiply, ListOperation.mul(this.a, this.b));
    }

    @Test
    void testMultiply() {
        double[] multiply = {0.5, 1.0, 1.5};
        assertEquals(multiply, ListOperation.mul(this.a, this.c));
    }

    @Test
    void testElementDivide() {
        double[] elementDivide = {1.0, 1.0, 1.0};
        assertEquals(elementDivide, ListOperation.divide(this.a, this.b));
    }

    @Test
    void testDivide() {
        double[] divide = {2.0, 4.0, 6.0};
        assertEquals(divide, ListOperation.divide(this.a, this.c));
    }
}
