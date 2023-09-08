import java.util.HashMap;
import java.util.LinkedList;

//import Token.Type;

public class Lexer {

    private StringHandler h;
    private int lineNumber;
    private int position;
    private LinkedList<Token> tokenList = new LinkedList<Token>();
    private HashMap<String, Token.Type> keyWords = new HashMap<String, Token.Type> (20);
    private HashMap<String, Token.Type> doubleSymbols = new HashMap<String, Token.Type> (16);
    private HashMap<String, Token.Type> singleSymbol = new HashMap<String, Token.Type> (26);
    
    public Lexer(String fileData){
        h = new StringHandler(fileData);
        lineNumber = 1; // The current line.
        position = 1;   // The current index inside the current line.
        initalizeHashMaps(keyWords, doubleSymbols, singleSymbol);
    }
    
    /**
     * The lex() method.
     * @return A linked list with all of the tokens generated from the file.
     * @throws Exception
     */
    public LinkedList<Token> lex() throws Exception{
        // Loop thru all the data from the file.
        while (!h.isDone()){
            // Look ahead to the next character to determin what to do
            char c = h.peek();  
            // Skip to the next character if its a whitespace character
            if (c == ' ' || c == '\t' || c == '\r'){
                h.swallow(1);
                position++;
            }
            // Generate a seperator token if its a newline character.
            else if (c == '\n'){
                tokenList.add(new Token(Token.Type.SEPERATOR, lineNumber, position));
                lineNumber++;
                position = 1;
                h.swallow(1);
            }
            // Check if its a string literal
            else if (c == '\"'){                
                tokenList.add(processStringLiterals());
            }
            // Check if it'a pattern
            else if (c == '`'){
                tokenList.add(processPattern());
            }
            // If it is a comment (starting with '#') skip to next line.
            else if (c == '#'){
                while(!h.isDone() && h.peek() != '\n'){
                    h.swallow(1);
                    lineNumber++;
                    position = 1;
                }
            }
            // Check to see if it is part of a double char set
            else if (h.remaining() >=2 && doubleSymbols.containsKey(h.peekString(2))){
                tokenList.add(new Token(doubleSymbols.get(h.peekString(2)), lineNumber, position));
                h.swallow(2);
                position += 2;
            }
            // Check to see if it is part of a single char set
            else if (singleSymbol.containsKey(h.peekString(1))){
                tokenList.add(new Token(singleSymbol.get(h.peekString(1)), lineNumber, position));
                // If it is a newline, iterate the line count number.
                if (h.getChar() == '\n'){ // I used the getChar() method to move the handler along.
                    lineNumber++;
                    position = 0;
                }
            }
            // Create a new number token if it is a number character or period.
            else if (Character.isDigit(c) || c == '.'){
                tokenList.add(processNumber());
            }
            // Create a new Word token if it starts with an alphabetic character
            else if (Character.isAlphabetic(c)){
                tokenList.add(processWord());
            }
            // If we don't regognise the character, throw an exception.
            else{
                throw new Exception("There is an unexpected \'" + c + "\' at Line: " + lineNumber + " Position: " + position);
            }
        }
        return tokenList;
    }

    /**
     * The processWord() method.
     * @return A word token generated from the collected word.
     */
    private Token processWord() {
        String word = "";           // Stores the collected letters.
        char c = h.peek();
        int wordStart = position;   // Stores the start index of the word.

        // Loop until the end or a non alphanumeric charater.
        while (!h.isDone() && (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_')){
            word += h.getChar();
            c = h.peek();
            position++;
            /**
            try{
                c = handler.peek();
            }
            catch (Exception StringIndexOutOfBoundsException){
                break;
            }
            /**/
        }
        // Check if it is a keyWord. If so, make it a key token instead.
        if (keyWords.containsKey(word)){
            Token keyWordToken = new Token(keyWords.get(word), lineNumber, wordStart);
            return keyWordToken;
        }
        // Otherwize, make a word token and return it.
        Token wordToken = new Token(Token.Type.WORD, word, lineNumber, wordStart);
        return wordToken;
    }

    /**
     * The processNumber() method.
     * @return A number token generated from the collected number sequence
     */
    private Token processNumber(){
        boolean foundPeriod = false;
        String number = "";         // Stores the collected numbers.
        char c = h.peek();
        int numberStart = position; // Stores the start index of the number

        // Loop until the end of the number, or the second period
        while (!h.isDone() && (Character.isDigit(c)) || (c == '.' && !foundPeriod)){
            if (c == '.') 
                foundPeriod = true; // Update if the new character is a period.
            number += h.getChar();
            c = h.peek();
            /**
            try{
                c = handler.peek();
            }
            catch (Exception StringIndexOutOfBoundsException){
                break;
            }
            /* */
            position++;
        }

        // Construct the token and return it.
        Token numberToken = new Token(Token.Type.NUMBER, number, lineNumber, numberStart);
        return numberToken;
    }

    /**
     * The processStringLiteral() method.
     * @return A StringLiteral token generated from the string literal.
     * @throws Exception
     */
    private Token processStringLiterals() throws Exception{
        h.swallow(1);           // Swallow the '"'
        String stringLiteral = "";
        char c = h.peek();
        int stringStart = position;
        position++;

        // Loop until the end of the file or the next '"'
        while (!h.isDone() && (c != '\"')){
            stringLiteral += h.getChar();
            c = h.peek();
            position++;
            // If we encounter an excape thing, skip that and pretend it's not there.
            if (c == '\\' && !h.isDone()){
                h.swallow(1);
                stringLiteral += h.getChar();
                position+=2;
                c = h.peek();
            }
            // If we reach the end of the line or the end of the file without a closing '"' we throw an exception
            if (/**!h.isDone() ||/**/ c == '\n'){
                throw new Exception("Expected a '\"' on line " + lineNumber);
            }
        }
        // Need to swallow the quotation mark so it doesn't make an incorrect string literal. 
        if (!h.isDone())
            h.swallow(1);
        position++;

        // Return the new token
        Token stringToken = new Token(Token.Type.STRINGLITERAL, stringLiteral, lineNumber, stringStart);
        return stringToken;
    }

    /**
     * The processPattern() method.
     * @return A pattern token generated by the text between two '`'s
     * @throws Exception When there isn't a closing '`' at the end of a statement.
     */
    private Token processPattern() throws Exception{
        h.swallow(1);           // Swallow the '`'
        String pattern = "";
        char c = h.peek();
        int patternStart = position;
        position++;

        //Loop until we find another '`'
        while(!h.isDone() && c != '`'){
            pattern += h.getChar();
            c = h.peek();
            position++;
            // Make sure that there isn't a missing second '`''
            if (c == '\n' || h.remaining() < 1)
                throw new Exception("Expected a '`' on line " + lineNumber);
        }
        // Eat the '`' so we don't start a new pattern in the next loop
        if (!h.isDone())
            h.swallow(1);
        position++;

        //Return the generated token
        return new Token(Token.Type.PATTERN, pattern, lineNumber, patternStart);
    }

    /**
    * The intializeHashMap() method.
    * @param keyWords The hashmap to be initalized with all the key-words.
    */
    private void initalizeHashMaps(HashMap<String, Token.Type> keyWords, HashMap<String, Token.Type> doubleSymbol, HashMap<String, Token.Type> singleSymbol){
        keyWords.put("while", Token.Type.WHILE);
        keyWords.put("if", Token.Type.IF);
        keyWords.put("do", Token.Type.DO);
        keyWords.put("for", Token.Type.FOR);
        keyWords.put("break", Token.Type.BREAK);
        keyWords.put("continue", Token.Type.CONTINUE);
        keyWords.put("else", Token.Type.ELSE);
        keyWords.put("return", Token.Type.RETURN);
        keyWords.put("BEGIN", Token.Type.BEGIN);
        keyWords.put("END", Token.Type.END);
        keyWords.put("print", Token.Type.PRINT);
        keyWords.put("printf", Token.Type.PRINTF);
        keyWords.put("next", Token.Type.NEXT);
        keyWords.put("in", Token.Type.IN);
        keyWords.put("delete", Token.Type.DELETE);
        keyWords.put("getline", Token.Type.GETLINE);
        keyWords.put("EXIT", Token.Type.EXIT);
        keyWords.put("nextfile", Token.Type.NEXTFILE);
        keyWords.put("function", Token.Type.FUNCTION);

        doubleSymbol.put(">=", Token.Type.GREATEREQUALS);
        doubleSymbol.put("++", Token.Type.PLUSPLUS);
        doubleSymbol.put("--", Token.Type.MINUSMINUS);
        doubleSymbol.put("<=", Token.Type.LESSEQUALS);
        doubleSymbol.put("==", Token.Type.EQUALSEQUALS);
        doubleSymbol.put("!=", Token.Type.NOTEQUALS);
        doubleSymbol.put("^=", Token.Type.EXPONENTEQUALS);
        doubleSymbol.put("%=", Token.Type.MODEQUALS);
        doubleSymbol.put("*=", Token.Type.TIMESEQUALS);
        doubleSymbol.put("/=", Token.Type.DIVIDEEQUALS);
        doubleSymbol.put("+=", Token.Type.PLUSEQUALS);
        doubleSymbol.put("-=", Token.Type.MINUSEQUALS);
        doubleSymbol.put("!~", Token.Type.NOTMATCH);
        doubleSymbol.put("&&", Token.Type.AND);
        doubleSymbol.put(">>", Token.Type.APPEND);
        doubleSymbol.put("||", Token.Type.OR);

        singleSymbol.put("{", Token.Type.LCURLY);
        singleSymbol.put("}", Token.Type.RCURLY);
        singleSymbol.put("[", Token.Type.LSQUARE);
        singleSymbol.put("]", Token.Type.RSQUARE);
        singleSymbol.put("(", Token.Type.LPAREN);
        singleSymbol.put(")", Token.Type.RPAREN);
        singleSymbol.put("$", Token.Type.DOLLAR);
        singleSymbol.put("~", Token.Type.MATCH);
        singleSymbol.put("=", Token.Type.EQUALS);
        singleSymbol.put("<", Token.Type.LESS);
        singleSymbol.put(">", Token.Type.GREATER);
        singleSymbol.put("!", Token.Type.NOT);
        singleSymbol.put("+", Token.Type.PLUS);
        singleSymbol.put("^", Token.Type.EXPONENT);
        singleSymbol.put("-", Token.Type.MINUS);
        singleSymbol.put("?", Token.Type.QUESTIONMARK);
        singleSymbol.put(":", Token.Type.COLON);
        singleSymbol.put("*", Token.Type.ASTRIC);
        singleSymbol.put("/", Token.Type.SLASH);
        singleSymbol.put("%", Token.Type.MOD);
        singleSymbol.put(";", Token.Type.SEPERATOR);
        singleSymbol.put("\n", Token.Type.SEPERATOR);
        singleSymbol.put("|", Token.Type.BAR);
        singleSymbol.put(",", Token.Type.COMMA);
    }
}
