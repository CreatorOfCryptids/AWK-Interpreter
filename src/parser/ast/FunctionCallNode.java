package parser.ast;

import java.util.LinkedList;

public class FunctionCallNode extends StatementNode {

    private String functionName;
    private LinkedList<Node> parameters;

    public FunctionCallNode(String functionName, LinkedList<Node> parameters) {
        this.functionName = functionName;
        this.parameters = parameters;
    }

    public String getName() {
        return functionName;
    }

    public LinkedList<Node> getParameters() {
        return parameters;
    }

    public boolean isVariadic() {
        return false;
    }

    public String toString() {
        String retval = functionName;
        retval += "(";
        for (Node n : parameters)
            retval += n.toString() + ',';
        retval += ")";
        return retval;
    }
}
