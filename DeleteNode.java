public class DeleteNode extends StatementNode{
    
    private VariableReferenceNode deletedVariable;

    DeleteNode (VariableReferenceNode deletedVariable){
        this.deletedVariable = deletedVariable;
    }

    public VariableReferenceNode getDeletedVariable(){
        return deletedVariable;
    }

    public String toString() {
        String retval = "delete " + deletedVariable;
        return retval;
    }
}
