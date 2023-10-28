import java.util.LinkedList;
import java.util.HashMap;
import java.util.function.Function;

public class BuiltInFunctionDefinitionNode extends FunctionDefinitionNode{

    private boolean isVariadic;
    public Function<HashMap<String, InterpreterDataType>, String> execute;

    public BuiltInFunctionDefinitionNode(String name, Function<HashMap<String, InterpreterDataType>, String> foo, boolean variadic) {
        super(name, new LinkedList<String>(), new LinkedList<StatementNode>());
        execute = foo;
        isVariadic = variadic;
        //print, printf, getline, next, gsub, index, length, match, split, sprintf, sub, substr, tolower, toupper
    }
}
