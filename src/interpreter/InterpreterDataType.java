package interpreter;

public class InterpreterDataType {
    private String value;

    public InterpreterDataType(String value) {
        this.value = value;
    }

    public InterpreterDataType() {
        // Nothing
    }

    public String getValue() {
        return value;
    }

    protected static InterpreterDataType toIDT(String value) {
        return new InterpreterDataType(value);
    }

    protected static InterpreterDataType toIDT(int value) {
        return new InterpreterDataType(Integer.toString(value));
    }

    protected static InterpreterDataType toIDT(float value) {
        return new InterpreterDataType(Float.toString(value));
    }

    /**
     * The toFloat() accessor.
     * 
     * @return A float of the internal value if it can be parsed to a float.
     * @throws Exception
     */
    public float toFloat() throws Exception {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new Exception("Expected a parseable float instead of \"" + value + "\"");
        }

    }

    /**
     * The toInt() accessor.
     * 
     * @return An int of the internal value if it can be parsed to an int.
     * @throws Exception
     */
    public int toInt() throws Exception {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new Exception("Expected a parseable integer instead of \"" + value + "\"");
        }

    }

    /**
     * The toBoolean() accessor.
     * 
     * @return true if the value parses to a non-zero float.
     *         false if else.
     */
    public boolean toBoolean() {
        try {
            float fakeBoolean = Float.parseFloat(value);
            if (fakeBoolean == 0)
                return false;
            else
                return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
