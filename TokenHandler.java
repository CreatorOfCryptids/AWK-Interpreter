import java.util.LinkedList;
import java.util.Optional;

public class TokenHandler {
    private LinkedList<Token> tokens;

    /**
     * The TokenHandler() constructor.
     * @param tokens A linked list of tokens.
     */
    TokenHandler(LinkedList<Token> tokens){
        this.tokens = tokens;
    }

    /**
     * The peek() method.
     * @param i 
     * @return The i'th token in the list.
     */
    Optional<Token> peek(int i){
        if (tokens.size() > i)
            return Optional.of(tokens.get(i));
        else
            return Optional.empty();
    }

    /**
     * The peek() method.
     * @return The top token in the list.
     */
    Optional<Token> peek(){
        if (tokens.size() > 0)
            return Optional.of(tokens.get(0));
        else
            return Optional.empty();
    }

    /**
     * The moreTokens() method.
     * @return True if the list has more tokens.
     *         False if the list does not have any more tokens.
     */
    boolean moreTokens(){
        if (tokens.size() > 0)
            return true;
        else 
            return false;
    }

    /**
     * The matchAndRemove() method.
     * @param type A type of token to compare to the top token in the list.
     * @return The top token if the types match. 
     *         Empty token if else.
     */
    Optional<Token> matchAndRemove(Token.Type type){
        if (tokens.size() > 0 && tokens.get(0).getType() == type){
            return Optional.of(tokens.pop());
        }
        else{
            return Optional.empty();
        }
    }

    /** Moved to TH for easier testing outside of Parser
     * The acceptSeperators() method
     * @return True if there is one or more seperators.
     */
    boolean acceptSeperators(){
        boolean existsSeperators = false;
        while (moreTokens() && matchAndRemove(Token.Type.SEPERATOR).isPresent())
            existsSeperators = true;
        return existsSeperators;
    }/* */
}
