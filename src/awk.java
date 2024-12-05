import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import interpreter.Interpreter;
import lexer.Lexer;
import parser.Parser;

public class awk {
    public static void main(String[] args) throws Exception {
        // Choose file.
        String programFileName;
        String inputFileName;
        if (args.length > 0)
            programFileName = args[0];
        else
            // fileName = "test2.txt";
            programFileName = "testAWK1.awk";

        if (args.length > 1)
            if (args[1].equals("-IN")) {
                inputFileName = "\\INLINE\\";
            } else {
                inputFileName = args[1];
            }
        else
            inputFileName = "";

        // Open file and pass to the lexer.
        Path myPath = Paths.get(programFileName);
        String file = new String(Files.readAllBytes(myPath));

        Lexer lex = new Lexer(file);

        Parser parser = new Parser(lex.lex());

        Interpreter interpreter = new Interpreter(parser.parse(), inputFileName);

        interpreter.interpretProgram();
    }
}
