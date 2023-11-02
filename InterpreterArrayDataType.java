import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType{
    private HashMap<String, InterpreterDataType> values;

    InterpreterArrayDataType(){
        values = new HashMap<String, InterpreterDataType>();
    }

    InterpreterArrayDataType(String[] values){
        this.values = new HashMap<String, InterpreterDataType>();
        for(int i=0; i<values.length; i++)
            this.values.put(Integer.toString(i), toIDT(values[i]));
    }

    public InterpreterDataType getValue(String key){
        return values.get(key);
    }

    public int getSize(){
        return values.size();
    }

    public void add(String hash, InterpreterDataType value){
        values.put(hash, value);
    }

    public boolean contains(String key){
        return values.containsKey(key);
    }
}
