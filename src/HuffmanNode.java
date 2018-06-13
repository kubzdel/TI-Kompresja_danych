import java.io.Serializable;

public class HuffmanNode implements  Comparable<HuffmanNode>,Serializable {

    public char character;
    public int frequency;
    private HuffmanNode leftChild = null;
    private HuffmanNode rightChild = null;

    public HuffmanNode getLeftChild() {
        return leftChild;
    }

    public void setLeftChild(HuffmanNode leftChild) {
        this.leftChild = leftChild;
    }

    public HuffmanNode getRightChild() {
        return rightChild;
    }

    public void setRightChild(HuffmanNode rightChild) {
        this.rightChild = rightChild;
    }

    public HuffmanNode(int frequency) {
        this.frequency = frequency;
    }

    public void print(){
        System.out.println(this.character+" : "+this.frequency);
    }

    public HuffmanNode(char character, int frequency) {

        this.character = character;
        this.frequency = frequency;
    }

    @Override
    public int compareTo(HuffmanNode o) {
        return this.frequency-o.frequency;
    }


}
