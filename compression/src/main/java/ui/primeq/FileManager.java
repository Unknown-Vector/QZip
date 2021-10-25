package ui.primeq;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;

import ui.primeq.config.Config;



public class FileManager {

    private final int NUMBEROFBITSREMAINDERS = 31;
    private final int NUMBEROFBITSPRIME = 31;
    private final int NUMBEROFBITSREMAINDER = 31;
    
    public FileManager(){}

    public void generateCompressedFile(int noPrimes, String og_fileName, HashMap<Integer, String> unique_map, 
        int[] data, ArrayList<Integer> remainders) throws IOException{
		File file = new File(og_fileName + "Compressed.txt");
        String bitString = new String("");
        // System.out.println(remainders);
        Collections.sort(remainders, Collections.reverseOrder());
        // System.out.println(remainders);
        for (int i = 0; i < data.length; i++) {
            // Concat all data into 1 long bitstring
            String dataString =  unique_map.get(data[i]);
            if (dataString == null) {
                dataString = "s0";
            } else {
                // System.out.println("before: "+ dataString);
                int remainder = Integer.parseInt(dataString.substring(noPrimes+1), 2);
                String remainderString = Integer.toBinaryString(remainders.indexOf(remainder));
                remainderString = padZeros(remainderString, remainders.size());
                // System.out.println(Integer.toBinaryString(remainder) + "to" + remainderString);
                dataString = (dataString.substring(0, noPrimes+1)).concat(remainderString);
                // System.out.println("after:  " + dataString + "\n");
            }
            bitString = bitString.concat(dataString);
            // System.out.println(bitString.length());
        }
        // System.out.println("No of Pre-concat Bits = " + bitString.length());
        // System.out.println("Final Print length = " + bitString.length());
        // System.out.println("HEADER = " + generateHeader(remainders, noPrimes));
        // System.out.println("HEADER LENGTH = " + generateHeader(remainders, noPrimes).length());
        bitString = generateHeader(remainders, noPrimes).concat(bitString);
        // System.out.println("No of Bits = " + bitString.length());
        // System.out.println(bitString);
        byte[] byteArray = convertStringToByteArray(bitString);
        // byte[] byteArray = convertStringToByteArray(bitString);

        // System.out.println("BYTE ARRAY SIZE AT THE END! = " + byteArray.length);
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

    private byte[] concatArray(byte[] a, byte[] b) throws IOException{
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(a.length + b.length);
        outputStream.write(a);
        outputStream.write(b);

        byte[] c = outputStream.toByteArray();
        outputStream.close();
        return c;
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

    public int[] readFile(String filePath) throws FileNotFoundException, IOException{
        File datafile = new File(filePath);
        byte[] data = Files.readAllBytes(datafile.toPath());
        System.out.println(processByteArrayToString(data));
        // System.out.println("READ FILE = " + data.length);
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
        // System.out.println("NEW LENGTH = " + len);
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
        System.out.println("No of bits = " + data_str.length());
        System.out.println("decompress data string: " + data_str);

        int noPrimes = Integer.parseInt(data_str, 0, getNoOfBits(NUMBEROFBITSPRIME), 2);
        // System.out.println("noPrimes : " + data_str.substring(0, getNoOfBits(NUMBEROFBITSPRIME)));
        int remainderSize = Integer.parseInt(data_str, getNoOfBits(NUMBEROFBITSPRIME),  getNoOfBits(NUMBEROFBITSPRIME) + getNoOfBits(NUMBEROFBITSREMAINDER), 2);
        // System.out.println("remainderSize : " + data_str.substring(getNoOfBits(NUMBEROFBITSPRIME), getNoOfBits(NUMBEROFBITSPRIME) + getNoOfBits(NUMBEROFBITSREMAINDER)));
        int remainderBits = Integer.parseInt(data_str,  getNoOfBits(NUMBEROFBITSPRIME) + getNoOfBits(NUMBEROFBITSREMAINDER), getNoOfBits(NUMBEROFBITSPRIME) + getNoOfBits(NUMBEROFBITSREMAINDER) + getNoOfBits(NUMBEROFBITSREMAINDERS), 2);
        System.out.println("remainderBits : " + data_str.substring(getNoOfBits(NUMBEROFBITSPRIME) + getNoOfBits(NUMBEROFBITSREMAINDER), getNoOfBits(NUMBEROFBITSPRIME) + getNoOfBits(NUMBEROFBITSREMAINDER) + getNoOfBits(NUMBEROFBITSREMAINDERS)));

        int dict_chunk = remainderBits * remainderSize;
        // System.out.println("dict_chunk : " + dict_chunk);
        String remainders = data_str.substring(getNoOfBits(NUMBEROFBITSPRIME) + getNoOfBits(NUMBEROFBITSREMAINDER) + getNoOfBits(NUMBEROFBITSREMAINDERS),  getNoOfBits(NUMBEROFBITSPRIME) + getNoOfBits(NUMBEROFBITSREMAINDER) + getNoOfBits(NUMBEROFBITSREMAINDERS) + dict_chunk);

        String[] remainderArray = new String[remainderSize];
        // System.out.println(remainders);
        for(int i = 0; i < remainderSize; i++){
            remainderArray[i] = remainders.substring(i * remainderBits, (i+1) * remainderBits );
            // System.out.println(i + " : " + remainderArray[i]);
        }

        String dataString = data_str.substring(getNoOfBits(NUMBEROFBITSPRIME) + getNoOfBits(NUMBEROFBITSREMAINDER) + getNoOfBits(NUMBEROFBITSREMAINDERS) + dict_chunk);
        System.out.println("dataChuncks: " + dataString.length());
        int dataBits = noPrimes + 1 + getNoOfBits(remainderSize);
        // System.out.println("dataBits " + dataBits);
        int[] dataArray = new int[dataString.length() / dataBits];
        for(int i=0; i < dataArray.length; i++) {
            String temp = dataString.substring(i * dataBits, (i+1) * dataBits);
            System.out.println("tempsub " + temp);
            int index = Integer.parseInt(temp, noPrimes + 1, dataBits, 2);

            if(i == dataArray.length-1 & index == 0){
                index = Integer.parseInt(dataString, (i+1) * dataBits, dataString.length(), 2);
            }
            temp = (temp.substring(0, noPrimes+1)).concat(remainderArray[index]);
            // System.out.println("temp " + temp);
            System.out.println(i);
            int res = processDataChunk(temp, noPrimes);
            System.out.println(res);
            System.out.println();
            dataArray[i] = res;
        }
        
        byte[] decompressedData = expandData(dataArray);
        File file = new File(filePath.substring(0, filePath.length()-4) + "DeCompressed.txt");

        try{
		    // FileUtils.writeByteArrayToFile(file, decompressedData, false);
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
        System.out.println(data);
        System.out.println("Prime String = " + data.substring(0, noPrimes));
        String primes = data.substring(0, noPrimes);
        System.out.println(data.substring(noPrimes+1));
        int remainder = Integer.parseInt(data, noPrimes+1, data.length(),2);
        System.out.println("Remainder = " + remainder);
        int result = 1;
        primes = new StringBuilder(primes).reverse().toString();
        for (int i = 0; i < primes.length(); i++) {
            if (primes.charAt(i) == '1') {
                result *= Config.primes[i];
            }
        }
        System.out.println("Composite No :" + result);

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
        // System.out.println("remainderSize(G) : " + Integer.parseInt(remainderSize, 2));
        primeSize = padZeros(primeSize, NUMBEROFBITSPRIME);
        // System.out.println("psp : " + primeSize);

        String remainderBits = Integer.toBinaryString(getNoOfBits(remainders.get(0)));
        // System.out.println("remainderBits(G) : " + Integer.parseInt(remainderBits, 2));
        remainderBits = padZeros(remainderBits, NUMBEROFBITSREMAINDER);
        // System.out.println("ps : " + noPrimes + "  Noof remain : " + remainders.size() + " length of r : " + getNoOfBits(remainders.get(0)));
        String byteString = primeSize + remainderSize + remainderBits;
        // System.out.println("bs : " + byteString);

        for (int i = 0; i < remainders.size(); i++) {
            String remainder = Integer.toBinaryString(remainders.get(i));
            remainder = padZeros(remainder, remainders.get(0));
            byteString += remainder;      
        }

        // byte[] headerByte = convertStringToByteArray(byteString);

        return byteString;
    }
}
