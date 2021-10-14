package ui.primeq;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;



public class FileManager {
    
    public FileManager(){}

    public void generateCompressedFile(String og_fileName, HashMap<Integer, String> unique_map, int[] data){
		File file = new File(og_fileName + "Compressed.txt");
        ArrayList<String> raw_data =  new ArrayList<>();
        for (int i = 0; i < data.length; i++) {
            raw_data.add(unique_map.get(data[i]));
        }
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

    public int[] readFile(String filePath) throws FileNotFoundException, IOException{
        File datafile = new File(filePath);
        byte[] data = Files.readAllBytes(datafile.toPath());

        int len = data.length;
        ByteBuffer bytebuf;
        if(data.length % 2 != 0){
            byte[] data_temp = new byte[data.length + 1];
            System.arraycopy(data, 0, data_temp, 0, data.length);
            bytebuf = ByteBuffer.wrap(data_temp);
            len ++;
        }else{
            bytebuf = ByteBuffer.wrap(data);
        }
        
        int[] data_int = new int[len];

        int k = 0;
        while(bytebuf.remaining() > 0){          
            char x = bytebuf.getChar();
            System.out.println((int) x);
            data_int[k] = (int) x;
            k++;
        }

        return data_int;
    }

    public ArrayList<Integer> readQzipFile(){
        ArrayList<Integer> data = new ArrayList<Integer>();

        return data;
    }
    
}
