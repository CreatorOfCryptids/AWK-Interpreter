import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class awk {
    public static void main(String[] args) throws Exception{
        //Choose file.
        String fileName = "test1.txt";

        // Open file and pass to the lexer.
        Path myPath = Paths.get(fileName);
        String file = new String(Files.readAllBytes(myPath));
        Lexer lex = new Lexer(file);

        // Lex.
        LinkedList<Token> list = lex.lex();

        //Print the tokens.
        for(Token t: list){
            System.out.print(t.toString());
        }
        System.out.println();
    }
}
