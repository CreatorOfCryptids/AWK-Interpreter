package parser.ast;

import java.util.LinkedList;

public class DoWhileNode extends StatementNode {

    private Node condition;
    private LinkedList<StatementNode> statements;

    public DoWhileNode(Node condition, LinkedList<StatementNode> statements) {
        this.condition = condition;
        this.statements = statements;
    }

    public LinkedList<StatementNode> getStatements() {
        return statements;
    }

    public Node getCondition() {
        return condition;
    }

    public String toString() {
        String retval = "do {\n";
        for (StatementNode sn : statements)
            retval += sn.toString() + ";\n";
        retval += "} while (";
        retval += condition.toString() + ")";
        return retval;
    }
}
