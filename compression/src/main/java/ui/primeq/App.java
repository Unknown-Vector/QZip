package ui.primeq;

import java.io.IOException;

import ui.primeq.config.Config;
import ui.primeq.optimizer.Adam;
import ui.primeq.optimizer.FunctionManager;
import ui.primeq.storage.Storage;

public class App {
    public static void main( String[] args ) throws IOException {

        // Initialize Config
        Config config = new Config();
        config = config.initConfig();

        // Initialize constants
        String nameOfFile = "sample";
        String fileFormat = ".txt";
        
        // Initialize Managers
        FileManager fileManager =  new FileManager();
        FunctionManager functionManager = new FunctionManager(config);

        // Initialize Storage
        Storage storage = new Storage();

        // Initialize Optimizer
        Adam opt = new Adam(config);

        // Convert File into int array to be processed
        storage.readData(nameOfFile, fileFormat);

        // Process Unique Values with Optimizer
        opt.processUniqueValues(config, functionManager, storage);

        System.out.println("filename: " + storage.getFileName());
        for (int remainder : storage.getRemainders()) {
            System.out.println("remainder: " + remainder);
        }

        for (int key : storage.getUniqueMap().keySet()) {
            System.out.println(key + ": " + storage.getUniqueMap().get(key));
        }

        // Generate Compressed file with unique map from Optimizer.
        fileManager.generateCompressedFile(config, storage);

        // Decompression of fill.
        fileManager.decompress(nameOfFile + "Compressed.txt");
    }
}
