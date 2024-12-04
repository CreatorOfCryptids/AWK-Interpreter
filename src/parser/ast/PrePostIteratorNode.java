package parser.ast;

/**
 * The PrePostIterator class.
 * 
 * I made this to store the [pre/post][inc/dec] operation nodes and still pass
 * them as StatementNodes. Its definitely not the most elegant solution, but it
 * works.
 */
public class PrePostIteratorNode extends StatementNode {
    OperationNode iterator;

    public PrePostIteratorNode(OperationNode iterator) {
        this.iterator = iterator;
    }

    public PrePostIteratorNode(Node var, OperationNode.Operation op) {
        iterator = new OperationNode(var, op);
    }

    public OperationNode getNode() {
        return iterator;
    }

    public String toString() {
        return iterator.toString();
    }
}
