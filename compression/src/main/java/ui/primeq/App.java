package ui.primeq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ui.primeq.config.Config;
import ui.primeq.optimizer.Adam;
import ui.primeq.optimizer.FunctionManager;

public class App {
    public static void main( String[] args ) throws IOException {

        // Initialize Config
        Config config = new Config();
        config = config.initConfig();

        // Initialize constants
        String nameOfFile = "sample";
        
        // Initialize Managers
        FileManager fileManager =  new FileManager();
        FunctionManager functionManager = new FunctionManager(config);

        // Initialize Optimizer
        Adam opt = new Adam(config);

        // Initialize ArrayList & HashMap to contain compressed integers and remainder list
        ArrayList<Integer> remainders = new ArrayList<>();
        HashMap<Integer, String> unique_map =  new HashMap<>();

        // Convert File into int array to be processed
        int[] data = fileManager.readFile("./" + nameOfFile + ".txt");

        // Process Unique Values with Optimizer
        unique_map = opt.processUniqueValues(config, data, functionManager, remainders);

        // Generate Compressed file with unique map from Optimizer
        fileManager.generateCompressedFile(config, nameOfFile, unique_map, data, remainders);
    }
}
