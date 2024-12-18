package parser.ast;

import java.util.LinkedList;

public class FunctionDefinitionNode extends Node {
    private String name;
    private LinkedList<String> parameters;
    private LinkedList<StatementNode> statements;

    public FunctionDefinitionNode(String name, LinkedList<String> parameters, LinkedList<StatementNode> statementList) {
        this.name = name;
        this.parameters = parameters;
        this.statements = statementList;
    }

    public String getName() {
        return name;
    }

    public LinkedList<String> getParameters() {
        return parameters;
    }

    public LinkedList<StatementNode> getStatements() {
        return statements;
    }

    public boolean isVariadic() {
        return false;
    }

    public String toString() {
        String retVal = "function " + name + " (";

        if (parameters != null && !parameters.isEmpty())
            for (String st : parameters)
                retVal += st + ", ";

        retVal += ") { ";

        if (statements != null && statements.size() > 0)
            for (StatementNode s : statements)
                retVal += s.toString() + '\n';
        // *
        else
            retVal += "NULL STATEMENTS\n";/**/

        return retVal + "}";
    }
}
