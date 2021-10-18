package ui.primeq;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;



public class FileManager {
    
    public FileManager(){}

    public void generateCompressedFile(String og_fileName, HashMap<Integer, String> unique_map, int[] data){
		File file = new File(og_fileName + "Compressed.txt");
        String bitString = new String("");
        for (int i = 0; i < data.length; i++) {
            // Concat all data into 1 long bitstring
            String dataString =  unique_map.get(data[i]);
            if (dataString == null) {
                dataString = "s0";
            }
            bitString = bitString.concat(dataString);
        }

        for (int i = 8; i < bitString.length(); i += 9) {
            bitString = this.addChar(bitString, "s", i);
        }

        String[] byteString = StringUtils.split(bitString, "s");
        byte[] byteArray = new byte[byteString.length];
        for (int i = 0; i < byteString.length; i++) {
            while (byteString[i].length() < 8) {
                byteString[i] = this.addChar(byteString[i], "0", 0);
            }
            Integer temp = (Integer.parseInt(byteString[i], 2));
            byteArray[i] = temp.byteValue();
        }
        
        try{
		    FileUtils.writeByteArrayToFile(file, byteArray, false);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private String addChar(String str, String ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }

    // private ArrayList<String> dataToString(ArrayList<ArrayList<Integer>> data){
    //     ArrayList<String> list = new ArrayList<String>();

    //     for(int i = 0; i < data.size(); i++){
    //         ArrayList<Integer> temp = data.get(i);
    //         String dataStr = temp.stream().map(Object::toString).collect(Collectors.joining());
    //         list.add(dataStr);
    //     }

    //     return list;
    // }

    public int[] readFile(Path path) throws FileNotFoundException, IOException{
        byte[] data = Files.readAllBytes(path);

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
