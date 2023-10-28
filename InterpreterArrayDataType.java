import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType{
    private HashMap<String, InterpreterDataType> values;

    InterpreterArrayDataType(){
        values = new HashMap<String, InterpreterDataType>();
    }

    public String getValue(String key){
        return values.get(key).getValue();
    }
}
