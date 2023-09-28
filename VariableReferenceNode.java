import java.util.Optional;

public class VariableReferenceNode extends Node{

    String name;
    Optional<Node> indexExpression;

    /**
     * The VariableReferenceNode constructor.
     * @param name The name of the variable.
     */
    VariableReferenceNode(String name){
        this.name = name;
        this.indexExpression = Optional.empty();
    }

    /**
     * The VariableReferenceNode with array constructor.
     * @param name
     * @param indexExpression
     */
    VariableReferenceNode(String name, Node indexExpression){
        this.name = name;
        this.indexExpression = Optional.of(indexExpression);
    }

    public String toString() {
        String retVal = name;
        if(indexExpression.isPresent())
            retVal += "[" + indexExpression.get().toString() + "]";
        return retVal;
    }   
}