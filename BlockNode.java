import java.util.LinkedList;
import java.util.Optional;

public class BlockNode extends Node{

    private LinkedList<StatementNode> statements;
    private Optional<Node> condition;  
    
    public BlockNode(Optional<Node> condition, LinkedList<StatementNode> statementNodes){
        if (condition == null){
            this.condition = Optional.empty();
        }
        else{
            this.condition = condition;
        }
        statements = statementNodes;
    }

    public Optional<Node> getCondition(){
        return condition;
    }

    public LinkedList<StatementNode> getStatements(){
        return statements;
    }

    public String toString() {
        String retVal = "";
        // Add the condition
        if (condition.isPresent())
            retVal += condition.get().toString();
        retVal += "{ ";
        // Print the statements (if there are any).
        if (statements == null || statements.size() == 0)
            retVal += "NULL STATEMENT\n";
        else
            for (StatementNode s : statements)
                retVal += s.toString() + '\n';
        retVal += '}';
        return retVal;
    }
}
