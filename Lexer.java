import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Lexer {
    
    public void Lex(String fileName) throws IOException{

        Path myPath = Paths.get(fileName);
        String file = new String(Files.readAllBytes(myPath));
        StringHandler handler = new StringHandler(file);
        
        LinkedList<Token> tokenList = new LinkedList<Token>();
        int lineNumber = 1;
        int position = 0;
        String currentToken = "";

        while (handler.isDone()){

            char next = handler.peek(1);
            
            if (next == '\\'){

            }
            if(next == ' '){

            }
        }
    }

    public Token processWord(){
        Token token = new Token();
        return token;
    }

    public Token processNumber(){
        Token token = new Token();
        return token;
    }
}
