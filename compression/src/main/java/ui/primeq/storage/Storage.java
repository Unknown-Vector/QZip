package ui.primeq.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ui.primeq.FileManager;
import ui.primeq.config.Config;

public class Storage {

    private int[] data;
    private HashMap<Integer, String> uniqueMap;
    private ArrayList<Integer> remainders;
    
    public Storage() throws IOException{
        this.data = new int[10];
        this.uniqueMap = new HashMap<>();
        this.remainders = new ArrayList<>();
    }

    public void readData(Config config) throws IOException{
        String nameOfFile = config.getNameOfFile();
        String fileFormat = config.getFileFormat();
        FileManager fileManager = new FileManager();
        this.data = fileManager.readFile("./" + nameOfFile + fileFormat);
    }

    public int[] getData() {
        return this.data;
    }

    public void addElementsToUniqueMap(int n, String compressedData) {
        this.uniqueMap.put(n, compressedData);
    }

    public HashMap<Integer, String> getUniqueMap() {
        return this.uniqueMap;
    }

    public void addRemainder(int r) {
        if (!remainders.contains(r)) {
            remainders.add(Math.abs(r));
        }
    }

    public ArrayList<Integer> getRemainders() {
        return this.remainders;
    }
}
