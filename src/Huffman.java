import jdk.jfr.Frequency;

import java.io.Serializable;
import java.util.HashMap;
import java.util.PriorityQueue;

public class Huffman implements Serializable {
    public Huffman() {
        this.nodeQueue = new PriorityQueue<>();
        this.charFrequencyMap = new HashMap<>();
        this.codingMap = new HashMap<>();

    }

    public PriorityQueue<HuffmanNode> nodeQueue;
    public HashMap<Character,Integer> charFrequencyMap;
    public HashMap<Character,String> codingMap;
    private int i =0;

    public void computeFrequencies(String text){
        for(int i=0;i<text.length();i++){
            char key = text.charAt(i);
            if (!charFrequencyMap.containsKey(key)) {
                charFrequencyMap.put(key, 1);
            }
            else {
                int currVal = charFrequencyMap.get(key);
                charFrequencyMap.replace(key, currVal + 1);
            }
        }
        for (Character key : charFrequencyMap.keySet()) {
           HuffmanNode newNode = new HuffmanNode(key,charFrequencyMap.get(key));
           newNode.print();
           nodeQueue.add(newNode);
        }
    }

    public void createHuffmanTree(){
        while(nodeQueue.size()>1){
            HuffmanNode first = nodeQueue.remove();
            HuffmanNode second = nodeQueue.remove();
            int totalFrequency = first.frequency+second.frequency;
            HuffmanNode newParent = new HuffmanNode(totalFrequency);
            newParent.setLeftChild(first);
            newParent.setRightChild(second);
            nodeQueue.add(newParent);
        }
    }

    public void printTree(HuffmanNode node, String code){
        if(node.getLeftChild()==null){
            System.out.println(node.character+" : "+code);
        }
        else{
            printTree(node.getLeftChild(),code+'0');
            printTree(node.getRightChild(),code+'1');
        }
    }
    public void createCode(HuffmanNode node, String code){
        if(node.getLeftChild()==null){
            codingMap.put(node.character,code);
        }
        else{
            createCode(node.getLeftChild(),code+'0');
            createCode(node.getRightChild(),code+'1');
        }
    }

    public String decodeText(String text) {
        StringBuilder decodedText = new StringBuilder();
        HuffmanNode temp = nodeQueue.peek();
        for(int i =0;i<text.length();i++){
            if(text.charAt(i)=='0') {
                temp = temp.getLeftChild();
            }
                else {
                    temp = temp.getRightChild();
                }
            if(temp.getLeftChild()==null){
                    decodedText.append(temp.character);
                    temp = nodeQueue.peek();
            }
        }
        return decodedText.toString();
    }
}
