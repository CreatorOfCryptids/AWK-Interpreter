/**
 * The PrePostIterator class.
 * 
 * I made this to store the [pre/post][inc/dec] operation nodes and still pass them as StatementNodes. Its definitly not the most elegant solution, but it works.
 */
public class PrePostIterator extends StatementNode{
    OperationNode iterator;

    PrePostIterator(OperationNode iterator){
        this.iterator = iterator;
    }

    PrePostIterator(Node var, OperationNode.Operation op){
        iterator = new OperationNode(var, op);
    }

    public OperationNode getNode(){
        return iterator;
    }
    
    public String toString(){
        return iterator.toString();
    }
}
