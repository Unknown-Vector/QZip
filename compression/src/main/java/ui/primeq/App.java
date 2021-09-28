package ui.primeq;

import java.io.IOException;
// import java.util.ArrayList;
// import java.util.HashMap;
import java.util.ArrayList;

public class App 
{
    public static void main( String[] args ) throws IOException
    {
        System.out.println("Hello There!");
        fileManager man = new fileManager();
        ArrayList<ArrayList<Integer>> test =  new ArrayList<ArrayList<Integer>>();
        for(int i = 0; i < 500; i++){
            ArrayList<Integer> temp = new ArrayList<Integer>();
            for(int j = 0; j < 1600; j++){
                temp.add(j);
            }
            test.add(temp);
        }
        man.generateCompressedFile("test", test);
    }
}
