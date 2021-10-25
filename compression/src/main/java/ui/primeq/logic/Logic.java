package ui.primeq.logic;

import java.io.IOException;

import ui.primeq.logic.commands.CompressCommand;
import ui.primeq.logic.commands.DecompressCommand;
import ui.primeq.logic.commands.ExitCommand;
import ui.primeq.model.Model;
import ui.primeq.storage.Storage;

public class Logic {

    public static final String COMPRESSCOMMAND = "COMPRESS";
    public static final String DECOMPRESSCOMMAND = "DECOMPRESS";
    public static final String EXITCOMMAND = "EXIT";

    private final Storage storage;
    private final Model model;

    public Logic(Storage storage, Model model){
        this.storage = storage;
        this.model = model;
    }

    public void execute(String command) throws IOException{
        if (command.equals(COMPRESSCOMMAND)) {
            CompressCommand.execute(this.model, this.storage);
        } else if (command.equals(DECOMPRESSCOMMAND)) {
            DecompressCommand.execute(this.model);
        } else if (command.equals(EXITCOMMAND)) {
            ExitCommand.execute();
        }
    }
}
