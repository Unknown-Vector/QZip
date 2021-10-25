package ui.primeq.logic.commands;

import java.io.IOException;

import ui.primeq.config.JsonUtil;
import ui.primeq.model.Model;

public class ChangeFileFormatCommand {
    public static void execute(Model model, String fileFormat) throws IOException{
        model.getConfig().setFileFormat(fileFormat);
        JsonUtil.serializeObjectToJsonFile(model.getConfig().getSettingsPath(), model.getConfig());
    }
}
