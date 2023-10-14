public class ReturnNode extends StatementNode{

    private OperationNode returnValue;

    ReturnNode(OperationNode returnValue){
        this.returnValue = returnValue;
    }

    public String toString() {
        String retval = "return ";
        retval += returnValue.toString() + ";";
        return retval;
    }
}
