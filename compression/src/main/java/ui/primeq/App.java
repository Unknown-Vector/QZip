package ui.primeq;

import java.io.IOException;
import java.util.Scanner;

import ui.primeq.config.Config;
import ui.primeq.logic.Logic;
import ui.primeq.logic.commands.CommandList;
import ui.primeq.model.Model;
import ui.primeq.storage.Storage;

public class App {
    public static void main( String[] args ) throws IOException {

        // Initialize Config
        Config config = new Config();
        config = config.initConfig();
<<<<<<< HEAD

        // Initialize constants
        String nameOfFile = "GCF_009858895.2_ASM985889v3_genomic";
=======
>>>>>>> SamYJ2606-main
        
        // Initialize Model
        Model model = new Model(config);

<<<<<<< HEAD
        // Convert File into int array to be processed
        int[] data = fileManager.readFile("../" + nameOfFile + ".gff");
=======
        // Initialize Storage
        Storage storage = new Storage();
>>>>>>> SamYJ2606-main

        // Initialize Logic
        Logic logic = new Logic(storage, model);
        // Initialize Scanner
        Scanner scanner = new Scanner(System.in);
        
        while (true){
            System.out.println("Enter Command");
            String commandString = scanner.nextLine();
            String[] command = commandString.split(" ");
            String commandWord = command[0].strip().toUpperCase();
            while (!CommandList.commandFound(commandWord)) {
                System.out.println("Please key in correct command.");
                System.out.println("Command Entered: " + commandWord);
                commandString = scanner.nextLine();
                command = commandString.split(" ");
                commandWord = command[0].strip().toUpperCase();
            }
            if (command.length == 1) {
                logic.execute(command[0].toUpperCase().strip(), " ");
            } else {
                logic.execute(command[0].toUpperCase().strip(), command[1].strip());
            }
        }
    }
}
