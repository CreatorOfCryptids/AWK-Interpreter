//import java.util.*;

public class StringHandler {

    private String fileData = "";
    private int currentIndex = 0;

    /**
     * Constructor
     * @param file The sting containing the information in the file.
     */
    StringHandler(String file){
        this.fileData = file;
    }

    /**
    * The peek() method.
    * @param i How far ahead the method looks.
    * @return The char i characters ahead of the index.
    * Looks “i” characters ahead and returns that character; doesn’t move the index.
    */
    public char peek(int i){
        return fileData.charAt(currentIndex+i);
    }

    /**
     * The peek() overloaded method.
     * Yes, I am not ashamed to admit that I made a whole extra method to get out of typing one single extra letter. Judge me all you want, but it works.
     * @return The next character
     */
    public char peek(){
        return fileData.charAt(currentIndex);
    }

    /**
    * The peekString() method. 
    * @param i How far ahead the method will look.
    * @return A string of the i characters in the file after the current index.
    * Returns a string of the next “i” characters but doesn’t move the index
    */
    public String peekString(int i){
        return fileData.substring(currentIndex, currentIndex+i);
    }

    /**
     * The getChar() method.
     * @return The next char
     * Returns the next character and moves the index
     */
    public char getChar(){
        char output = fileData.charAt(currentIndex);
        currentIndex++;
        return output;
    }

    /**
     * The swallow method.
     * @param i How far the index moves
     * Moves the index ahead “i” positions
     */
    public void swallow(int i) throws ArrayIndexOutOfBoundsException{
        if (i >=0)
            currentIndex += i;
        else 
            throw new ArrayIndexOutOfBoundsException();
    }

    /**
     * The isDone() method.
     * @return True if the index is at the end of the document, false if not.
     * Returns true if we are at the end of the document.
     */
    public boolean isDone(){
        return (fileData.length() <= currentIndex);
    }

    /**
     * The remainder() method.
     * @return A string of the rest of the document.
     * Returns the rest of the document as a string.
     */
    public String remainder() {
        return fileData.substring(currentIndex);
    }

    /**
     * The getFingerIndex() method.
     * @return The current index of the finger.
     * Shhhhh! This is just for testing pourposes. 
     *
    public int getFingerIndex(){
        return fingerIndex;
    }/**/
}

