import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Lexer {

    private StringHandler handler;
    private int lineNumber;
    private int position;
    private LinkedList<Token> tokenList = new LinkedList<Token>();

    public Lexer(String fileName) throws IOException {
        Path myPath = Paths.get(fileName);
        String file = new String(Files.readAllBytes(myPath));
        handler = new StringHandler(file);
        lineNumber = 1;
        position = 0;
    }
    
    public LinkedList<Token> lex(){
        while (!handler.isDone()){
            char c = handler.peek();
            if (c == ' ' || c == '\t' || c == '\r'){
                handler.swallow(1);
            }
            else if (c == '\n'){
                tokenList.add(new Token(Token.Type.SEPERATOR, null, lineNumber, position));
                lineNumber++;
                position = 0;
                handler.swallow(1);
            }
            else if (Character.isDigit(c) || c == '.'){
                tokenList.add(processNumber());
            }
            else if (Character.isAlphabetic(c)){
                tokenList.add(processWord());
            }
            else{
                System.out.println("The unrecognised character \"" + c + "\" appeared in Line: " + lineNumber + " Position: " + position + ".");
                break;
            }
        }

        return tokenList;
    }

    private Token processWord() {
        String word = "";
        char c = handler.peek();
        int wordStart = position;

        while (!handler.isDone() && (Character.isAlphabetic(c) || Character.isDigit(c))){
            word += handler.getChar();
            position++;
            try{
                c = handler.peek();
            }
            catch (Exception StringIndexOutOfBoundsException){
                break;
            }
        }

        Token wordToken = new Token(Token.Type.WORD, word, lineNumber, wordStart);
        return wordToken;
    }

    private Token processNumber(){
        boolean foundPeriod = false;
        String number = "";
        char c = handler.peek();
        int numberStart = position;

        while (!handler.isDone() && (Character.isDigit(c)) || (c == '.' && !foundPeriod)){
            if (c == '.') 
                foundPeriod = true;
            number += handler.getChar();
            try{
                c = handler.peek();
            }
            catch (Exception StringIndexOutOfBoundsException){
                break;
            }
            position++;
        }

        Token numberToken = new Token(Token.Type.NUMBER, number, lineNumber, numberStart);
        return numberToken;
    }
}
