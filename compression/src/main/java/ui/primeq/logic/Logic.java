package ui.primeq.logic;

import java.io.IOException;

import ui.primeq.logic.commands.ChangeFileFormatCommand;
import ui.primeq.logic.commands.ChangeFileNameCommand;
import ui.primeq.logic.commands.CompressCommand;
import ui.primeq.logic.commands.DecompressCommand;
import ui.primeq.logic.commands.ExitCommand;
import ui.primeq.model.Model;
import ui.primeq.storage.Storage;

public class Logic {

    public static final String COMPRESSCOMMAND = "COMPRESS";
    public static final String DECOMPRESSCOMMAND = "DECOMPRESS";
    public static final String EXITCOMMAND = "EXIT";
    public static final String CHANGEFILENAMECOMMAND = "CHANGEFILENAME";
    public static final String CHANGEFILEFORMATCOMMAND = "CHANGEFILEFORMAT";

    private final Storage storage;
    private final Model model;

    public Logic(Storage storage, Model model){
        this.storage = storage;
        this.model = model;
    }

    public void execute(String command, String args) throws IOException{
        if (command.equals(COMPRESSCOMMAND)) {
            CompressCommand.execute(this.model, this.storage);
        } else if (command.equals(DECOMPRESSCOMMAND)) {
            DecompressCommand.execute(this.model);
        } else if (command.equals(EXITCOMMAND)) {
            ExitCommand.execute();
        } else if (command.equals(CHANGEFILENAMECOMMAND)) {
            ChangeFileNameCommand.execute(this.model, args);
        } else if (command.equals(CHANGEFILEFORMATCOMMAND)) {
            ChangeFileFormatCommand.execute(this.model, args);
        }
    }
}
