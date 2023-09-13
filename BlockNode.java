import java.util.LinkedList;
import java.util.Optional;

public class BlockNode extends Node{

    LinkedList<StatementNode> statements;
    Optional<Node> condition;

    public String toString() {
        String retVal = "";
        if (condition.isPresent()){
            retVal += condition.get().toString();
        }
        retVal += " {";
        for (StatementNode s : statements)
            retVal += s.toString();
        retVal += '}';
        return retVal;
    }
    
}
