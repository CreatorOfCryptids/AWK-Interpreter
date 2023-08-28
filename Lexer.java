import java.util.*;

public class Lexer {
    
    public void Lex(String fileName){
        
        StringHandler handler = new StringHandler(fileName);
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
}
