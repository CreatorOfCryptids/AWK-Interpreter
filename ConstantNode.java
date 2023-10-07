public class ConstantNode extends Node{
    
    String value;
    
    /**
     * The ConstantNode Token constructor.
     * @param token A token with a value
     */
    ConstantNode(Token token){
        this.value = token.getValue();
    }

    public String toString() {
        return "\"" + value + "\"";
    }
}