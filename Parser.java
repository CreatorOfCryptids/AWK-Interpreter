import java.util.LinkedList;

public class Parser {

    TokenHandler h;
    ProgramNode pNode;

    Parser(LinkedList<Token> tokens){
        h = new TokenHandler(tokens);
        pNode = new ProgramNode();
    }

    /**
     * The parse() method.
     * @return the ProgramNode generated by the inputed LinkedList of Tokens.
     */
    ProgramNode parse() throws Exception{
        while(h.moreTokens()){
            // Loop calling two functions, parseFunction() and parseAction().
            while (h.moreTokens()){
                if (parseFunction(pNode));
                else if (parseAction(pNode));
                else
                    throw new Exception("Issue Parsing.");
                // If both are false, throw an exception
            }

            return pNode;
        }
    }

    /**
     * The parseFunction() method.
     * @param node
     * @return
     */
    boolean parseFunction(ProgramNode node) throws Exception{
        if (h.matchAndRemove(Token.Type.FUNCTION).isEmpty())
            return false;
        
        FunctionDefinitionNode func = new FunctionDefinitionNode(h);
        pNode.add(func);
        return true;
    }

    /**
     * The parseFunction() method.
     * @param node
     * @return
     */
    boolean parseAction(ProgramNode node){
        if (h.matchAndRemove(Token.Type.BEGIN).isPresent()){
            // TODO
            parseBlock();
            
            return true;
        }
        else if (h.matchAndRemove(Token.Type.END).isPresent()){
            // TODO
            parseBlock();

            return true;
        }
        else {
            parseOperation();
            parseBlock();
        }


    }

    
}
