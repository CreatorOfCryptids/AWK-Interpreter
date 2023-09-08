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
        // Make sure the index is not beond the end of the string.
        if((currentIndex+i < fileData.length()))
            return fileData.charAt(currentIndex+i);
        else
            return ' ';
    }

    /**
     * The peek() overloaded method.
     * Yes, I am not ashamed to admit that I made a whole extra method to get out of typing one single extra letter. Judge me all you want, but it works.
     * @return The next character
     */
    public char peek(){
        // Make sure the index is not beond the end of the string.
        if((currentIndex < fileData.length()))
            return fileData.charAt(currentIndex);
        else
            return ' ';
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
        //log(output);
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
        //log(fileData.substring(currentIndex-i, currentIndex));
    }

    /**
     * The isDone() method.
     * @return True if the index is at the end of the document. False if not.
     */
    public boolean isDone(){
        return (fileData.length() <= currentIndex);
    }

    /**
     * The remainder() method.
     * @return The rest of the input file as a single string.
     */
    public String remainder() {
        return fileData.substring(currentIndex);
    }

    /**
     * The remaingin
     * @return the amount of remaining character in the string
     */
    public int remaining(){
        return fileData.length() - currentIndex;
    }

    /**
     * Shhhhh! This is just for testing pourposes. 
     * The getCurrentIndex() method.
     * @return The current index of the finger.
     */
    public int getCurrentIndex(){
        return currentIndex;
    }/**/

    public void log(String log){
        System.out.print(log);
    }

    public void log(char log){
        System.out.print(log);
    }
}

