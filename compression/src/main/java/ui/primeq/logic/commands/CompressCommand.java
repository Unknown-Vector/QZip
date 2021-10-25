package ui.primeq.logic.commands;

import java.io.IOException;

import ui.primeq.model.Model;
import ui.primeq.storage.Storage;

public class CompressCommand {
    public static void execute(Model model, Storage storage) throws IOException{
        storage.readData(model.getConfig());
        model.getOptimizer().processUniqueValues(model.getConfig(), model.getFunctionManager(), storage);
        model.getFileManager().generateCompressedFile(model.getConfig(), storage);
    }
}
