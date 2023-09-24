import org.junit.Assert;
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
            "BEGIN\t{FS = \",\"}\n" + 
            "\t{\n" + 
            "\t LineCount = 0;\n" + 
            "\t for (i = 1; i <= NF; i = i + 1) {\n" + 
            "\t\tLineCount = LineCount + $i\n" + 
            "\t\t}\n" + 
            "\tCountTotal = CountTotal + LineCount;\n" + 
            "\tprint \"line \" NR \": \" LineCount;\n" + 
            "\t}\n" + 
            "END\t{print \"Grand Total: \" CountTotal;}\n";
    String testTH = "test for test while hello test do test break examin if whatabout continue tryAn else butwhatif return andwecantforgetabout BEGIN waitand END\nbutwhatifthekeywordsaretogether print printf next in delete getline exit nextfile function\n\"What about a string literal?\" \"does it recognize \\\"ESCAPESEPTION!?!?!\\\"\" test andIShouldNotforget the \"\" `*patern*`\nI am going to try to string literal \"Is it working???\" banana ; fish \"What about \\\"NOW?\\\"\" What if I just \"\" \n>= ++ -- <= == != ^= %= *= 3/=4 += -= !~ && >> whatAboutACurveBall ||\n{ } [ ] ( ) $ ~ = < > ! + ^ - ? :test * / % ; curveBall | ,\n`test` `124` `next to``eachother` `` banananana`patterns|with&symbols`";

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

    /*
    @Test
    public void StringHandler() throws Exception {
        StringHandler test = new StringHandler(
            "TestAlphabeticWord Test_Words_With_Underscores Tes5tW0rdsW1thNum8ers testLowercase TEST_UPPPERCASE\n" +
            "WhatAboutA2ndLine Does_That_Still_Work AreY0uSur3 are_you_really_sure TOTALY_POSITIVE\n" + 
            "3 14 159 265359 2.718281828459045 127.0.0.1 \n" + 
            "WordsThen 1039257.3\n" + 
            "8792305 ThenWords");
        Assert.assertTrue(false);
    }/* */

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
                " { banana fish\n" + //
                " if }\n" + //
                " function testy (no, yes, maybe) {true false}";
        
        String output = "function tester (a, c, d, ) { NULL STATEMENTS\n" + 
                        "}\n" + 
                        "function testy (no, yes, maybe, ) { NULL STATEMENTS\n" + 
                        "}\n";
        Lexer lex = new Lexer(input);
        Parser parse = new Parser(lex.lex());
        ProgramNode pNode = parse.parse();
        Assert.assertEquals(output, pNode.toString());
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
        System.out.println(testParse);
        ProgramNode testNode = testParse.parse();
        Assert.assertEquals("BEGIN { NULL STATEMENT\n}\n{ NULL STATEMENT\n}\n{ NULL STATEMENT\n}\n{ NULL STATEMENT\n}\n", testNode.toString());
    }

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
}
