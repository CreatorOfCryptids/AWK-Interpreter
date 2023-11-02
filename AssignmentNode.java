public class AssignmentNode extends StatementNode{

    private Node target;
    private Node expression;

    AssignmentNode(Node target, Node expression){
        this.target = target;
        this.expression = expression;
    }

    public String toString() {
        String retval = target.toString() + "=" + expression.toString();
        return retval;
    }

    public Node getLeft(){
        return target;
    }

    public Node getRight(){
        return expression;
    }
}
