import java.util.LinkedList;
import java.util.Optional;

public class TokenHandler {
    private LinkedList<Token> tokens;
    private int lineNumber = 1;
    private int index = 0;

    /**
     * The TokenHandler() constructor.
     * 
     * @param tokens A linked list of tokens.
     */
    TokenHandler(LinkedList<Token> tokens){
        this.tokens = tokens;
    }

    /**
     * Returns the token i tokens ahead.
     * 
     * @param i How many tokens ahead to peek.
     * @return Optional with the i'th token in the list.
     */
    Optional<Token> peek(int i){
        if (tokens.size() > i)
            return Optional.of(tokens.get(i));
        else
            return Optional.empty();
    }

    /**
     * Returns the next token in the list.
     * 
     * @return Optional with the top token in the list.
     */
    Optional<Token> peek(){
        if (tokens.size() > 0)
            return Optional.of(tokens.get(0));
        else
            return Optional.empty();
    }

    /**
     * Sees if there are more tokens in the list.
     * 
     * @return True if the list has more tokens.
     *         False if the list does not have any more tokens.
     */
    boolean moreTokens(){
        // See if the Linked List has entries.
        if (tokens.size() > 0)
            return true;
        else 
            return false;
    }

    /**
     * Removes and returns the first item in the list if it has the same Token.Type as the provided Type.
     * 
     * @param type A type of token to compare to the top token in the list.
     * @return Optional with the top token if the types match. 
     *         Empty Optional if else.
     */
    Optional<Token> matchAndRemove(Token.Type type){
        if (tokens.size() > 0 && tokens.get(0).getType() == type){
            // move lineNumber and index along for better 
            lineNumber = tokens.get(0).getLineNumber();
            index = tokens.get(0).getIndex();
            return Optional.of(tokens.pop());
        }
        else{
            return Optional.empty();
        }
    }

    /**
     * Returns a string with the current index and line number to allow for easier debuging for future users of this compiler :)
     * @return A string containing the line and index of the last processed token.
     */
    String getErrorPosition(){
        String retval = "Line: " + lineNumber + " Index: " + index;
        return retval;
    }

    /** Moved acceptSeperators() to TH for easier testing outside of Parser. Ignore otherwize.
     * The acceptSeperators() method
     * @return True if there is one or more seperators.
     *
    boolean acceptSeperators(){
        boolean existsSeperators = false;
        while (moreTokens() && matchAndRemove(Token.Type.SEPERATOR).isPresent())
            existsSeperators = true;
        return existsSeperators;
    }/* */
}
