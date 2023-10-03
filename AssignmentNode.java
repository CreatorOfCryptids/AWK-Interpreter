public class AssignmentNode extends Node{

    private VariableReferenceNode target;
    private OperationNode expression;

    AssignmentNode(VariableReferenceNode target, OperationNode expression){
        this.target = target;
        this.expression = expression;
    }

    public String toString() {
        String retval = target.toString() + " = " + expression.toString();
        return retval;
    }
}
