package test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.junit.Test;

import lexer.*;
import parser.Parser;
import parser.ast.*;
import interpreter.Interpreter;

public class awkTest {

    @Test
    public void FINAL_HelloWorld() throws Exception {
        // Select files
        String programFileName = "testAWK1.awk";
        String inputFileName = "test1.txt";

        // Open file and pass to the lexer.
        Path myPath = Paths.get(programFileName);
        String file = new String(Files.readAllBytes(myPath));
        Lexer lex = new Lexer(file);

        // Lex.
        LinkedList<Token> list = lex.lex();

        // * Parse
        Parser parser = new Parser(list);
        ProgramNode program = parser.parse();

        // Interpret
        Interpreter interpreter = new Interpreter(program, inputFileName);
        interpreter.interpretProgram();
    }

    @Test
    public void FINAL_MathCheck() throws Exception {
        // Select files
        String programFileName = "testAWK2.awk";
        String inputFileName = "test2.txt";

        // Open file and pass to the lexer.
        Path myPath = Paths.get(programFileName);
        String file = new String(Files.readAllBytes(myPath));
        Lexer lex = new Lexer(file);

        // Lex.
        LinkedList<Token> list = lex.lex();

        // * Parse
        Parser parser = new Parser(list);
        ProgramNode program = parser.parse();

        // Interpret
        Interpreter interpreter = new Interpreter(program, inputFileName);
        interpreter.interpretProgram();
    }

    @Test
    public void FINAL_InputCheck() throws Exception {
        // Select files
        String programFileName = "testAWK3.awk";
        String inputFileName = "test3.txt";

        // Open file and pass to the lexer.
        Path myPath = Paths.get(programFileName);
        String file = new String(Files.readAllBytes(myPath));
        Lexer lex = new Lexer(file);

        // Lex.
        LinkedList<Token> list = lex.lex();

        // * Parse
        Parser parser = new Parser(list);
        ProgramNode program = parser.parse();

        // Interpret
        Interpreter interpreter = new Interpreter(program, inputFileName);
        interpreter.interpretProgram();
    }

    @Test
    public void FINAL_ConditionalCheck() throws Exception {
        // Select files
        String programFileName = "testAWK4.awk";
        String inputFileName = "";

        // Open file and pass to the lexer.
        Path myPath = Paths.get(programFileName);
        String file = new String(Files.readAllBytes(myPath));
        Lexer lex = new Lexer(file);

        // Lex.
        LinkedList<Token> list = lex.lex();

        // * Parse
        Parser parser = new Parser(list);
        ProgramNode program = parser.parse();

        // Interpret
        Interpreter interpreter = new Interpreter(program, inputFileName);
        interpreter.interpretProgram();
    }

    @Test
    public void FINAL_FunctionsCheck() throws Exception {
        // Select files
        String programFileName = "testAWK5.awk";
        String inputFileName = "";

        // Open file and pass to the lexer.
        Path myPath = Paths.get(programFileName);
        String file = new String(Files.readAllBytes(myPath));
        Lexer lex = new Lexer(file);

        // Lex.
        LinkedList<Token> list = lex.lex();

        // * Parse
        Parser parser = new Parser(list);
        ProgramNode program = parser.parse();

        // Interpret
        Interpreter interpreter = new Interpreter(program, inputFileName);
        interpreter.interpretProgram();
    }

    @Test
    public void FINAL_LoopsBröther() throws Exception {
        // Select files
        String programFileName = "testAWK6.awk";
        String inputFileName = "";

        // Open file and pass to the lexer.
        Path myPath = Paths.get(programFileName);
        String file = new String(Files.readAllBytes(myPath));
        Lexer lex = new Lexer(file);

        // Lex.
        LinkedList<Token> list = lex.lex();

        // * Parse
        Parser parser = new Parser(list);
        ProgramNode program = parser.parse();

        // Interpret
        Interpreter interpreter = new Interpreter(program, inputFileName);
        interpreter.interpretProgram();
    }
}
