import java.util.LinkedList;

public class ForEachNode extends StatementNode{
    
    private VariableReferenceNode iterator;
    private VariableReferenceNode iteratableList;
    private LinkedList<StatementNode> statements;

    ForEachNode(VariableReferenceNode iterator, VariableReferenceNode iteratableList, LinkedList<StatementNode> statements){
        this.iterator = iterator;
        this.iteratableList = iteratableList;
        this.statements = statements;
    }

    public VariableReferenceNode getIterator(){
        return iterator;
    }

    public VariableReferenceNode getList(){
        return iteratableList;
    }

    public LinkedList<StatementNode> getStatements(){
        return statements;
    }

    public String toString() {
        String retval = "for (" + iterator.toString() + " in " + iteratableList.toString() + "){\n";
        for (StatementNode sn : statements)
            retval += sn.toString() + ";\n";
        retval += "}";
        return retval;
    }
}
