import java.util.Optional;

public class OperationNode extends Node{
    public enum Operation {EQ, NE, LT, LE, GT, GE, AND, OR, NOT, MATCH, NOTMATCH, 
                        DOLLAR, PREINC, POSTINC,PREDEC, POSTDEC, UNARYPOS, UNARYNEG, 
                        IN, EXPONENT, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, 
                        CONCATENATION}

    Node left;
    Optional<Node> right;
    Operation operation;

    /**
     * The OperationNode constructor.
     * @param left The left node of the operation.
     * @param right The right node of the operation.
     * @param operation The operation
     */
    OperationNode(Node left, Node right, Operation operation){
        this.left = left;
        this.right = Optional.of(right);
        this.operation = operation;
    }

    /**
     * The OperationNode constructor.
     * @param left The left node of the operation.
     * @param operation The right node of the operation.
     */
    OperationNode(Node left, Operation operation){
        this.left = left;
        this.operation = operation;
        this.right = Optional.empty();
    }

    public String toString() {
        String retval = left.toString() + ' ' + operation;
        if (right.isPresent())
            retval += ' ' + right.get().toString();
        return retval;
    }
}