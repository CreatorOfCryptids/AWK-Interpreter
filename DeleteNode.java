import java.util.Optional;

public class DeleteNode extends StatementNode{
    
    private VariableReferenceNode deletedVariable;
    private Optional<OperationNode> arrayIndex;

    DeleteNode (VariableReferenceNode deletedVariable){
        this.deletedVariable = deletedVariable;
        this.arrayIndex = Optional.empty();
    }

    DeleteNode (VariableReferenceNode deletedVariable, OperationNode arrayIndex){
        this.deletedVariable = deletedVariable;
        this.arrayIndex = Optional.of(arrayIndex);
    }

    public String toString() {
        String retval = "delete " + deletedVariable;
        if (arrayIndex.isPresent())
            retval += "[" + arrayIndex.get().toString() + "];";
        return retval;
    }
}
