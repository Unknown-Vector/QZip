package ui.primeq;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import ui.primeq.config.Config;



public class FileManager {

    private final int NUMBEROFBITSREMAINDERS = 31;
    private final int NUMBEROFBITSPRIME = 31;
    private final int NUMBEROFBITSREMAINDER = 31;
    
    public FileManager(){}

    public void generateCompressedFile(Config config, String og_fileName, HashMap<Integer, String> unique_map, 
        int[] data, ArrayList<Integer> remainders) throws IOException{
        int noPrimes = config.getNoPrimes();
		File file = new File(og_fileName + "Compressed.txt");
        String bitString = new String("");
        
        Collections.sort(remainders, Collections.reverseOrder());
    
        for (int i = 0; i < data.length; i++) {
            String dataString =  unique_map.get(data[i]);
            if (dataString == null) {
                dataString = "s0";
            } else {
                int remainder = Integer.parseInt(dataString.substring(noPrimes+1), 2);
                String remainderString = Integer.toBinaryString(remainders.indexOf(remainder));
                remainderString = padZeros(remainderString, remainders.size());
                dataString = (dataString.substring(0, noPrimes+1)).concat(remainderString);
            }
            bitString = bitString.concat(dataString);
        }

        bitString = generateHeader(remainders, noPrimes).concat(bitString);
        byte[] byteArray = convertStringToByteArray(bitString);
  
        try{
		    FileUtils.writeByteArrayToFile(file, byteArray, false);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private String addChar(String str, String ch, int position) {
        return str.substring(0, position) + ch + str.substring(position);
    }

    private byte[] convertStringToByteArray(String bitString) {

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

        return byteArray;
    }

    private String padZeros(String str, int value) {
        while (str.length() < getNoOfBits(value)) {
            str = "0" + str;
        }
        return str;
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

        int[] data_int = new int[(int) len/2];
        int k = 0;
        while(bytebuf.remaining() > 0){          
            char x = bytebuf.getChar();
            System.out.println((int) x);
            data_int[k] = (int) x;
            k++;
        }

        return data_int;
    }

    public byte[] readQzipFile(String filePath) throws IOException{
        File datafile = new File(filePath);
        byte[] data = Files.readAllBytes(datafile.toPath());
        return data;
    }

    public String processByteArrayToString(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        String data_string = "";

        while(buffer.remaining() > 0){
            String temp = String.format("%8s", Integer.toBinaryString(buffer.get() & 0xFF)).replace(' ', '0');
            data_string += temp;
        }

        return data_string;
    }
    public void decompress(String filePath) throws IOException{
        byte[] data = readQzipFile(filePath);
        String data_str = processByteArrayToString(data);
        int prime_bits = getNoOfBits(NUMBEROFBITSPRIME);
        int remainderbits = getNoOfBits(NUMBEROFBITSREMAINDER);
        int noremainderbits = getNoOfBits(NUMBEROFBITSREMAINDERS);

        int noPrimes = Integer.parseInt(data_str, 0, prime_bits, 2);
        int remainderSize = Integer.parseInt(data_str, prime_bits,  prime_bits + remainderbits, 2);
        int remainderBits = Integer.parseInt(data_str,  prime_bits + remainderbits, prime_bits + remainderbits + noremainderbits, 2);
       

        int dict_chunk = remainderBits * remainderSize;
        String remainders = data_str.substring(prime_bits + remainderbits + noremainderbits, prime_bits + remainderbits + noremainderbits + dict_chunk);

        String[] remainderArray = new String[remainderSize];

        for(int i = 0; i < remainderSize; i++){
            remainderArray[i] = remainders.substring(i * remainderBits, (i+1) * remainderBits );
        }

        String dataString = data_str.substring(getNoOfBits(NUMBEROFBITSPRIME) + getNoOfBits(NUMBEROFBITSREMAINDER) + getNoOfBits(NUMBEROFBITSREMAINDERS) + dict_chunk);
        int dataBits = noPrimes + 1 + getNoOfBits(remainderSize);
  
        int[] dataArray = new int[dataString.length() / dataBits];
        for(int i=0; i < dataArray.length; i++) {
            String temp = dataString.substring(i * dataBits, (i+1) * dataBits);
            int index = Integer.parseInt(temp, noPrimes + 1, dataBits, 2);

            if(i == dataArray.length-1 & index == 0){
                index = Integer.parseInt(dataString, (i+1) * dataBits, dataString.length(), 2);
            }
            temp = (temp.substring(0, noPrimes+1)).concat(remainderArray[index]);
            int res = processDataChunk(temp, noPrimes);
            dataArray[i] = res;
        }
        
        byte[] decompressedData = expandData(dataArray);
        File file = new File(filePath.substring(0, filePath.length()-4) + "DeCompressed.txt");

        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(decompressedData);
            fos.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private byte[] expandData(int[] dataArray){
        String exString = "";
        for(int i = 0; i < dataArray.length; i++){
            String temp = String.format("%16s", Integer.toBinaryString(dataArray[i] & 0xFFFF)).replace(' ', '0');
            exString += temp;
        }

        byte[] expandedData = convertStringToByteArray(exString);

        return expandedData;
    }

    private int processDataChunk(String data, int noPrimes) {
        String primes = data.substring(0, noPrimes);
        int remainder = Integer.parseInt(data, noPrimes+1, data.length(),2);

        int result = 1;
        primes = new StringBuilder(primes).reverse().toString();
        for (int i = 0; i < primes.length(); i++) {
            if (primes.charAt(i) == '1') {
                result *= Config.primes[i];
            }
        }

        if (data.charAt(noPrimes) != '1') {
            return result += remainder;
        } else {
            return result -= remainder;
        }
    }

    private int getNoOfBits(int value) {
        return (int) (Math.log(value)/Math.log(2) + 1);
    }

    private String generateHeader(ArrayList<Integer> remainders, int noPrimes) {
        String remainderSize = Integer.toBinaryString(remainders.size());
        String primeSize = Integer.toBinaryString(noPrimes);

        remainderSize = padZeros(remainderSize, NUMBEROFBITSREMAINDERS);
        primeSize = padZeros(primeSize, NUMBEROFBITSPRIME);

        String remainderBits = Integer.toBinaryString(getNoOfBits(remainders.get(0)));
        remainderBits = padZeros(remainderBits, NUMBEROFBITSREMAINDER);
        String byteString = primeSize + remainderSize + remainderBits;

        for (int i = 0; i < remainders.size(); i++) {
            String remainder = Integer.toBinaryString(remainders.get(i));
            remainder = padZeros(remainder, remainders.get(0));
            byteString += remainder;      
        }

        return byteString;
    }

    // private byte[] concatArray(byte[] a, byte[] b) throws IOException{
    //     ByteArrayOutputStream outputStream = new ByteArrayOutputStream(a.length + b.length);
    //     outputStream.write(a);
    //     outputStream.write(b);

    //     byte[] c = outputStream.toByteArray();
    //     outputStream.close();
    //     return c;
    // }

    // private ArrayList<String> dataToString(ArrayList<ArrayList<Integer>> data){
    //     ArrayList<String> list = new ArrayList<String>();

    //     for(int i = 0; i < data.size(); i++){
    //         ArrayList<Integer> temp = data.get(i);
    //         String dataStr = temp.stream().map(Object::toString).collect(Collectors.joining());
    //         list.add(dataStr);
    //     }

    //     return list;
    // }
}
