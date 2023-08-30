import java.util.*;

public class awk {
    public static void main(String[] args) throws Exception{
        String fileName = "test1.txt";
        Lexer lex = new Lexer(fileName);
        LinkedList<Token> list = lex.lex();

        for(Token t: list){
            System.out.print(t.toString() + " ");
        }
    }
}
