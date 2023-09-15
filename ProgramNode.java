import java.util.LinkedList;
import java.util.Optional;

public class ProgramNode extends Node{
    
    private LinkedList<Node> beginBlockNodes;
    private LinkedList<Node> endBlockNodes;
    private LinkedList<Node> otherBlockNodes;
    private LinkedList<Node> functionNodes;

    public ProgramNode(){
        beginBlockNodes = new LinkedList<Node>();
        endBlockNodes = new LinkedList<Node>();
        otherBlockNodes = new LinkedList<Node>();
        functionNodes = new LinkedList<Node>();
    }

    public void add(FunctionDefinitionNode node){
        functionNodes.add(node);
    }

    public void addBeginBlock(BlockNode node){
        beginBlockNodes.add(node);
    }

    public void addEndBlock(BlockNode node){
        endBlockNodes.add(node);
    }

    public void addOtherBlock(Optional<Node> condition, BlockNode node){
        otherBlockNodes.add(node);
    }

    public String toString() {
        return functionNodes.toString() + '\n' + beginBlockNodes.toString() + '\n' + otherBlockNodes.toString() + '\n' + endBlockNodes.toString() + '\n';
    }
}
