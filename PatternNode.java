public class PatternNode extends Node {

    private String value;

    /**
     * The PatternNode constructor.
     * @param patternToken a pattern token.
     */
    PatternNode(Token patternToken){
        value = patternToken.getValue();
    }

    public String toString(){
        return '`' + value + '`';
    }
}