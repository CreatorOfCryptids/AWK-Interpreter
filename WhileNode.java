import java.util.LinkedList;

public class WhileNode extends StatementNode{

    private OperationNode condition;
    private LinkedList<StatementNode> statements;

    WhileNode (OperationNode condition, LinkedList<StatementNode> statements){
        this.condition = condition;
        this.statements = statements;
    }

    public String toString(){
        String retval = "while (" + condition.toString() + "){\n";
        for (StatementNode sn : statements)
            retval += sn.toString() + ";\n";
        retval += "}";
        return retval;
    }
}
