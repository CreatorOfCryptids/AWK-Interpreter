import org.junit.Assert;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;
import org.junit.Test;

public class UnitTests {

    String fileName1 = "text1.txt";
    String testString1 = 
    "TestAlphabeticWord Tes5tW0rdsW1thNum8ers testlowercase TESTUPPPERCASE\n" +
    "WhatAboutA2ndLine AreY0uSur3 areyoureallysure TOTALYPOSITIVE\n" +
    "3 14 159 265359 2.718281828459045 127.0.0.1\n" +
    "WordsThen 1039257.3\n"+
    "8792305 ThenWords";
    String fullDesiredOutput1 = 
    "WORD(TestAlphabeticWord)WORD(Tes5tW0rdsW1thNum8ers)WORD(testlowercase)WORD(TESTUPPPERCASE)SEPERATORWORD(WhatAboutA2ndLine)" + 
    "WORD(AreY0uSur3)WORD(areyoureallysure)WORD(TOTALYPOSITIVE)SEPERATORNUMBER(3)NUMBER(14)NUMBER(159)NUMBER(265359)NUMBER(2.718281828459045)NUMBER(127.0)" + 
    "NUMBER(.0)NUMBER(.1)SEPERATORWORD(WordsThen)NUMBER(1039257.3)SEPERATORNUMBER(8792305)WORD(ThenWords)";
    String testString2 = 
    "test for test while hello test do test break examin if whatabout continue tryan else butwhatif reurn andwecantforgetabout BEGIN waitand END\n" +
    "butwhatifthekeywordsaretogether print printf next in delete getline exit nextfile function\n" + 
    "\"What about a string literal?\" \"does it recognize \\\"ESCAPESEPTION!?!?!\\\"\" test andIShouldnotforget the\"\" `*patern*`";
    String desiredOutput2 = "";
    String testParse = 
            "function add_three (number) {\nreturn number + 3\n}\nBEGIN {\nprint(\"Hello, world!\")\n}\n{\nprint(add_three(36))     # Outputs '''39'''\n}\nEND {\nprint(\"Goodbye\")\n}";
    String testTH = "test for test while hello test do test break examin if whatabout continue tryAn else butwhatif return andwecantforgetabout BEGIN waitand END\nbutwhatifthekeywordsaretogether print printf next in delete getline exit nextfile function\n\"What about a string literal?\" \"does it recognize \\\"ESCAPESEPTION!?!?!\\\"\" test andIShouldNotforget the \"\" `*patern*`\nI am going to try to string literal \"Is it working???\" banana ; fish \"What about \\\"NOW?\\\"\" What if I just \"\" \n>= ++ -- <= == != ^= %= *= 3/=4 += -= !~ && >> whatAboutACurveBall ||\n{ } [ ] ( ) $ ~ = < > ! + ^ - ? :test * / % ; curveBall | ,\n`test` `124` `next to``eachother` `` banananana`patterns|with&symbols`";

    String fullAWKTest1 = "";
    String fullAWKTest2 = "";
    String fullAWKTest3 = "";

    
    @Test 
    public void SH_peek() throws Exception{
        StringHandler testHandler = new StringHandler(testString1);

        //loops thru each and every character in the thing to see if it works.
        for (int i = 0; i<testString1.length(); i++){
            Assert.assertEquals(testString1.charAt(i), testHandler.peek(i));
            Assert.assertEquals(0, testHandler.getCurrentIndex());
        }

        testHandler.swallow(19); // Iterates the finger
        Assert.assertEquals(19, testHandler.getCurrentIndex());
        // Checks if it works when the finger index is greater than 0.
        for(int i=0; i<(testString1.length()-19); i++)
            Assert.assertEquals(testString1.charAt(i+19), testHandler.peek(i));
        // Checks that the finger isn't moved.
        Assert.assertEquals(19, testHandler.getCurrentIndex());
    }

    @Test
    public void SH_peekString() throws Exception {
        StringHandler testHandler = new StringHandler(testString1);

        Assert.assertEquals("", testHandler.peekString(0));
        Assert.assertEquals("Test", testHandler.peekString(4));
        Assert.assertEquals("TestAlphabeticWord", testHandler.peekString(18));
        testHandler.swallow(70);
        Assert.assertEquals("WhatAboutA2ndLine", testHandler.peekString(17));
        testHandler.swallow(46);
        Assert.assertEquals("TOTALYPOSITIVE\n" + 
                "3 14 159", testHandler.peekString(23));
    }

    @Test
    public void SH_getChar() throws Exception {
        StringHandler testHandler = new StringHandler(testString1);
        Assert.assertEquals(0, testHandler.getCurrentIndex());
        Assert.assertEquals('T', testHandler.getChar());
        Assert.assertEquals(1, testHandler.getCurrentIndex());
        for (int i=1; i<testString1.length(); i++){
            Assert.assertEquals(testString1.charAt(i), testHandler.getChar());
        }

    }

    @Test
    public void SH_swallow() throws Exception {
        StringHandler testHandler = new StringHandler(testString1);

        
        Assert.assertEquals(0, testHandler.getCurrentIndex());
        testHandler.swallow(0);
        Assert.assertEquals(0, testHandler.getCurrentIndex());
        testHandler.swallow(10);
        Assert.assertEquals(10, testHandler.getCurrentIndex());
        testHandler.swallow(90);
        Assert.assertEquals(100, testHandler.getCurrentIndex());
    }

    @Test
    public void SH_isDone() throws Exception {
        StringHandler testHandler = new StringHandler(testString1);

        String output = "";
        while(!testHandler.isDone()){
            output += testHandler.getChar();
        }
        Assert.assertEquals(testString1, output);
    }

    @Test
    public void SH_remainder() throws Exception {
        StringHandler testHandler = new StringHandler(testString1);

        Assert.assertEquals(testString1, testHandler.remainder());
        testHandler.swallow(99);
        Assert.assertEquals(testString1.substring(99), testHandler.remainder());
        testHandler.swallow(96);
        Assert.assertEquals("8792305 ThenWords", testHandler.remainder());
    }

    @Test
    public void LEX1() throws Exception{
        Lexer testLex = new Lexer(testString1);
        String output = "";
        for (Token t : testLex.lex()){
            output += t.toString();
        }
        Assert.assertEquals(fullDesiredOutput1, output);
    }

    @Test
    public void LEX_processNumber() throws Exception {
        Lexer testLex = new Lexer("1 23 456 7.89 10.1112 .1314 .15.16");
        LinkedList<Token> output = testLex.lex();
        Assert.assertEquals(output.get(0).toString(), "NUMBER(1)");
        Assert.assertEquals(output.get(1).toString(), "NUMBER(23)");
        Assert.assertEquals(output.get(2).toString(), "NUMBER(456)");
        Assert.assertEquals(output.get(3).toString(), "NUMBER(7.89)");
        Assert.assertEquals(output.get(4).toString(), "NUMBER(10.1112)");
        Assert.assertEquals(output.get(5).toString(), "NUMBER(.1314)");
        Assert.assertEquals(output.get(6).toString(), "NUMBER(.15)");
        Assert.assertEquals(output.get(7).toString(), "NUMBER(.16)");

    }

    @Test
    public void LEX_processWord() throws Exception {
        Lexer testLex = new Lexer("lowercaseword PascalCaseWord camalCaseWord UPPERCASEWORD w0rdW1thNum8ber5 word_with_underscores");
        LinkedList<Token> output = testLex.lex();
        Assert.assertEquals(output.get(0).toString(), "WORD(lowercaseword)");
        Assert.assertEquals(output.get(1).toString(), "WORD(PascalCaseWord)");
        Assert.assertEquals(output.get(2).toString(), "WORD(camalCaseWord)");
        Assert.assertEquals(output.get(3).toString(), "WORD(UPPERCASEWORD)");
        Assert.assertEquals(output.get(4).toString(), "WORD(w0rdW1thNum8ber5)");
        Assert.assertEquals(output.get(5).toString(), "WORD(word_with_underscores)");
    }

    @Test
    public void LEX_processStringLiteral() throws Exception{
        Lexer  testLex = new Lexer("I am going to try to string literal \"Is it working???\" banana \"What about \\\"NOW?\\\"\" What if I just \"\"");
        LinkedList<Token> output = testLex.lex();
        Assert.assertEquals("STRINGLITERAL(Is it working???)", output.get(8).toString());
        Assert.assertEquals("STRINGLITERAL(What about \"NOW?\")", output.get(10).toString());
        Assert.assertEquals("STRINGLITERAL()", output.get(15).toString());
    }

    @Test
    public void LEX_processDoubleCharacters() throws Exception{
        Lexer testLex = new Lexer(">= ++ -- <= == != ^= %= *= 3/=4 += -= !~ && >> whatAboutACurveBall ||");
        LinkedList<Token> output = testLex.lex();
        Assert.assertEquals(output.get(0).toString(), "GREATEREQUALS");
        Assert.assertEquals(output.get(1).toString(), "PLUSPLUS");
        Assert.assertEquals(output.get(2).toString(), "MINUSMINUS");
        Assert.assertEquals(output.get(3).toString(), "LESSEQUALS");
        Assert.assertEquals(output.get(4).toString(), "EQUALSEQUALS");
        Assert.assertEquals(output.get(5).toString(), "NOTEQUALS");
        Assert.assertEquals(output.get(6).toString(), "EXPONENTEQUALS");
        Assert.assertEquals(output.get(7).toString(), "MODEQUALS");
        Assert.assertEquals(output.get(8).toString(), "TIMESEQUALS");
        Assert.assertEquals(output.get(10).toString(), "DIVIDEEQUALS");
        Assert.assertEquals(output.get(12).toString(), "PLUSEQUALS");
        Assert.assertEquals(output.get(13).toString(), "MINUSEQUALS");
        Assert.assertEquals(output.get(14).toString(), "NOTMATCH");
        Assert.assertEquals(output.get(15).toString(), "AND");
        Assert.assertEquals(output.get(16).toString(), "APPEND");
        Assert.assertEquals(output.get(18).toString(), "OR");
    }

    @Test
    public void LEX_processSingleChar() throws Exception{
        Lexer testLex = new Lexer("{ } [ ] ( ) $ ~ = < > ! + ^ - ? :test * / % ; \n curveBall | ,");
        LinkedList<Token> output = testLex.lex();
        Assert.assertEquals(output.get(0).toString(), "LCURLY");
        Assert.assertEquals(output.get(1).toString(), "RCURLY");
        Assert.assertEquals(output.get(2).toString(), "LSQUARE");
        Assert.assertEquals(output.get(3).toString(), "RSQUARE");
        Assert.assertEquals(output.get(4).toString(), "LPAREN");
        Assert.assertEquals(output.get(5).toString(), "RPAREN");
        Assert.assertEquals(output.get(6).toString(), "DOLLAR");
        Assert.assertEquals(output.get(7).toString(), "MATCH");
        Assert.assertEquals(output.get(8).toString(), "EQUALS");
        Assert.assertEquals(output.get(9).toString(), "LESS");
        Assert.assertEquals(output.get(10).toString(), "GREATER");
        Assert.assertEquals(output.get(11).toString(), "NOT");
        Assert.assertEquals(output.get(12).toString(), "PLUS");
        Assert.assertEquals(output.get(13).toString(), "EXPONENT");
        Assert.assertEquals(output.get(14).toString(), "MINUS");
        Assert.assertEquals(output.get(15).toString(), "QUESTIONMARK");
        Assert.assertEquals(output.get(16).toString(), "COLON");
        Assert.assertEquals(output.get(18).toString(), "ASTRIC");
        Assert.assertEquals(output.get(19).toString(), "SLASH");
        Assert.assertEquals(output.get(20).toString(), "MOD");
        Assert.assertEquals(output.get(21).toString(), "SEPERATOR");
        Assert.assertEquals(output.get(22).toString(), "SEPERATOR");
        Assert.assertEquals(output.get(24).toString(), "BAR");
        Assert.assertEquals(output.get(25).toString(), "COMMA");
    }

    @Test
    public void LEX_processPattern() throws Exception{
        Lexer testLex = new Lexer("`test` `124` `next to``eachother` `` banananana`patterns|with&symbols`");
        LinkedList<Token> output = testLex.lex();

        Assert.assertEquals(output.get(0).toString(), "PATTERN(test)");
        Assert.assertEquals(output.get(1).toString(), "PATTERN(124)");
        Assert.assertEquals(output.get(2).toString(), "PATTERN(next to)");
        Assert.assertEquals(output.get(3).toString(), "PATTERN(eachother)");
        Assert.assertEquals(output.get(4).toString(), "PATTERN()");
        Assert.assertEquals(output.get(6).toString(), "PATTERN(patterns|with&symbols)");
    }

    @Test
    public void T_toString() throws Exception {
        Token testToken = new Token(Token.Type.WORD, "TestAlphabeticWord", 1, 20);
        Assert.assertEquals("WORD(TestAlphabeticWord)", testToken.toString());
        testToken = new Token(Token.Type.NUMBER, "3.14159", 20, 1);
        Assert.assertEquals("NUMBER(3.14159)", testToken.toString());
        testToken = new Token(Token.Type.SEPERATOR, null, 420, 69);
        Assert.assertEquals("SEPERATOR", testToken.toString());
    }

    @Test
    public void TH_matchAndRemove() throws Exception {
        Lexer lex = new Lexer(testTH);
        TokenHandler testTH = new TokenHandler(lex.lex());

        while(testTH.moreTokens()){
            Assert.assertEquals(testTH.peek(), testTH.matchAndRemove(testTH.peek().get().getType()));
        }
    }

    @Test
    public void TH_moreTokens() throws Exception {
        Lexer lex = new Lexer("if *= number 12");
        TokenHandler testTH = new TokenHandler(lex.lex());

        Assert.assertTrue(testTH.moreTokens());
        testTH.matchAndRemove(Token.Type.IF);
        Assert.assertTrue(testTH.moreTokens());
        testTH.matchAndRemove(Token.Type.TIMESEQUALS);
        Assert.assertTrue(testTH.moreTokens());
        testTH.matchAndRemove(Token.Type.WORD);
        Assert.assertTrue(testTH.moreTokens());
        testTH.matchAndRemove(Token.Type.NUMBER);
        Assert.assertFalse(testTH.moreTokens());
    }

    @Test
    public void TH_peek() throws Exception {
        Lexer lex = new Lexer("if for while banananana 24 \"no\"");
        TokenHandler testTH = new TokenHandler(lex.lex());

        Assert.assertEquals("IF", testTH.peek().get().toString());
        Assert.assertEquals("FOR", testTH.peek(1).get().toString());
        Assert.assertEquals("WHILE", testTH.peek(2).get().toString());
        Assert.assertEquals("WORD(banananana)", testTH.peek(3).get().toString());
        Assert.assertEquals("NUMBER(24)", testTH.peek(4).get().toString());
        Assert.assertEquals("STRINGLITERAL(no)", testTH.peek(5).get().toString());
    }

    @Test
    public void PAR_parceFunction() throws Exception {
        String input = "function tester (a, c, \n" + //
                " d) \n" + //
                " { a= \"banana\" \"fish\"\n;}\n" + //
                " function testy (no, yes, maybe) {true(); false();}";
        
        //String output = "function tester (a, c, d, ) { NULL STATEMENTS\n}\nfunction testy (no, yes, maybe, ) { NULL STATEMENTS\n}\n";
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
    public void PAR_parceAction() throws Exception {
        String input = "BEGIN {}";
        Lexer lex = new Lexer(input);
        Parser parse = new Parser(lex.lex());
        ProgramNode pNode = parse.parse();
        Assert.assertEquals("BEGIN { NULL STATEMENT\n}\n", pNode.toString());
    }

    @Test
    public void PAR_parce() throws Exception {
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

    /* Test for accept Seperators
    @Test
    public void PAR_acceptSeperators() throws Exception {
        Lexer lexer = new Lexer("This is a test ;;;;;;;;;;;;; hi \n\n\n\n are ;;\n these words accepted?");
        TokenHandler testTH = new TokenHandler(lexer.lex());
        for (int i =0; i<4; i++){
            Assert.assertFalse(testTH.acceptSeperators());
            testTH.matchAndRemove(Token.Type.WORD);
        }
        Assert.assertTrue(testTH.acceptSeperators());
        Assert.assertEquals("WORD(hi)", testTH.matchAndRemove(Token.Type.WORD).get().toString());
        Assert.assertTrue(testTH.acceptSeperators());
        Assert.assertEquals("WORD(are)", testTH.matchAndRemove(Token.Type.WORD).get().toString());
        Assert.assertTrue(testTH.acceptSeperators());
        Assert.assertEquals("WORD(these)", testTH.matchAndRemove(Token.Type.WORD).get().toString());
        testTH.matchAndRemove(Token.Type.WORD);
        testTH.matchAndRemove(Token.Type.WORD);
        Assert.assertFalse(testTH.acceptSeperators());
    }*/

    /* Test for the first part of parseOperation(). parseOperation() needs to be public to work. Has already been tested :)
    @Test
    public void PAR_parseOperation1() throws Exception {
        Lexer lex = new Lexer("++a ++$b (++d) -5 `[abc]` e[++b] $7");
        Parser parse = new Parser(lex.lex());
        Assert.assertEquals("++a", parse.TEST_parseOperation().get().toString());
        Assert.assertEquals("++$b", parse.TEST_parseOperation().get().toString());
        Assert.assertEquals("++d", parse.TEST_parseOperation().get().toString());
        Assert.assertEquals("-5", parse.TEST_parseOperation().get().toString());
        Assert.assertEquals("`[abc]`", parse.TEST_parseOperation().get().toString());
        Assert.assertEquals("e[++b]", parse.TEST_parseOperation().get().toString());
    }/**/

    //* Test for the second part of parseOperation(). It is no longer public so this test doesn't work any more.
    @Test
    public void PAR_parseOperation2() throws Exception {
        String[] tests = {  "(2)",  "$2",      "++preinc",  "--predec",  "!expr",  "+expr",  "-expr",  "a * b","a/b",  "a%b",  
                            "a+b",  "a-b",  "a b",      "\"Hello, \" \"World!\"",      "a < 1",    "a<=b",  "a == b","a!=b",  
                            "a>b",  "a>=b",  "a ~ b","a!~b",  "a[2]",    "a[b]","a && b","a||b",  "a && b || c","a ? b : c",
                            "a ^= b", "a%=b",   "a*=b",   "a/=b",   "a+=b",   "a-=b",   "a=b","a ^ b",    "a^b^c", "a + b + c",
                            "z = (a+b)-(c*d)/e + f^g^h"};
        String[] results = {"\"2\"","($\"2\")","(++preinc)","(--predec)","(!expr)","(+expr)","(-expr)","(a*b)","(a/b)","(a%b)",
                            "(a+b)","(a-b)","(a cat b)","(\"Hello, \" cat \"World!\")","(a<\"1\")","(a<=b)","(a==b)","(a!=b)",
                            "(a>b)","(a>=b)","(a~b)","(a!~b)","a[\"2\"]","a[b]","(a&&b)","(a||b)","((a&&b)||c)","(a ? b : c)",
                            "a=(a^b)","a=(a%b)","a=(a*b)","a=(a/b)","a=(a+b)","a=(a-b)","a=b","(a^b)","(a^(b^c))", "((a+b)+c)",
                            "z=(((a+b)-((c*d)/e))+(f^(g^h)))"};
        Lexer lex;
        Parser par;
        Node test;

        for (int i = 0; i<tests.length; i++){
            lex = new Lexer(tests[i]);
            par = new Parser(lex.lex());
            test = par.TEST_parseOperation().get();
            Assert.assertEquals(results[i], test.toString());
        }
    }/**/

    @Test
    public void PAR_parse2() throws Exception {
        Lexer lex = new Lexer("BEGIN {a = \"Data:\"; count = 0;} $1 == \"b\" {count++; funcky($3)} function funky (b){ return b + ($2 ? 2 :3);} END {printable = a + count}");
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
    public void PAR_parse3() throws Exception {
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
    public void PAR_parseIf() throws Exception{
        String input = "BEGIN{if(1+1==2){doThings()}else if(44>3) doOtherThings() else{doThing(); if(0) doThingz()}}";
        String output = "BEGIN { if (((\"1\"+\"1\")==\"2\")){\ndoThings();\n}else if ((\"44\">\"3\")){\ndoOtherThings();\n}else {\ndoThing();\nif (\"0\"){\ndoThingz();\n};\n}\n}\n";
        Lexer lex = new Lexer(input);
        Parser test = new Parser(lex.lex());
        Assert.assertEquals(output, test.parse().toString());
    }

    @Test
    public void PAR_parseFor() throws Exception{
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
    public void PAR_parseWhile() throws Exception{
        Lexer lex = new Lexer("BEGIN {while(1) print(\"f\")}");
        Parser test = new Parser(lex.lex());
        Assert.assertEquals("BEGIN { while (\"1\"){\n" + //
                "print(\"f\",);\n" + //
                "}\n" + //
                "}\n", test.parse().toString());
    }

    @Test
    public void PAR_parseDoWhile() throws Exception{
        Lexer lex = new Lexer("END {do{things()}while (0>=-4)}");
        Parser test = new Parser(lex.lex());
        Assert.assertEquals("END { do {\n" + //
                "things();\n" + //
                "} while ((\"0\">=(-\"4\")))\n" + //
                "}\n", test.parse().toString());
    }

    @Test
    public void PAR_parseReturnDelete() throws Exception{
        Lexer lex = new Lexer("function foo(){return 59;} END {delete things;}");
        Parser test = new Parser(lex.lex());
        Assert.assertEquals("function foo () { return \"59\";\n" + //
                "}\n" + //
                "END { delete things\n" + //
                "}\n", test.parse().toString());
    }

    @Test
    public void PAR_parseBreakContinue() throws Exception{
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
    public void PAR_parseFunctionCall() throws Exception{
        Lexer lex = new Lexer("BEGIN {\n\tprint \"Hello, world!\"\nexit}\n");
        Parser test = new Parser(lex.lex());

        Assert.assertEquals("BEGIN { print(\"Hello, world!\",)\nexit()\n}\n", test.parse().toString());
    }

    @Test
    public void PNODE_toString() throws Exception {
        ProgramNode testNode = new ProgramNode();
        Assert.assertEquals("", testNode.toString());
        testNode.add(new FunctionDefinitionNode("banana", null, null));
        Assert.assertEquals("function banana () { NULL STATEMENTS\n}\n", testNode.toString());
    }

    @Test
    public void PNODE_add() throws Exception {
        ProgramNode test = new ProgramNode();
        Assert.assertEquals("", test.toString());
        FunctionDefinitionNode func = new FunctionDefinitionNode("funky", null, null);
        test.add(func);
        Assert.assertEquals("function funky () { NULL STATEMENTS\n}\n", test.toString());
        BlockNode begin = new BlockNode(Optional.empty(), null);
        test.addBeginBlock(begin);
        test.addEndBlock(begin);
        test.toString();
        Assert.assertEquals("function funky () { NULL STATEMENTS\n}\nBEGIN { NULL STATEMENT\n}\nEND { NULL STATEMENT\n}\n", test.toString());

    }

    @Test
    public void FNODE_toString() throws Exception {
        String expectedOutcome= "function Name (a, b, c, ) { NULL STATEMENTS\n" +
                                "}";
        LinkedList<String> param = new LinkedList<String>();
        param.add("a");
        param.add("b");
        param.add("c");
        FunctionDefinitionNode fnode = new FunctionDefinitionNode("Name", param, null);
        Assert.assertEquals(expectedOutcome, fnode.toString());
    }

    @Test
    public void BNODE_toString() throws Exception {
        LinkedList<StatementNode> list = new LinkedList<StatementNode>();
        BlockNode test = new BlockNode(Optional.empty(), list);
        Assert.assertEquals("{ NULL STATEMENT\n}", test.toString());
    }

    @Test
    public void OPNODE_toString() throws Exception {
        OperationNode test = new OperationNode(new VariableReferenceNode("leftVariable"), OperationNode.Operation.PREDEC);
        Assert.assertEquals("(--leftVariable)", test.toString());
        test = new OperationNode(new VariableReferenceNode("leftVariable"), OperationNode.Operation.DIVIDE, new VariableReferenceNode("5"));
        Assert.assertEquals("(leftVariable/5)", test.toString());
    }

    @Test
    public void VRN_toString() throws Exception {
        VariableReferenceNode test = new VariableReferenceNode("variable");
        Assert.assertEquals("variable", test.toString());
        test = new VariableReferenceNode("array", new VariableReferenceNode("index"));
        Assert.assertEquals("array[index]", test.toString());
    }

    @Test
    public void INTP() throws Exception{
        Lexer lex = new Lexer("BEGIN {print 2  \" \" \"banana\"}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        inter.interpretProgram();
    }
    
    @Test
    public void LM() throws Exception{
        String inputFileName = "test3.txt";
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), inputFileName);

        for(int i = 0; i<10; i++)
            Assert.assertTrue(inter.getLineManager().splitAndAssign());
        Assert.assertFalse(inter.getLineManager().splitAndAssign());

        // Make a new Interpreter to make a new line manager for further testing.
        inter = new Interpreter(parse.parse(), "test3.txt");
        HashMap<String, InterpreterDataType> map = inter.TEST_getGlobals();

        inter.getLineManager().splitAndAssign();
        Assert.assertEquals(map.get("$0").getValue(), "1 Dave 78");
        Assert.assertEquals(map.get("$1").getValue(), "1");
        Assert.assertEquals(map.get("$2").getValue(), "Dave");
        Assert.assertEquals(map.get("$3").getValue(), "78");

        inter.getLineManager().splitAndAssign();
        Assert.assertEquals(map.get("$0").getValue(), "2 Greta 19023");
        Assert.assertEquals(map.get("$1").getValue(), "2");
        Assert.assertEquals(map.get("$2").getValue(), "Greta");
        Assert.assertEquals(map.get("$3").getValue(), "19023");

        inter.getLineManager().splitAndAssign();
        Assert.assertEquals(map.get("$0").getValue(), "3 Tod 789");
        Assert.assertEquals(map.get("$1").getValue(), "3");
        Assert.assertEquals(map.get("$2").getValue(), "Tod");
        Assert.assertEquals(map.get("$3").getValue(), "789");
    }

    @Test
    public void BIFD() throws Exception{
        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("0", new InterpreterDataType("return value :)"));
        var test = new BuiltInFunctionDefinitionNode("testFoo", (hm)->(hm.get("0").getValue()), false, new LinkedList<String>());
        Assert.assertEquals("return value :)", test.execute(testmap));
    }

    @Test
    public void BIFD_print() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("print");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("array", new InterpreterArrayDataType(new String[]{"test"}));

        Assert.assertEquals("", test.execute(testmap));
    }

    @Test
    public void BIFD_printf() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("printf");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("array", new InterpreterArrayDataType(new String[]{"test"}));
        testmap.put("format", toIDT("%s"));

        Assert.assertEquals("", test.execute(testmap));
    }

    @Test
    public void BIFD_getline() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("getline");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        //testmap.put();

        Assert.assertEquals("1", test.execute(testmap));
    }

    @Test
    public void BIFD_next() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("next");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        //testmap.put();

        Assert.assertEquals("1", test.execute(testmap));
    }

    @Test
    public void BIFD_gsub() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("gsub");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        inter.TEST_getGlobals().put("test", toIDT("@ replace this please ->>@<<- pleeeeeease"));
        testmap.put("target", toIDT("test"));
        testmap.put("regexp", toIDT("(@)"));
        testmap.put("replacement", toIDT("\\$"));

        Assert.assertEquals("1", test.execute(testmap));
        Assert.assertEquals("$ replace this please ->>$<<- pleeeeeease", inter.TEST_getGlobals().get("test").getValue());
    }

    @Test
    public void BIFD_match() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("match");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("string", toIDT(" can you find the 1@??????"));
        testmap.put("regexp", toIDT("[0-9]@*"));

        Assert.assertEquals("19", test.execute(testmap));
    }

    @Test
    public void BIFD_sub() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("sub");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        inter.TEST_getGlobals().put("test", toIDT("water, water, everywhere"));
        testmap.put("regexp", toIDT("at"));
        testmap.put("replacement", toIDT("ith"));
        testmap.put("target", toIDT("test"));

        Assert.assertEquals("1", test.execute(testmap));
        Assert.assertEquals("wither, water, everywhere", inter.TEST_getGlobals().get("test").getValue());
    }

    @Test
    public void BIFD_index() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("index");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("find", toIDT("@"));
        testmap.put("in", toIDT("wheres the @?"));


        Assert.assertEquals("11", test.execute(testmap));
    }

    @Test
    public void BIFD_length() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("length");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("string", toIDT("How long?"));

        Assert.assertEquals("9", test.execute(testmap));
    }

    @Test
    public void BIFD_split() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("split");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("string", toIDT("Can you seperate these?"));
        testmap.put("array", toIDT("array"));

        Assert.assertEquals("4", test.execute(testmap));
    }

    @Test
    public void BIFD_sprintf() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("sprintf");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("array", new InterpreterArrayDataType(new String[]{"test"}));
        testmap.put("format", toIDT("%s"));

        Assert.assertEquals("test", test.execute(testmap));
    }
    
    @Test
    public void BIFD_substr() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("substr");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("string", toIDT("this is a test"));
        testmap.put("start", toIDT(10));

        Assert.assertEquals("test", test.execute(testmap));
    }

    @Test
    public void BIFD_toLower() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("tolower");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("string", toIDT("make This lOWER"));

        Assert.assertEquals("make this lower", test.execute(testmap));
    }

    @Test
    public void BIFD_toUpper() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("toupper");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("string", toIDT("make this UPPER"));

        Assert.assertEquals("MAKE THIS UPPER", test.execute(testmap));
    }

    @Test
    public void INTP_getIDT_AssignmentNode() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        AssignmentNode an  = new AssignmentNode(new VariableReferenceNode("testVar"), 
                                                new ConstantNode(new Token(Token.Type.NUMBER, "420", 1, 1)));
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();

        Assert.assertEquals("420", inter.TEST_getIDT(an, vars).getValue());

        an  = new AssignmentNode(new VariableReferenceNode("testVar"), 
              new ConstantNode(new Token(Token.Type.NUMBER, "69", 1, 1)));

        Assert.assertEquals("69", inter.TEST_getIDT(an, vars).getValue());
        Assert.assertEquals("69", vars.get("testVar").getValue());
    }

    @Test
    public void INTP_getIDT_ConstantNode() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        ConstantNode cn  = new ConstantNode(new Token(Token.Type.NUMBER, "666",1, 1));
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();

        Assert.assertEquals("666", inter.TEST_getIDT(cn, vars).getValue());
    }

    @Test
    public void INTP_getIDT_FunctionCall() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}\n" + 
                              "function foo() {return \"\"}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        FunctionCallNode fcn  = new FunctionCallNode("foo", new LinkedList<Node>());
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();

        Assert.assertEquals("", inter.TEST_getIDT(fcn, vars).getValue());
    }

    @Test
    public void INTP_getIDT_PatternNode() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        PatternNode pn  = new PatternNode(new Token(Token.Type.PATTERN, "*", 1, 1));
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();

        try{
            inter.TEST_getIDT(pn, vars);
        }
        catch(Exception e){
            Assert.assertTrue(true);
        }
    }

    @Test
    public void INTP_getIDT_TernaryNode() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        TernaryNode tn  = new TernaryNode(new ConstantNode(new Token(Token.Type.NUMBER, "1",1, 1)),
                                          new ConstantNode(new Token(Token.Type.NUMBER, "true",1, 1)),
                                          new ConstantNode(new Token(Token.Type.NUMBER, "false",1, 1)));
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();

        Assert.assertEquals("true", inter.TEST_getIDT(tn, vars).getValue());

        tn  = new TernaryNode(new ConstantNode(new Token(Token.Type.NUMBER, "true (ie, false :)",1, 1)),
              new ConstantNode(new Token(Token.Type.NUMBER, "true",1, 1)),
              new ConstantNode(new Token(Token.Type.NUMBER, "false",1, 1)));

        Assert.assertEquals("false", inter.TEST_getIDT(tn, vars).getValue());
    }

    @Test
    public void INTP_getIDT_VariableReferenceNode() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        VariableReferenceNode vrn  = new VariableReferenceNode("test");
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();
        vars.put("test", toIDT("correct"));

        Assert.assertEquals("correct", inter.TEST_getIDT(vrn, vars).getValue());

        vrn  = new VariableReferenceNode("testArray", new ConstantNode(new Token(Token.Type.NUMBER, "2", 1, 1)));
        vars.put("testArray", new InterpreterArrayDataType(new String[]{"not this one", "Not this either", "yes this one", "Nope too far"}));

        Assert.assertEquals("yes this one", inter.TEST_getIDT(vrn, vars).getValue());
    }

    @Test
    public void INTP_getIDT_OperationNode() throws Exception{
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();
        vars.put("tests", new InterpreterArrayDataType(new String[]{"correct"}));
        vars.put("testNum", toIDT(3));

        OperationNode on  = new OperationNode(toConstantNode(3), OperationNode.Operation.ADD, toConstantNode(3));
        Assert.assertEquals("6.0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(3), OperationNode.Operation.AND, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode(0), OperationNode.Operation.AND, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode("This and "), OperationNode.Operation.CONCATENATION, toConstantNode("that"));
        Assert.assertEquals("This and that", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(69), OperationNode.Operation.DIVIDE, toConstantNode(3));
        Assert.assertEquals("23.0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(3), OperationNode.Operation.EQ, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode(3.0f), OperationNode.Operation.EQ, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode("many"), OperationNode.Operation.EQ, toConstantNode("many"));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode("many"), OperationNode.Operation.EQ, toConstantNode("more"));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(3), OperationNode.Operation.EXPONENT, toConstantNode(3));
        Assert.assertEquals("27.0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(3), OperationNode.Operation.GE, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode(4), OperationNode.Operation.GE, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode(2), OperationNode.Operation.GE, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(3), OperationNode.Operation.GT, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode(4), OperationNode.Operation.GT, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode(2), OperationNode.Operation.GT, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(3), OperationNode.Operation.LE, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode(4), OperationNode.Operation.LE, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode(2), OperationNode.Operation.LE, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(3), OperationNode.Operation.LT, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode(4), OperationNode.Operation.LT, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode(2), OperationNode.Operation.LT, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(0), OperationNode.Operation.IN, new VariableReferenceNode("tests"));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(new PatternNode(new Token(Token.Type.PATTERN, "[0-9]*.p", 0, 0)), OperationNode.Operation.MATCH, toConstantNode("hi 2gamma.p!"));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(4), OperationNode.Operation.MODULO, toConstantNode(3));
        Assert.assertEquals("1.0", inter.TEST_getIDT(on, vars).getValue());
        
        on  = new OperationNode(toConstantNode(3), OperationNode.Operation.MULTIPLY, toConstantNode(3));
        Assert.assertEquals("9.0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(3), OperationNode.Operation.NE, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());
        on  = new OperationNode(toConstantNode(4), OperationNode.Operation.NE, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(new PatternNode(new Token(Token.Type.PATTERN, "[1-9]*.p", 0, 0)), OperationNode.Operation.MATCH, toConstantNode("hi 2gamma.p!"));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(3), OperationNode.Operation.OR, toConstantNode(0));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(3), OperationNode.Operation.SUBTRACT, toConstantNode(2));
        Assert.assertEquals("1.0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(3), OperationNode.Operation.NOT);
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(new VariableReferenceNode("testNum"), OperationNode.Operation.POSTDEC);
        Assert.assertEquals("3.0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(new VariableReferenceNode("testNum"), OperationNode.Operation.POSTINC);
        Assert.assertEquals("2.0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(new VariableReferenceNode("testNum"), OperationNode.Operation.PREDEC);
        Assert.assertEquals("2.0", inter.TEST_getIDT(on, vars).getValue());
        
        on  = new OperationNode(new VariableReferenceNode("testNum"), OperationNode.Operation.PREINC);
        Assert.assertEquals("3.0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(69), OperationNode.Operation.UNARYNEG);
        Assert.assertEquals("-69.0", inter.TEST_getIDT(on, vars).getValue());

        on  = new OperationNode(toConstantNode(-3), OperationNode.Operation.UNARYPOS);
        Assert.assertEquals("3.0", inter.TEST_getIDT(on, vars).getValue());
    }

    @Test
    public void ReturnType() throws Exception{
        ReturnType retty = new ReturnType(ReturnType.Result.BREAK);
        Assert.assertEquals("BREAK", retty.toString());
        Assert.assertFalse(retty.hasValue());
        Assert.assertEquals(ReturnType.Result.BREAK, retty.getResult());

        retty = new ReturnType(ReturnType.Result.RETURN, "I am the return variable");
        Assert.assertEquals("RETURN \"I am the return variable\"", retty.toString());
        Assert.assertTrue(retty.hasValue());
        Assert.assertEquals("I am the return variable", retty.getValue().get());
        Assert.assertEquals(ReturnType.Result.RETURN, retty.getResult());
    }

    @Test
    public void FINAL_HelloWorld() throws Exception{
        // Select files
        String programFileName = "testAWK1.awk";
        String inputFileName = "test1.txt";
        
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
        Interpreter interpreter = new Interpreter(program, inputFileName);
        interpreter.interpretProgram();
    }

    @Test
    public void FINAL_MathCheck() throws Exception{
        // Select files
        String programFileName = "testAWK2.awk";
        String inputFileName = "test2.txt";
        
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
        Interpreter interpreter = new Interpreter(program, inputFileName);
        interpreter.interpretProgram();
    }

    @Test
    public void FINAL_InputCheck() throws Exception{
        // Select files
        String programFileName = "testAWK3.awk";
        String inputFileName = "test3.txt";
        
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
        Interpreter interpreter = new Interpreter(program, inputFileName);
        interpreter.interpretProgram();
    }

    @Test
    public void FINAL_ConditionalCheck() throws Exception{
        // Select files
        String programFileName = "testAWK4.awk";
        String inputFileName = "";
        
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
        Interpreter interpreter = new Interpreter(program, inputFileName);
        interpreter.interpretProgram();
    }

    @Test
    public void FINAL_FunctionsCheck() throws Exception{
        // Select files
        String programFileName = "testAWK5.awk";
        String inputFileName = "";
        
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

        //* Parse
        Parser parser = new Parser(list);
        ProgramNode program = parser.parse();

        //Interpret
        Interpreter interpreter = new Interpreter(program, inputFileName);
        interpreter.interpretProgram();
    }

    // Quality of life functions:
    private InterpreterDataType toIDT(String value){
        return new InterpreterDataType(value);
    }

    private InterpreterDataType toIDT(int value){
        return new InterpreterDataType(Integer.toString(value));
    }

    private ConstantNode toConstantNode(String value){
        return new ConstantNode(new Token(Token.Type.STRINGLITERAL, value, 0, 0));
    }

    private ConstantNode toConstantNode(int value){
        return new ConstantNode(new Token(Token.Type.NUMBER, Integer.toString(value), 0, 0));
    }

    private ConstantNode toConstantNode(float value){
        return new ConstantNode(new Token(Token.Type.NUMBER, Float.toString(value), 0, 0));
    }
}
