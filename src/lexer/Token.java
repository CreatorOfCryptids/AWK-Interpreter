package lexer;

public class Token {
    public enum Type {
        WORD, NUMBER, SEPERATOR, WHILE, IF, DO, FOR, BREAK, CONTINUE, ELSE, RETURN, BEGIN,
        END, PRINT, PRINTF, NEXT, IN, DELETE, GETLINE, EXIT, NEXTFILE, FUNCTION, STRINGLITERAL, PATTERN,
        GREATEREQUALS, PLUSPLUS, MINUSMINUS, LESSEQUALS, EQUALSEQUALS, NOTEQUALS, EXPONENTEQUALS, MODEQUALS,
        TIMESEQUALS, DIVIDEEQUALS, PLUSEQUALS, MINUSEQUALS, NOTMATCH, AND, APPEND, OR, LCURLY, RCURLY,
        LSQUARE, RSQUARE, LPAREN, RPAREN, DOLLAR, MATCH, EQUALS, LESS, GREATER, NOT, PLUS, EXPONENT, MINUS,
        QUESTIONMARK, COLON, ASTRIC, SLASH, MOD, BAR, COMMA
    }

    private String value; // The actual string passed to the class
    private Type type; // Stores the type of value
    private int lineNumber; // Stores the line number of the token
    private int index; // Stores the index of the token within it's line number

    /**
     * The null string TokenType constructor.
     */
    public Token(Type token, int lineNumber, int index) {
        this.type = token;
        this.lineNumber = lineNumber;
        this.index = index;
    }

    /**
     * The TokenType constructor.
     */
    public Token(Type token, String word, int lineNumber, int index) {
        // Token(token, lineNumber, index);
        /**/
        this.type = token;
        this.lineNumber = lineNumber;
        this.index = index;
        /**/
        this.value = word;
    }

    /**
     * The toString() method.
     */
    public String toString() {
        if (value == null)
            return type.toString();
        else
            return type.toString() + "(" + value + ")";
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getIndex() {
        return index;
    }

    public Type getType() {
        return this.type;
    }

    public String getValue() {
        return value;
    }
}