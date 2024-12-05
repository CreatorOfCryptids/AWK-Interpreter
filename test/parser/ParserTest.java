package test.parser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import lexer.Lexer;
import lexer.Token;
import parser.Parser;
import parser.ast.*;

public class ParserTest {

    String testParse = "function add_three (number) {\nreturn number + 3\n}\nBEGIN {\nprint(\"Hello, world!\")\n}\n{\nprint(add_three(36))     # Outputs '''39'''\n}\nEND {\nprint(\"Goodbye\")\n}";

    @Test
    public void parceFunction() throws Exception {
        String input = "function tester (a, c, \n" + //
                " d) \n" + //
                " { a= \"banana\" \"fish\"\n;}\n" + //
                " function testy (no, yes, maybe) {true(); false();}";

        // String output = "function tester (a, c, d, ) { NULL STATEMENTS\n}\nfunction
        // testy (no, yes, maybe, ) { NULL STATEMENTS\n}\n";
        Lexer lex = new Lexer(input);
        Parser parse = new Parser(lex.lex());
        ProgramNode pNode = parse.parse();
        Assert.assertEquals("function tester (a, c, d, ) { a=\"banana\"\n" + //
                "}\n" + //
                "function testy (no, yes, maybe, ) { true()\n" + //
                "false()\n" + //
                "}\n" + //
                "", pNode.toString());
    }

    @Test
    public void parceAction() throws Exception {
        String input = "BEGIN {}";
        Lexer lex = new Lexer(input);
        Parser parse = new Parser(lex.lex());
        ProgramNode pNode = parse.parse();
        Assert.assertEquals("BEGIN { NULL STATEMENT\n}\n", pNode.toString());
    }

    @Test
    public void parce() throws Exception {
        Lexer lex = new Lexer(testParse);
        Parser testParse = new Parser(lex.lex());
        ProgramNode testNode = testParse.parse();
        Assert.assertEquals("function add_three (number, ) { return (number+\"3\");\n" + //
                "}\n" + //
                "BEGIN { print(\"Hello, world!\",)\n" + //
                "}\n" + //
                "{ print(add_three(\"36\",),)\n" + //
                "}\n" + //
                "END { print(\"Goodbye\",)\n" + //
                "}\n" + //
                "", testNode.toString());
    }

    /*
     * Test for accept Seperators
     * 
     * @Test
     * public void PAR_acceptSeperators() throws Exception {
     * Lexer lexer = new
     * Lexer("This is a test ;;;;;;;;;;;;; hi \n\n\n\n are ;;\n these words accepted?"
     * );
     * TokenHandler testTH = new TokenHandler(lexer.lex());
     * for (int i =0; i<4; i++){
     * Assert.assertFalse(testTH.acceptSeperators());
     * testTH.matchAndRemove(Token.Type.WORD);
     * }
     * Assert.assertTrue(testTH.acceptSeperators());
     * Assert.assertEquals("WORD(hi)",
     * testTH.matchAndRemove(Token.Type.WORD).get().toString());
     * Assert.assertTrue(testTH.acceptSeperators());
     * Assert.assertEquals("WORD(are)",
     * testTH.matchAndRemove(Token.Type.WORD).get().toString());
     * Assert.assertTrue(testTH.acceptSeperators());
     * Assert.assertEquals("WORD(these)",
     * testTH.matchAndRemove(Token.Type.WORD).get().toString());
     * testTH.matchAndRemove(Token.Type.WORD);
     * testTH.matchAndRemove(Token.Type.WORD);
     * Assert.assertFalse(testTH.acceptSeperators());
     * }
     */

    /*
     * Test for the first part of parseOperation(). parseOperation() needs to be
     * public to work. Has already been tested :)
     * 
     * @Test
     * public void PAR_parseOperation1() throws Exception {
     * Lexer lex = new Lexer("++a ++$b (++d) -5 `[abc]` e[++b] $7");
     * Parser parse = new Parser(lex.lex());
     * Assert.assertEquals("++a", parse.TEST_parseOperation().get().toString());
     * Assert.assertEquals("++$b", parse.TEST_parseOperation().get().toString());
     * Assert.assertEquals("++d", parse.TEST_parseOperation().get().toString());
     * Assert.assertEquals("-5", parse.TEST_parseOperation().get().toString());
     * Assert.assertEquals("`[abc]`", parse.TEST_parseOperation().get().toString());
     * Assert.assertEquals("e[++b]", parse.TEST_parseOperation().get().toString());
     * }/
     **/

    // * Test for the second part of parseOperation(). It is no longer public so
    // this test doesn't work any more.
    @Test
    public void parseOperation2() throws Exception {
        String[] tests = { "(2)", "$2", "++preinc", "--predec", "!expr", "+expr", "-expr", "a * b", "a/b", "a%b",
                "a+b", "a-b", "a b", "\"Hello, \" \"World!\"", "a < 1", "a<=b", "a == b", "a!=b",
                "a>b", "a>=b", "a ~ b", "a!~b", "a[2]", "a[b]", "a && b", "a||b", "a && b || c", "a ? b : c",
                "a ^= b", "a%=b", "a*=b", "a/=b", "a+=b", "a-=b", "a=b", "a ^ b", "a^b^c", "a + b + c",
                "z = (a+b)-(c*d)/e + f^g^h" };
        String[] results = { "\"2\"", "($\"2\")", "(++preinc)", "(--predec)", "(!expr)", "(+expr)", "(-expr)", "(a*b)",
                "(a/b)", "(a%b)",
                "(a+b)", "(a-b)", "(a cat b)", "(\"Hello, \" cat \"World!\")", "(a<\"1\")", "(a<=b)", "(a==b)",
                "(a!=b)",
                "(a>b)", "(a>=b)", "(a~b)", "(a!~b)", "a[\"2\"]", "a[b]", "(a&&b)", "(a||b)", "((a&&b)||c)",
                "(a ? b : c)",
                "a=(a^b)", "a=(a%b)", "a=(a*b)", "a=(a/b)", "a=(a+b)", "a=(a-b)", "a=b", "(a^b)", "(a^(b^c))",
                "((a+b)+c)",
                "z=(((a+b)-((c*d)/e))+(f^(g^h)))" };
        Lexer lex;
        Parser par;
        Node test;

        for (int i = 0; i < tests.length; i++) {
            lex = new Lexer(tests[i]);
            par = new Parser(lex.lex());
            test = par.TEST_parseOperation().get();
            Assert.assertEquals(results[i], test.toString());
        }
    }/**/

    @Test
    public void parse2() throws Exception {
        Lexer lex = new Lexer(
                "BEGIN {a = \"Data:\"; count = 0;} $1 == \"b\" {count++; funcky($3)} function funky (b){ return b + ($2 ? 2 :3);} END {printable = a + count}");
        Parser test = new Parser(lex.lex());
        Assert.assertEquals("function funky (b, ) { return (b+(($\"2\") ? \"2\" : \"3\"));\n" + //
                "}\n" + //
                "BEGIN { a=\"Data:\"\n" + //
                "count=\"0\"\n" + //
                "}\n" + //
                "(($\"1\")==\"b\"){ (count++)\n" + //
                "funcky(($\"3\"),)\n" + //
                "}\n" + //
                "END { printable=(a+count)\n" + //
                "}\n" + //
                "", test.parse().toString());
    }

    @Test
    public void parse3() throws Exception {
        Lexer lexy = new Lexer("BEGIN {\n" + //
                "\tprint \"Hello, world!\"\n" + //
                "\texit\n" + //
                "}\n" + //
                "");
        Parser test = new Parser(lexy.lex());
        Assert.assertEquals("BEGIN { print(\"Hello, world!\",)\nexit()\n}\n", test.parse().toString());

        String programFileName = "testAWK1.awk";

        Path myPath = Paths.get(programFileName);
        String file = new String(Files.readAllBytes(myPath));

        Lexer lex = new Lexer(file);
        LinkedList<Token> list = lex.lex();

        Parser parser = new Parser(list);
        ProgramNode program = parser.parse();

        Assert.assertEquals("BEGIN { print(\"Hello, world!\n\",)\n}\n", program.toString());
    }

    @Test
    public void parseIf() throws Exception {
        String input = "BEGIN{if(1+1==2){doThings()}else if(44>3) doOtherThings() else{doThing(); if(0) doThingz()}}";
        String output = "BEGIN { if (((\"1\"+\"1\")==\"2\")){\ndoThings();\n}else if ((\"44\">\"3\")){\ndoOtherThings();\n}else {\ndoThing();\nif (\"0\"){\ndoThingz();\n};\n}\n}\n";
        Lexer lex = new Lexer(input);
        Parser test = new Parser(lex.lex());
        Assert.assertEquals(output, test.parse().toString());
    }

    @Test
    public void parseFor() throws Exception {
        Lexer lex = new Lexer("BEGIN{for(i=1; i<3; i++) print \"Thing \" i} END {for(thing in things){ print(thing)}}");
        Parser test = new Parser(lex.lex());
        Assert.assertEquals("BEGIN { for(i=\"1\"; (i<\"3\"); (i++)){\n" + //
                "print((\"Thing \" cat i),);\n" + //
                "}\n" + //
                "}\n" + //
                "END { for (thing in things){\n" + //
                "print(thing,);\n" + //
                "}\n" + //
                "}\n" + //
                "", test.parse().toString());
    }

    @Test
    public void parseWhile() throws Exception {
        Lexer lex = new Lexer("BEGIN {while(1) print(\"f\")}");
        Parser test = new Parser(lex.lex());
        Assert.assertEquals("BEGIN { while (\"1\"){\n" + //
                "print(\"f\",);\n" + //
                "}\n" + //
                "}\n", test.parse().toString());
    }

    @Test
    public void parseDoWhile() throws Exception {
        Lexer lex = new Lexer("END {do{things()}while (0>=-4)}");
        Parser test = new Parser(lex.lex());
        Assert.assertEquals("END { do {\n" + //
                "things();\n" + //
                "} while ((\"0\">=(-\"4\")))\n" + //
                "}\n", test.parse().toString());
    }

    @Test
    public void parseReturnDelete() throws Exception {
        Lexer lex = new Lexer("function foo(){return 59;} END {delete things;}");
        Parser test = new Parser(lex.lex());
        Assert.assertEquals("function foo () { return \"59\";\n" + //
                "}\n" + //
                "END { delete things\n" + //
                "}\n", test.parse().toString());
    }

    @Test
    public void parseBreakContinue() throws Exception {
        Lexer lex = new Lexer("for(i=1; i<3; i++){if(i==1) continue; else if(i==3) break;}");
        Parser test = new Parser(lex.lex());
        Assert.assertEquals("{ for(i=\"1\"; (i<\"3\"); (i++)){\n" + //
                "if ((i==\"1\")){\n" + //
                "continue;;\n" + //
                "}else if ((i==\"3\")){\n" + //
                "break;;\n" + //
                "};\n" + //
                "}\n" + //
                "}\n", test.parse().toString());
    }

    @Test
    public void parseFunctionCall() throws Exception {
        Lexer lex = new Lexer("BEGIN {\n\tprint \"Hello, world!\"\nexit}\n");
        Parser test = new Parser(lex.lex());

        Assert.assertEquals("BEGIN { print(\"Hello, world!\",)\nexit()\n}\n", test.parse().toString());
    }
}
