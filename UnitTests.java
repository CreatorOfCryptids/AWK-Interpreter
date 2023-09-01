import org.junit.Assert;
import java.util.LinkedList;
import org.junit.Test;

public class UnitTests {

    String fileName = "text1.txt";
    String testString = "TestAlphabeticWord Tes5tW0rdsW1thNum8ers testlowercase TESTUPPPERCASE\n" +
    "WhatAboutA2ndLine AreY0uSur3 areyoureallysure TOTALYPOSITIVE\n" +
    "3 14 159 265359 2.718281828459045 127.0.0.1\n" +
    "WordsThen 1039257.3\n"+
    "8792305 ThenWords";
    String fullDesiredOutput = "WORD(TestAlphabeticWord)WORD(Tes5tW0rdsW1thNum8ers)WORD(testlowercase)WORD(TESTUPPPERCASE)SEPERATORWORD(WhatAboutA2ndLine)" + 
    "WORD(AreY0uSur3)WORD(areyoureallysure)WORD(TOTALYPOSITIVE)SEPERATORNUMBER(3)NUMBER(14)NUMBER(159)NUMBER(265359)NUMBER(2.718281828459045)NUMBER(127.0)" + 
    "NUMBER(.0)NUMBER(.1)SEPERATORWORD(WordsThen)NUMBER(1039257.3)SEPERATORNUMBER(8792305)WORD(ThenWords)";

    @Test 
    public void SH_peek() throws Exception{
        StringHandler testHandler = new StringHandler(testString);

        //loops thru each and every character in the thing to see if it works.
        for (int i = 0; i<testString.length(); i++){
            Assert.assertEquals(testString.charAt(i), testHandler.peek(i));
            Assert.assertEquals(0, testHandler.getCurrentIndex());
        }

        testHandler.swallow(19); // Iterates the finger
        Assert.assertEquals(19, testHandler.getCurrentIndex());
        // Checks if it works when the finger index is greater than 0.
        for(int i=0; i<(testString.length()-19); i++)
            Assert.assertEquals(testString.charAt(i+19), testHandler.peek(i));
        // Checks that the finger isn't moved.
        Assert.assertEquals(19, testHandler.getCurrentIndex());
    }

    @Test
    public void SH_peekString() throws Exception {
        StringHandler testHandler = new StringHandler(testString);

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
        StringHandler testHandler = new StringHandler(testString);


        Assert.assertEquals(0, testHandler.getCurrentIndex());
        Assert.assertEquals('T', testHandler.getChar());
        Assert.assertEquals(1, testHandler.getCurrentIndex());
        for (int i=1; i<testString.length(); i++){
            Assert.assertEquals(testString.charAt(i), testHandler.getChar());
        }

    }

    @Test
    public void SH_swallow() throws Exception {
        StringHandler testHandler = new StringHandler(testString);

        
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
        StringHandler testHandler = new StringHandler(testString);

        String output = "";
        while(!testHandler.isDone()){
            output += testHandler.getChar();
        }
        Assert.assertEquals(testString, output);
    }

    @Test
    public void SH_remainder() throws Exception {
        StringHandler testHandler = new StringHandler(testString);

        Assert.assertEquals(testString, testHandler.remainder());
        testHandler.swallow(99);
        Assert.assertEquals(testString.substring(99), testHandler.remainder());
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
    public void LEX() throws Exception{
        Lexer testLex = new Lexer(testString);
        String output = "";
        for (Token t : testLex.lex()){
            output += t.toString();
        }
        Assert.assertEquals(fullDesiredOutput, output);
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
    public void T_toString() throws Exception {
        Token testToken = new Token(Token.Type.WORD, "TestAlphabeticWord", 1, 20);
        Assert.assertEquals("WORD(TestAlphabeticWord)", testToken.toString());
        testToken = new Token(Token.Type.NUMBER, "3.14159", 20, 1);
        Assert.assertEquals("NUMBER(3.14159)", testToken.toString());
        testToken = new Token(Token.Type.SEPERATOR, null, 420, 69);
        Assert.assertEquals("SEPERATOR", testToken.toString());

    }
}
