import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Scanner;

public class Compression {

    private String sourceText;
    private HashMap<Character,String> code;



    public void create(String text)
    {
        int n =0;
        HashMap<Character,String> code = new HashMap();
        for(int i=0;i<text.length();i++)
        {
            if(!code.containsKey(text.charAt(i)))
            {
                String codeN = Integer.toBinaryString(n);
                code.put(text.charAt(i),codeN);
                n++;
            }
        }
        System.out.println(123);
        int size = (int) (Math.log(code.size())/Math.log(2)) +1;
        for (HashMap.Entry<Character,String> entry : code.entrySet()) {
            String newKey = entry.getValue();
            while(newKey.length()<size)
            {
                newKey = "0"+ newKey;
            }
            code.replace(entry.getKey(),newKey);
            System.out.println(newKey.getBytes().length);
        }
        this.code = code;
    }


    public byte[] encode(String text)
    {
        ArrayList<Byte> byteList = new ArrayList();
        String encoded="";
        for(int i=0;i<text.length();i++)
        {
            encoded=encoded+code.get(text.charAt(i));
        }
        for(int i=0;i<encoded.length()-8;i++)
        {
            String newByte = encoded.substring(i,i+7);
            byte b = Byte.parseByte(newByte, 2);
            byteList.add(b);
        }
        byte[] result = new byte[byteList.size()];
        for(int i = 0; i < byteList.size(); i++) {
            result[i] = byteList.get(i).byteValue();
        }
        return result;


    }

    public void save(byte[] data) throws IOException {
        File outputFile = new File("zakodowany");
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(data, 0, data.length);
        fos.flush();
        fos.close();
    }
    public void process() throws FileNotFoundException {
        Scanner scanner = new Scanner( new File("test.txt") );
        sourceText = scanner.useDelimiter("\\A").next();
        create(sourceText);
        try {
            save(encode(sourceText));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
