package ui.primeq.logic.commands;

import java.io.IOException;

import ui.primeq.config.JsonUtil;
import ui.primeq.model.Model;

public class ChangeFileNameCommand {
    public static void execute(Model model, String nameOfFile) throws IOException{
        model.getConfig().setNameOfFile(nameOfFile);
        JsonUtil.serializeObjectToJsonFile(model.getConfig().getSettingsPath(), model.getConfig());
    }
}
