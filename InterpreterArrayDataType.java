import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType{
    private HashMap<String, InterpreterDataType> values;

    InterpreterArrayDataType(){
        values = new HashMap<String, InterpreterDataType>();
    }

    InterpreterArrayDataType(String[][] values){
        for(int i=0; i<values.length; i++)
            this.values.put(values[i][0], toIDT(values[i][1]));
    }

    public String getValue(String key){
        return values.get(key).getValue();
    }

    public int getSize(){
        return values.size();
    }
}