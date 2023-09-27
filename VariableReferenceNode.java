import java.util.Optional;

public class VariableReferenceNode extends Node{

    String name;
    Optional<Node> indexExpression;

    VariableReferenceNode(String name){
        this.name = name;
        this.indexExpression = Optional.empty();
    }

    VariableReferenceNode(String name, Node indexExpression){
        this.name = name;
        this.indexExpression = Optional.of(indexExpression);
    }

    public String toString() {
        // TODO
        String retVal = "";
        return retVal;
    }
    
}
