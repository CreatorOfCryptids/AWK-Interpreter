
public class Token{
    enum TokenType{WORD, NUMBER, SEPERATOR}
    String value;
    TokenType type;

    /**
     * The TokenType constructor.
     */
    Token(){
        
    }

    public String toString(){
        return type + "(" +value + ")";
    }
}