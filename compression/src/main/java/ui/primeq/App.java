package ui.primeq;

import java.io.IOException;
// import java.util.ArrayList;
// import java.util.HashMap;
// import java.util.ArrayList;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
// import ui.primeq.optimizer.Adam;
// import ui.primeq.optimizer.Optimizer;

import org.javatuples.Triplet;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        System.out.println("Hello There!");
        quantum_circuit_runner runner = new quantum_circuit_runner();
        ArrayList<Double> list = new ArrayList<Double>();
        list.add(5.0);
        list.add(4.0);
        list.add(1.0);

        list.add(5.0);
        list.add(5.0);
        list.add(5.0);
        list.add(5.0);
        list.add(5.0);
        list.add(5.0);
        list.add(5.0);
        list.add(5.0);
        list.add(5.0);
        list.add(5.0);
        list.add(5.0);
        list.add(5.0);
        list.add(5.0);
        list.add(5.0);

        Triplet<List<Double>, Double, Double> Res = new Triplet<List<Double>, Double, Double>(new ArrayList<Double>(), 0.1, 0.2);
        System.out.println(Res);
        
        Map<String, Integer> m = runner.run(list);
        System.out.println(m);
        System.out.println(runner.get_gradients());
    }
}
