package ui.primeq;

import java.io.IOException;
import java.util.Scanner;

import ui.primeq.config.Config;
import ui.primeq.logic.Logic;
import ui.primeq.model.Model;
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
        
        // Initialize Model
        Model model = new Model(config);

        // Initialize Storage
        Storage storage = new Storage();

        // Initialize Logic
        Logic logic = new Logic(storage, model);

        logic.execute(command);
    }
}
