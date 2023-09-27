import java.util.Optional;

public class OperationNode extends Node{
    public enum Operation {EQ, NE, LT, LE, GT, GE, AND, OR, NOT, MATCH, NOTMATCH, 
                        DOLLAR, PREINC,POSTINC,PREDEC, POSTDEC,UNARYPOS, UNARYNEG, 
                        IN, EXPONENT, ADD, SUBTRACT,MULTIPLY, DIVIDE,MODULO, 
                        CONCATENATION}

    Node left;
    Optional<Node> right;
    Operation op;

    OperationNode(Node left, Node right, Operation op){
        this.left = left;
        this.right = Optional.of(right);
        this.op = op;
    }

    OperationNode(Node left, Operation op){
        this.left = left;
        this.op = op;
        this.right = Optional.empty();
    }

    public String toString() {
        // TODO
        String retval = "";
        return retval;
    }
    

}

