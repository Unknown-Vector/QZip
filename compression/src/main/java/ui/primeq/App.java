package ui.primeq;

import java.io.IOException;
import java.util.Scanner;

import ui.primeq.config.Config;
import ui.primeq.logic.Logic;
import ui.primeq.model.Model;
import ui.primeq.storage.Storage;

public class App {
    public static void main( String[] args ) throws IOException {

        // Initialize Config
        Config config = new Config();
        config = config.initConfig();
        
        // Initialize Model
        Model model = new Model(config);

        // Initialize Storage
        Storage storage = new Storage();

        // Initialize Logic
        Logic logic = new Logic(storage, model);
        // Initialize Scanner
        Scanner scanner = new Scanner(System.in);
        
        while (true){
            System.out.println("Enter Command");
            String command = scanner.nextLine().strip().toUpperCase();
            while (!command.equals(Logic.COMPRESSCOMMAND) && !command.equals(Logic.DECOMPRESSCOMMAND)
                && !command.equals(Logic.EXITCOMMAND)) {
                System.out.println("Please key in correct command.");
                System.out.println("Command Entered: " + command);
                command = scanner.nextLine().strip().toUpperCase();
            }
            logic.execute(command);
        }
    }
}
