package parser.ast;

public class ReturnNode extends StatementNode {

    private Node returnValue;

    public ReturnNode(Node returnValue) {
        this.returnValue = returnValue;
    }

    /**
     * The getReturnValue() accessor.
     * 
     * @return A node containting the desired return value.
     */
    public Node getReturnValue() {
        return returnValue;
    }

    public String toString() {
        String retval = "return ";
        retval += returnValue.toString() + ";";
        return retval;
    }
}
