package parser.ast;

import java.util.LinkedList;

public class ForNode extends StatementNode {

    private Node initialization;
    private Node condition;
    private Node iterator;
    private LinkedList<StatementNode> statements;

    /**
     * The ForNode constuctor.
     * 
     * @param initial    The initialization section of the for loop.
     * @param condition  The test condition of the for loop.
     * @param iter       The iteration part of the for loop.
     * @param statements The statements in the for loop.
     */
    public ForNode(Node initial, Node condition, Node iter, LinkedList<StatementNode> statements) {
        this.initialization = initial;
        this.condition = condition;
        this.iterator = iter;
        this.statements = statements;
    }

    public Node getInitialization() {
        return initialization;
    }

    public Node getCondition() {
        return condition;
    }

    public Node getIterator() {
        return iterator;
    }

    public LinkedList<StatementNode> getStatements() {
        return statements;
    }

    public String toString() {
        String retval = "for(";
        retval += initialization.toString() + "; ";
        retval += condition.toString() + "; ";
        retval += iterator.toString() + "){\n";
        for (StatementNode sn : statements)
            retval += sn.toString() + ";\n";
        retval += "}";
        return retval;
    }
}
