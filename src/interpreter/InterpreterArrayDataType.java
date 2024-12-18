package interpreter;

import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType {
    private HashMap<String, InterpreterDataType> values;

    public InterpreterArrayDataType() {
        values = new HashMap<String, InterpreterDataType>();
    }

    public InterpreterArrayDataType(String[] values) {
        this.values = new HashMap<String, InterpreterDataType>();
        for (int i = 0; i < values.length; i++)
            this.values.put(Integer.toString(i), toIDT(values[i]));
    }

    public InterpreterDataType getValue(String key) {
        return values.get(key);
    }

    public InterpreterDataType remove(String key) {
        return values.remove(key);
    }

    public HashMap<String, InterpreterDataType> getMap() {
        return values;
    }

    public int getSize() {
        return values.size();
    }

    public void add(String hash, InterpreterDataType value) {
        values.put(hash, value);
    }

    public void add(InterpreterDataType value) {
        values.put(Integer.toString(values.size()), value);
    }

    public boolean contains(String key) {
        return values.containsKey(key);
    }
}
