package ui.primeq.logic.commands;

import java.io.IOException;

import ui.primeq.model.Model;

public class DecompressCommand {
    public static void execute(Model model) throws IOException{
        model.getFileManager().decompress(model.getConfig().getNameOfFile() + "Compressed" + model.getConfig().getFileFormat());
    }
}
