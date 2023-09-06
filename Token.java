
public class Token{
    public enum Type{WORD, NUMBER, SEPERATOR, WHILE, IF, DO, FOR, BREAK, CONTINUE, ELSE, RETURN, BEGIN, 
        END, PRINT, PRINTF, NEXT, IN, DELETE, GETLINE, EXIT, NEXTFILE, FUNCTION, STRINGLITERAL, REGEXLITERAL
        }
    
    private String value;   // The actual string passed to the class
    private Type type;      // Stores the type of value
    private int lineNumber; // Stores the line number of the token
    private int index;      // Stores the index of the token within it's line number

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