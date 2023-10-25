import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType{
    HashMap<String, InterpreterDataType> values;

    InterpreterArrayDataType(){
        values = new HashMap<String, InterpreterDataType>();
    }
}
