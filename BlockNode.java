import java.util.LinkedList;
import java.util.Optional;

public class BlockNode extends Node{

    private LinkedList<StatementNode> statements;
    private Optional<Node> condition;  
    
    public BlockNode(Node condition, LinkedList<StatementNode> statementNodes){
        if (condition == null){
            this.condition = Optional.empty();
        }
        else{
            this.condition = Optional.of(condition);
        }
        statements = statementNodes;
    }

    public Optional<Node> getCondition(){
        return condition;
    }

    public String toString() {
        String retVal = "";
        if (condition.isPresent()){
            retVal += condition.get().toString();
        }
        retVal += " {";
        for (StatementNode s : statements)
            retVal += s.toString() + '\n';
        retVal += '}';
        return retVal;
    }
}
