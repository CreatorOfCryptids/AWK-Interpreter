import java.util.LinkedList;

public class ProgramNode extends Node{
    
    private LinkedList<Node> beginBlockNodes;
    private LinkedList<Node> endBlockNodes;
    private LinkedList<Node> otherBlockNodes;
    private LinkedList<FunctionDefinitionNode> functionNodes;

    public ProgramNode(){
        beginBlockNodes = new LinkedList<Node>();
        endBlockNodes = new LinkedList<Node>();
        otherBlockNodes = new LinkedList<Node>();
        functionNodes = new LinkedList<FunctionDefinitionNode>();
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

    public void addOtherBlock(BlockNode node){
        otherBlockNodes.add(node);
    }

    public String toString() {
        String retVal = "";
        // Add the toStrings of the funciton nodes...
        for(Node n : functionNodes){
            retVal += n.toString() + '\n';
        }
        // the begin blocks...
        for(Node n : beginBlockNodes){
            retVal += "BEGIN " + n.toString() + '\n';
        }
        // the other blocks...
        for(Node n : otherBlockNodes){
            retVal += n.toString() + '\n';
        }
        // and the end blocks.
        for(Node n : endBlockNodes){
            retVal += "END " + n.toString() + '\n';
        }
        return retVal;
    }

    public LinkedList<FunctionDefinitionNode> getFunctionNodes() {
        return functionNodes;
    }

    public LinkedList<Node> getBeginNodes(){
        return beginBlockNodes;
    }

    public LinkedList<Node> getOtherNodes(){
        return otherBlockNodes;
    }

    public LinkedList<Node> getEndNodes(){
        return endBlockNodes;
    }
}
