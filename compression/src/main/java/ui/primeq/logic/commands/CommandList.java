package ui.primeq.logic.commands;

import java.util.ArrayList;
import java.util.List;

import ui.primeq.logic.Logic;

public class CommandList {
    public static ArrayList<String> commandList = new ArrayList<>(List.of(
        Logic.COMPRESSCOMMAND, Logic.DECOMPRESSCOMMAND, Logic.EXITCOMMAND,
        Logic.CHANGEFILEFORMATCOMMAND, Logic.CHANGEFILEFORMATCOMMAND));

    public static boolean commandFound(String command) {
        return commandList.contains(command);
    }
}

