public class TernaryNode extends Node{

    private Node boolExpression;
    private Node trueCase;
    private Node falseCase;

    TernaryNode(Node boolExpression, Node trueCase, Node falseCase){
        this.boolExpression = boolExpression;
        this.trueCase = trueCase;
        this.falseCase = falseCase;
    }

    public String toString() {
        String retval = "";
        retval = boolExpression.toString() + " ? " + trueCase.toString() + " : " + falseCase.toString();
        return retval;
    }
    
    public Node getExpression(){
        return boolExpression;
    }

    public Node getTrueCase(){
        return trueCase;
    }

    public Node getFalseCase(){
        return falseCase;
    }
}
