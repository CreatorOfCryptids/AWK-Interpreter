import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class awk {
    public static void main(String[] args) throws Exception{
        //Choose file.
        String programFileName;
        /*if (args != null && args[0] != null)
            fileName = args[0];
        else*/
            //fileName = "test2.txt";
            programFileName = "Example2.awk";

        String examinedFile = "";

        // Open file and pass to the lexer.
        Path myPath = Paths.get(programFileName);
        String file = new String(Files.readAllBytes(myPath));
        Lexer lex = new Lexer(file);

        // Lex.
        LinkedList<Token> list = lex.lex();

        //* Parse
        Parser parser = new Parser(list);
        ProgramNode program = parser.parse();

        //Interpret
        Interpreter interpreter = new Interpreter(program, examinedFile);
        //interpreter.interpret();

        //Print the tokens.
        for(Token t: list){
            System.out.print(t.toString() + " ");
        }

        System.out.println("\n\n" + program.toString());

        System.out.println();
    }
}
