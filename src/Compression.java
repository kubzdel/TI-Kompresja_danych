import java.io.*;
import java.util.*;

public class Compression {

    private String sourceText;
    private HashMap<Character,String> code;
    private HashMap<String,Character> loadedCode;
    private String loadedText;
    private int encodedTextLength;
    private int loadedTextLength;
    private ArrayList<String>loadedTextList= new ArrayList<>();
    private int loadedCodeLength;
    private String encodedText;


    public void create(String text)
    {
        int n =0;
        HashMap<Character,String> code = new HashMap();
        for(int i=0;i<text.length();i++) {
            if (!code.containsKey(text.charAt(i))) {
                String codeN = Integer.toBinaryString(n);
                code.put(text.charAt(i), codeN);
                n++;
            }
        }
        int size = (int) (Math.log(code.size())/Math.log(2)) +1;
        for (HashMap.Entry<Character,String> entry : code.entrySet()) {
            String newKey = entry.getValue();
            while(newKey.length()<size)
            {
                newKey = "0"+ newKey;
            }
            code.replace(entry.getKey(),newKey);
        }
        this.code = code;
    }



    public BitSet encode(String text)
    {
        ArrayList<String> encodedList = new ArrayList<>();
        for(int i=0;i<text.length();i++)
        {
            encodedList.add(code.get(text.charAt(i)));
        }

        encodedTextLength = encodedList.size();
        int k=0;
        BitSet bitSet = new BitSet();
        for(String batch:encodedList)
        {
            for(int i=0;i<batch.length();i++)
            {
                if(batch.charAt(i)=='1')
                {
                    bitSet.set(k);
                }
                k++;
            }
        }
        return bitSet;
    }

    public void save(BitSet data) throws IOException {
        byte[] bytes = data.toByteArray();
        File outputFile = new File("zakodowany.txt");
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.write(bytes);
        fos.flush();
        fos.close();

        Writer wr = new FileWriter("dlugosc.txt");
        wr.write(encodedTextLength+"");
        wr.close();

        FileOutputStream fileOut =  new FileOutputStream("mapping.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(code);
        out.close();
        fileOut.close();
    }

    public void load() throws IOException, ClassNotFoundException {

        FileInputStream map = new FileInputStream("mapping.ser");
        ObjectInputStream in = new ObjectInputStream(map);
        HashMap<Character,String> pom = new HashMap();
        pom = (HashMap<Character, String>) in.readObject();
        loadedCodeLength = pom.values().iterator().next().length();
        HashMap<String, Character> myNewHashMap = new HashMap<>();
        for(Map.Entry<Character, String> entry : pom.entrySet()){
            myNewHashMap.put(entry.getValue(), entry.getKey());
        }
        loadedCode = myNewHashMap;
        in.close();
        map.close();


        File file = new File("zakodowany.txt");
        byte[] fileData = new byte[(int) file.length()];
        DataInputStream dis = new DataInputStream(new FileInputStream(file));
        dis.readFully(fileData);
        dis.close();
        ArrayList<String>loadedBatches = new ArrayList<>();
        int k=0;
        StringBuilder batches = new StringBuilder();
        for(byte block:fileData)
        {
            if(k%10000==0){
                double proc = (k / (double) fileData.length) * 100;
                System.out.println(proc);}
                String temp = Integer.toBinaryString((block + 256) % 256);// output -> "10000010

                while (temp.length() < 8) {
                    temp = '0' + temp;
                }
                temp = new StringBuilder(temp).reverse().toString();
                batches.append(temp);

        k++;
        }
        loadedTextList= loadedBatches;
        loadedText= batches.toString();

        encodedTextLength =Integer.valueOf(new Scanner(new File("dlugosc.txt")).useDelimiter("\\Z").next());
    }

    public String decode(String text){
        StringBuilder decodedText=new StringBuilder();
        for(int k=0;k<encodedTextLength;k++) {

                decodedText.append(loadedCode.get(text.substring(k*loadedCodeLength,k * loadedCodeLength+loadedCodeLength)));
        }
        System.out.println(sourceText.substring(sourceText.length()-50));
        System.out.println(decodedText.substring(decodedText.length()-50));
        return decodedText.toString();

    }

    public void validate(String sourceText,String decodedText)
    {
        if(sourceText.equals(decodedText))
        {
            System.out.println("Tekst został zakodowany i odkodowany poprawanie");
        }
        else
        {
            System.out.println("Tekst został niepoprawnie odkodowany!");
        }
    }

    public void process() throws FileNotFoundException {
        Scanner scanner = new Scanner( new File("norm_wiki_sample.txt") );
        sourceText = scanner.useDelimiter("\\A").next();
        if(sourceText.charAt(sourceText.length()-1)==' ')
        sourceText = sourceText.substring(0,sourceText.length()-1);
        create(sourceText);
        try {
            save(encode(sourceText));
            load();
            validate(sourceText,decode(loadedText));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
