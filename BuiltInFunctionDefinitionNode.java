import java.util.LinkedList;
import java.util.HashMap;
import java.util.function.Function;

public class BuiltInFunctionDefinitionNode extends FunctionDefinitionNode{

    private boolean isVariadic;
    public Function<HashMap<String, InterpreterDataType>, String> execute;

    public BuiltInFunctionDefinitionNode(String name, Function<HashMap<String, InterpreterDataType>, String> foo, boolean variadic, LinkedList<String> args) {
        super(name, args, new LinkedList<StatementNode>());
        execute = foo;
        isVariadic = variadic;
    }

    public boolean getIsVariadic(){
        return isVariadic;
    }

    public String testExecute(HashMap<String, InterpreterDataType> input){
        String s = execute.apply(input);
        return s;
    }
}
