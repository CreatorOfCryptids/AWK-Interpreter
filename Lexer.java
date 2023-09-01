import java.util.LinkedList;

public class Lexer {

    private StringHandler h;
    private int lineNumber;
    private int position;
    private LinkedList<Token> tokenList = new LinkedList<Token>();

    public Lexer(String fileData){
        h = new StringHandler(fileData);
        lineNumber = 1; // The current line.
        position = 0;   // The current index inside the current line.
    }
    
    public LinkedList<Token> lex() throws Exception{
        // Loop thru all the data from the file.
        while (!h.isDone()){
            char c = h.peek();  // Look ahead to the next character to determin what to do
            // Skip to the next character if its a whitespace character
            if (c == ' ' || c == '\t' || c == '\r'){
                h.swallow(1);
            }
            // Generate a seperator token if its a newline character.
            else if (c == '\n'){
                tokenList.add(new Token(Token.Type.SEPERATOR, null, lineNumber, position));
                lineNumber++;
                position = 0;
                h.swallow(1);
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
                throw new Exception();
            }
        }
        return tokenList;
    }
    /**
     * The processWord() method.
     * @return A token generated from the collected word.
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
        // Make the token and return it.
        Token wordToken = new Token(Token.Type.WORD, word, lineNumber, wordStart);
        return wordToken;
    }

    /**
     * The processNumber() method.
     * @return
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
}
