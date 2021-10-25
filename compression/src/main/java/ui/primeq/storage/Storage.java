package ui.primeq.storage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import ui.primeq.FileManager;

public class Storage {

    private int[] data;
    private HashMap<Integer, String> uniqueMap;
    private ArrayList<Integer> remainders;
    private String nameOfFile;
    
    public Storage() throws IOException{
        this.data = new int[10];
        this.uniqueMap = new HashMap<>();
        this.remainders = new ArrayList<>();
    }

    public void readData(String nameOfFile, String fileFormat) throws IOException{
        FileManager fileManager = new FileManager();
        this.nameOfFile = nameOfFile;
        this.data = fileManager.readFile("./" + this.nameOfFile + fileFormat);
    }

    public String getFileName() {
        return this.nameOfFile;
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
