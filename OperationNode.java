import java.util.Optional;

public class OperationNode extends Node{
    public enum Operation { DOLLAR, PREINC, PREDEC, UNARYPOS, UNARYNEG, NOT, /* <- before left node, 
        after left node ->*/EQ, NE, LT, LE, GT, GE, AND, OR, MATCH, NOTMATCH, POSTINC, POSTDEC, IN, 
                            EXPONENT, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, CONCATENATION}

    private Node left;
    private Optional<Node> right;
    private Operation operation;
    private static String[] opToString = new String[] {"$", "++", "--", "+", "-", "!", "==", "!=", "<", "<=", ">", ">=", "&&", "||", 
                                "~", "!~", "++", "--", "in", "^", "+", "-", "*", "/", "%", ""};
                                // This is just me being extra, basically it stores how the enum would look printed to make a prettier toString().

    /**
     * The OperationNode constructor.
     * @param left The left node of the operation.
     * @param right The right node of the operation.
     * @param operation The operation
     */
    public OperationNode(Node left, Node right, Operation operation){
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
        String retval = "";
        // Check the enum's ordinal to see if it goes before the left node or after.
        if (operation.ordinal() <= 5) 
            retval += opToString[operation.ordinal()] + left.toString();
        else
            retval += left.toString() + opToString[operation.ordinal()];
        if (right.isPresent())
            retval += right.get().toString();
        return retval;
    }
}