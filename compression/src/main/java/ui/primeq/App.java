package ui.primeq;

import java.io.IOException;
import java.util.Scanner;

import org.codehaus.plexus.components.interactivity.Prompter;

import ui.primeq.config.Config;
import ui.primeq.optimizer.Adam;
import ui.primeq.optimizer.FunctionManager;
import ui.primeq.storage.Storage;

public class App {
    public static void main( String[] args ) throws IOException {

        System.out.println("1. Compress or 2. Decompress");

        Scanner scanner = new Scanner(System.in);
        String command = scanner.nextLine().strip().toLowerCase();
        while (!command.equals("compress") && !command.equals("decompress")) {
            System.out.println("Please key in correct command.");
            System.out.println("Command Entered: " + command);
            command = scanner.nextLine().strip().toLowerCase();
        }
        scanner.close();

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

        if (command.equals("compress")) {
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
        } else if (command.equals("decompress")) {
            // Decompression of fill.
            fileManager.decompress(nameOfFile + "Compressed.txt");
        }
    }
}
