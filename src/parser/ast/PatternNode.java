package parser.ast;

import lexer.Token;

public class PatternNode extends Node {

    private String value;

    /**
     * The PatternNode constructor.
     * 
     * @param patternToken a pattern token.
     */
    public PatternNode(Token patternToken) {
        value = patternToken.getValue();
    }

    public String toString() {
        return "`" + value + "`";
    }

    public String getPattern() {
        return value;
    }
}