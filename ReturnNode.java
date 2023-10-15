public class ReturnNode extends StatementNode{

    private Node returnValue;

    ReturnNode(Node returnValue){
        this.returnValue = returnValue;
    }

    public String toString() {
        String retval = "return ";
        retval += returnValue.toString() + ";";
        return retval;
    }
}
