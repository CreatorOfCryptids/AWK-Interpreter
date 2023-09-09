import java.util.LinkedList;

public class Parser {
    TokenHandler h;

    Parser(LinkedList<Token> tokens){
        h = new TokenHandler(tokens);
    }

    /**
     * The acceptSeperators() method
     * @return True if there is one or more seperators.
     */
    boolean acceptSeperators(){
        return false;
    }

    /**
     * 
     * @return
     */
    ProgramNode parse() throws Exception{
        while(h.moreTokens()){
            // Loop calling two functions, parseFunction() and parseAction().
            // If both are false, throw and exception
        }
    }

    /**
     * The parseFunction() method.
     * @param node
     * @return
     */
    boolean parseFunction(ProgramNode node){

    }

    /**
     * The parseFunction() method.
     * @param node
     * @return
     */
    boolean parseAction(ProgramNode node){

    }

    
}
