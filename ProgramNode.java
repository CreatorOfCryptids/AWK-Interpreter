import java.util.LinkedList;

public class ProgramNode extends Node{
    
    private LinkedList<Node> beginBlockNodes;
    private LinkedList<Node> endBlockNodes;
    private LinkedList<Node> otherBlockNodes;
    private LinkedList<Node> functionNodes;

    public ProgramNode(){
        // TODO
    }

    public void add(FunctionDefinitionNode node){
        functionNodes.add(node);
    }

    public void add(BlockNode node){
        if (node.condition() == Token.Type.BEGIN){
            beginBlockNodes.add(node);
        }
        // TODO continue
    }

    public String toString() {
        return beginBlockNodes.toString() + '\n' + otherBlockNodes.toString() + '\n' + endBlockNodes.toString() + '\n' + functionNodes.toString() + '\n';
    }
}
