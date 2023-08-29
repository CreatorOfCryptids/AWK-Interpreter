
public class Token{
    enum TokenType{WORD, NUMBER, SEPERATOR}
    String value;
    TokenType type;
    int lineNumber;
    int inLineIndex;

    /**
     * The TokenType constructor.
     */
    Token(){
        
    }

    public String toString(){
        return type + "(" +value + ")";
    }
}