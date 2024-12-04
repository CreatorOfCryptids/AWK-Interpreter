package interpreter;

import java.util.Optional;

public class ReturnType {

    Optional<String> returnValue;
    Result result;

    public enum Result {
        NORMAL, BREAK, CONTINUE, RETURN
    }

    public ReturnType(Result result) {
        this.result = result;
        returnValue = Optional.empty();
    }

    public ReturnType(Result result, String returnValue) {
        this.result = result;
        this.returnValue = Optional.of(returnValue);
    }

    public Result getResult() {
        return result;
    }

    public boolean hasValue() {
        return returnValue.isPresent();
    }

    public Optional<String> getValue() {
        return returnValue;
    }

    public String toString() {
        if (returnValue.isPresent())
            return result + " \"" + returnValue.get() + "\"";
        else
            return result.toString();
    }
}
