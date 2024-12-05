package parser.ast;

public class DeleteNode extends StatementNode {

    private VariableReferenceNode deletedVariable;

    public DeleteNode(VariableReferenceNode deletedVariable) {
        this.deletedVariable = deletedVariable;
    }

    public VariableReferenceNode getDeletedVariable() {
        return deletedVariable;
    }

    public String toString() {
        String retval = "delete " + deletedVariable;
        return retval;
    }
}
