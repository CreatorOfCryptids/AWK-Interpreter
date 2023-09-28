import java.util.LinkedList;

public class FunctionDefinitionNode extends Node{
    private String name;
    private LinkedList<String> parameters;
    private LinkedList<StatementNode> statements;

    public FunctionDefinitionNode(String name, LinkedList<String> parameters, LinkedList<StatementNode> statementList){
        this.name = name;
        this.parameters = parameters;
        this.statements = statementList;
    }

    public String toString(){
        String retVal = "function " + name + " (";

        if (parameters != null && !parameters.isEmpty())
            for (String st : parameters)
                retVal += st + ", ";

        retVal += ") { ";

        //TODO remove the null functionality of the toString.
        if (statements != null && statements.size() >0)
            for (StatementNode s : statements)
                retVal += s.toString() + '\n';
        //*
        else
            retVal += "NULL STATEMENTS\n";/**/

        return retVal + "}";
    }
}