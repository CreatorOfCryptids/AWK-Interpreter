import java.util.LinkedList;

public class FunctionDefinitionNode extends Node{
    private String name;
    private LinkedList<String> parameters;
    private LinkedList<StatementNode> statements;

    public FunctionDefinitionNode(TokenHandler h) throws Exception{
        name = h.matchAndRemove(Token.Type.STRINGLITERAL).get().getValue();
        h.acceptSeperators();
        if (h.matchAndRemove(Token.Type.LPAREN).isEmpty())
            throw new Exception("Expected a '(' after the " + name + " function declaration.");

        while (h.matchAndRemove(Token.Type.RPAREN).isPresent()){
            parameters.add(h.matchAndRemove(Token.Type.STRINGLITERAL).get().getValue());
            h.matchAndRemove(Token.Type.COMMA);
            h.acceptSeperators();
        }

        if (h.matchAndRemove(Token.Type.LCURLY).isEmpty())
            throw new Exception("Expected a '{' after the " + name + "function declaration.");

        while (h.matchAndRemove(Token.Type.RCURLY).isEmpty()){
            // TODO Do things? idk
        }
    }

    public FunctionDefinitionNode(String name, LinkedList<String> parameters, LinkedList<StatementNode> statementList){
        this.name = name;
        this.parameters = parameters;
        this.statements = statementList;
    }

    public String toString(){
        String retVal = "function " + name + " (";
        for (String st : parameters)
            retVal += st;
        retVal += ") {\n";
        for (StatementNode s : statements)
            retVal += s.toString() + '\n';
        return retVal;
    }
}
