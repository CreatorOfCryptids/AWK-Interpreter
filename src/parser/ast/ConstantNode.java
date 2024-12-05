package parser.ast;

import lexer.Token;

public class ConstantNode extends Node {

    private String value;

    /**
     * The ConstantNode Token constructor.
     * 
     * @param token A token with a value
     */
    public ConstantNode(Token token) {
        this.value = token.getValue();
    }

    public String getValue() {
        return value;
    }

    public String toString() {
        return "\"" + value + "\"";
    }
}