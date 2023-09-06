import java.util.HashMap;
import java.util.LinkedList;

//import Token.Type;

public class Lexer {

    private StringHandler h;
    private int lineNumber;
    private int position;
    private LinkedList<Token> tokenList = new LinkedList<Token>();
    private HashMap<String, Token.Type> keyWords = new HashMap<String, Token.Type> (20);
    //private HashMap<String, Token.Type>

    public Lexer(String fileData){
        h = new StringHandler(fileData);
        lineNumber = 1; // The current line.
        position = 1;   // The current index inside the current line.
        initalizeHashMap(keyWords);
    }
    
    /**
     * The lex() method.
     * @return A linked list with all of the tokens generated from the file.
     * @throws Exception
     */
    public LinkedList<Token> lex() throws Exception{
        // Loop thru all the data from the file.
        while (!h.isDone()){
            char c = h.peek();  // Look ahead to the next character to determin what to do
            // Skip to the next character if its a whitespace character
            if (c == ' ' || c == '\t' || c == '\r'){
                h.swallow(1);
                position++;
            }
            // Create a new number token if it is a number character or period.
            else if (Character.isDigit(c) || c == '.'){
                tokenList.add(processNumber());
            }
            // Create a new Word token if it starts with an alphabetic character
            else if (Character.isAlphabetic(c)){
                tokenList.add(processWord());
            }
            // Generate a seperator token if its a newline character.
            else if (c == '\n'){
                tokenList.add(new Token(Token.Type.SEPERATOR, null, lineNumber, position));
                lineNumber++;
                position = 1;
                h.swallow(1);
            }
            // String literals
            else if (c == '\"'){                
                tokenList.add(processStringLiterals());
            }
            else if (c == ';'){
                tokenList.add(new Token(Token.Type.SEPERATOR, null, lineNumber, position));
                h.swallow(1);
                position++;
            }
            // If it is a comment (starting with '#') skip to next line.
            else if (c == '#'){
                while(!h.isDone() && h.peek() != '\n'){
                    h.swallow(1);
                    lineNumber++;
                    position = 1;
                }
            }
            else if (c == '`' /**/|| c == '*' /**/){
                // TODO
                h.swallow(1);
            }
            // If we don't regognise the character, throw an exception.
            else{
                throw new Exception("We caught a \'" + c + "\' at Line: " + lineNumber + " Position: " + position);
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
    * The intializeHashMap() method.
    * @param HashMap The hashmap to be initalized with all the key-words.
    */
    private void initalizeHashMap(HashMap<String, Token.Type> HashMap){
        HashMap.put("while", Token.Type.WHILE);
        HashMap.put("if", Token.Type.IF);
        HashMap.put("do", Token.Type.DO);
        HashMap.put("for", Token.Type.FOR);
        HashMap.put("break", Token.Type.BREAK);
        HashMap.put("continue", Token.Type.CONTINUE);
        HashMap.put("else", Token.Type.ELSE);
        HashMap.put("return", Token.Type.RETURN);
        HashMap.put("BEGIN", Token.Type.BEGIN);
        HashMap.put("END", Token.Type.END);
        HashMap.put("print", Token.Type.PRINT);
        HashMap.put("printf", Token.Type.PRINTF);
        HashMap.put("next", Token.Type.NEXT);
        HashMap.put("in", Token.Type.IN);
        HashMap.put("delete", Token.Type.DELETE);
        HashMap.put("getline", Token.Type.GETLINE);
        HashMap.put("EXIT", Token.Type.EXIT);
        HashMap.put("nextfile", Token.Type.NEXTFILE);
        HashMap.put("function", Token.Type.FUNCTION);
    }
}
