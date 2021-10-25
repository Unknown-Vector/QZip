package ui.primeq.logic;

import java.io.IOException;

import ui.primeq.model.Model;
import ui.primeq.storage.Storage;

public class Logic {

    private static final String COMPRESSCOMMAND = "compress";
    private static final String DECOMPRESSCOMMAND = "decompress";

    private final Storage storage;
    private final Model model;

    public Logic(Storage storage, Model model){
        this.storage = storage;
        this.model = model;
    }

    public void execute(String command) throws IOException{
        if (command.equals(COMPRESSCOMMAND)) {
            storage.readData(model.getConfig());
            model.getOptimizer().processUniqueValues(model.getConfig(), model.getFunctionManager(), storage);
            model.getFileManager().generateCompressedFile(model.getConfig(), storage);
        } else if (command.equals(DECOMPRESSCOMMAND)) {
            model.getFileManager().decompress(model.getConfig().getNameOfFile() + "Compressed" + model.getConfig().getFileFormat());
        }
    }
}
