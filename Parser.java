import java.util.LinkedList;
import java.util.Optional;

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
    public ProgramNode parse() throws Exception{
        // Loop calling two functions, parseFunction() and parseAction().
        while (h.moreTokens()){
            if (parseFunction(pNode)){}
            else if (parseAction(pNode)){}
            else
                throw new Exception("Issue Parsing.");
            // If both are false, throw an exception
        }
        
        return pNode;
    }

    /**
     * The parseFunction() method.
     * @param node
     * @return
     */
    boolean parseFunction(ProgramNode node) throws Exception{
        if (h.matchAndRemove(Token.Type.FUNCTION).isEmpty())
            return false;
        
        else{
            String name; 
            LinkedList<String> parameters = new LinkedList<String>();
            LinkedList<StatementNode> statements = new LinkedList<StatementNode>();

            h.acceptSeperators();

            // Take in name, if its missing throw an exception.
            if (h.peek().get().getType() == Token.Type.STRINGLITERAL)
                name = h.matchAndRemove(Token.Type.STRINGLITERAL).get().getValue();
            else
                throw new Exception("Expected a name for the function.");

            if (h.matchAndRemove(Token.Type.LPAREN).isEmpty())
                throw new Exception("Expected a '(' after the " + name + " function declaration.");

            // Take in the parameters,
            while (h.matchAndRemove(Token.Type.RPAREN).isEmpty()){
                parameters.add(h.matchAndRemove(Token.Type.STRINGLITERAL).get().getValue());
                h.matchAndRemove(Token.Type.COMMA);
                h.acceptSeperators();
            }
            if (h.matchAndRemove(Token.Type.LCURLY).isEmpty())
                throw new Exception("Expected a '{' after the " + name + "function declaration.");
            
            
            while (h.matchAndRemove(Token.Type.RCURLY).isEmpty()) {
                Optional<StatementNode> statement = parseStatement();
                if (statement.isPresent())
                    statements.add(statement.get());
            }

            pNode.add(new FunctionDefinitionNode(name, parameters, null));
            return true;
        }
    }

    /**
     * The parseFunction() method.
     * @param node
     * @return
     */
    boolean parseAction(ProgramNode node){
        //ToDo move the selection of which block to alocate to after we know what the condition looks like.
        if (h.matchAndRemove(Token.Type.BEGIN).isPresent()){
            node.addBeginBlock(parseBlock());
            return true;
        }
        else if (h.matchAndRemove(Token.Type.END).isPresent()){
            node.addEndBlock(parseBlock());

            return true;
        }
        else {
            parseOperation();
            parseBlock();
            return true;
        }
    }

    private BlockNode parseBlock(){
        return null;
    }
    
    private Optional<Node> parseOperation(){
        return Optional.empty();
    }

    private Optional<StatementNode> parseStatement(){
        return Optional.empty();
    }
}
