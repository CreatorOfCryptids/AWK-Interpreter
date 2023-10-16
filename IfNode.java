import java.util.LinkedList;
import java.util.Optional;

public class IfNode extends StatementNode{
    
    private Optional<Node> condition;
    private LinkedList<StatementNode> statements;
    private Optional<IfNode> next;

    /**
     * The single if constructor.
     * @param condition
     * @param statements
     */
    IfNode(Node condition, LinkedList<StatementNode> statements){
        this.condition = Optional.of(condition);
        this.statements = statements;
        this.next = Optional.empty();
    }

    /**
     * The if-else if constructor.
     * @param condition
     * @param statements
     * @param next
     */
    IfNode(Node condition, LinkedList<StatementNode> statements, IfNode next){
        this.condition = Optional.of(condition);
        this.statements = statements;
        this.next = Optional.of(next);
    }

    /**
     * The else constructor.
     * @param statements
     */
    IfNode(LinkedList<StatementNode> statements){
        this.condition = Optional.empty();
        this.statements = statements;
        this.next = Optional.empty();
    }

    public String toString(){
        String retval = "";
        if (condition.isPresent()) // No condition means its the end else block, therefore, no "if".
            retval += "if (" + condition.get().toString() + ")";
        retval += "{\n";
        // Print out the statements
        for (StatementNode sn : statements)
            retval += sn.toString() + ";\n";
        retval += "}";
        // print out the next if statement if it exists.
        if (next.isPresent())
            retval += "else " + next.get().toString();
        return retval;
    }
}
