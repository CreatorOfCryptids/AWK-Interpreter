//import java.util.*;

public class StringHandler {

    private String file = "";
    private int fingerIndex = 0;

    StringHandler(String file){
        this.file = file;
    }

    /**
    * The peek() method.
    * @param i How far ahead the method looks.
    * @return The char i characters ahead of the index.
    * Looks “i” characters ahead and returns that character; doesn’t move the index.
    */
    public char peek(int i){
        return file.charAt(fingerIndex+i);
    }

    /**
    * The peekString() method. 
    * @param i How far ahead the method will look.
    * @return A string of the i characters in the file after the current index.
    * Returns a string of the next “i” characters but doesn’t move the index
    */
    public String peekString(int i){
        return file.substring(fingerIndex, fingerIndex+i);
    }

    /**
     * The getChar() method.
     * @return The next char
     * Returns the next character and moves the index
     */
    public char getChar(){
        char output = file.charAt(fingerIndex+1);
        fingerIndex++;
        return output;
    }

    /**
     * The swallow method.
     * @param i How far the index moves
     * Moves the index ahead “i” positions
     */
    public void swallow(int i) throws ArrayIndexOutOfBoundsException{
        if (i >=0)
            fingerIndex += i;
        else 
            throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * The isDone() method.
     * @return True if the index is at the end of the document, false if not.
     * Returns true if we are at the end of the document.
     */
    public boolean isDone(){
        return (file.length() > fingerIndex);
    }

    /**
     * The remainder() method.
     * @return A string of the rest of the document.
     * Returns the rest of the document as a string
     */
    public String remainder() {
        return file.substring(fingerIndex);
    }

    /**
     * The getFingerIndex() method.
     * @return The current index of the finger.
     * This is just for testing pourposes. 
     */
    public int getFingerIndex(){
        return fingerIndex;
    }
}

