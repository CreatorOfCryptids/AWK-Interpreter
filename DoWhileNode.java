import java.util.LinkedList;

public class DoWhileNode extends StatementNode{

    private Node condition;
    private LinkedList<StatementNode> statements;
    
    DoWhileNode(Node condition, LinkedList<StatementNode> statements){
        this.condition = condition;
        this.statements = statements;
    }

    public String toString(){
        String retval = "do {\n";
        for (StatementNode sn : statements)
            retval += sn.toString() + ";\n";
        retval += "} while (";
        retval += condition.toString() + ")";
        return retval;
    }
}
