package ui.primeq;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.nio.file.Files;


public class FileManager {
    
    public FileManager(){}

    public void generateCompressedFile(String og_fileName, ArrayList<ArrayList<Integer>> data){
		File file = new File(og_fileName + ".qzip");
        ArrayList<String> raw_data =  dataToString(data);

        try{
		    FileUtils.writeLines(file, raw_data, true);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private ArrayList<String> dataToString(ArrayList<ArrayList<Integer>> data){
        ArrayList<String> list = new ArrayList<String>();

        for(int i = 0; i < data.size(); i++){
            ArrayList<Integer> temp = data.get(i);
            String dataStr = temp.stream().map(Object::toString).collect(Collectors.joining());
            list.add(dataStr);
        }

        return list;
    }

    public byte[] readFileAsBytes(String filePath) throws FileNotFoundException, IOException{
        File datafile = new File(filePath);
        byte[] data = Files.readAllBytes(datafile.toPath());
        return data;
    }

    public ArrayList<Integer> readQzipFile(){
        ArrayList<Integer> data = new ArrayList<Integer>();

        return data;
    }
    
}