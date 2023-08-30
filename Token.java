
public class Token{
    public enum Type{WORD, NUMBER, SEPERATOR}
    
    private String value;
    private Type type;
    private int lineNumber;
    private int index;

    /**
     * The null string TokenType constructor.
     */
    Token(Type token, int lineNumber, int index){
        this.type = token;
        this.lineNumber = lineNumber;
        this.index = index;
    }

    /**
     * The TokenType constructor.
     */
    Token(Type token, String word, int lineNumber, int index){
        this.type = token;
        this.value = word;
        this.lineNumber = lineNumber;
        this.index = index;
    }

    /**
     * The toString() method.
     */
    public String toString(){
        if (value == null) 
            return type.toString();
        else
            return type.toString() + "(" + value + ")";
    }
}