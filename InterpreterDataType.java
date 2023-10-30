public class InterpreterDataType {
    private String value;

    InterpreterDataType(String value){
        this.value = value;
    }

    InterpreterDataType(){
        // Nothing
    }

    String getValue(){
        return value;
    }

    protected static InterpreterDataType toIDT(String value){
        return new InterpreterDataType(value);
    }
    
    protected static InterpreterDataType toIDT(int value){
        return new InterpreterDataType(Integer.toString(value));
    }

    protected static InterpreterDataType toIDT(float value){
        return new InterpreterDataType(Float.toString(value));
    }
}
