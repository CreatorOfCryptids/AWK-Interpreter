import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Test;

public class UnitTests {

    String fileName = "text1.txt";
    String testString = "TestAlphabeticWord Tes5tW0rdsW1thNum8ers testlowercase TESTUPPPERCASE\n" +
    "WhatAboutA2ndLine AreY0uSur3 areyoureallysure TOTALYPOSITIVE\n" +
    "3 14 159 265359 2.718281828459045 127.0.0.1\n" +
    "WordsThen 1039257.3\n"+
    "8792305 ThenWords";
    String fullDesiredOutput = "WORD(TestAlphabeticWord) WORD(Tes5tW0rdsW1thNum8ers) WORD(testLowercase) WORD(TESTUPPPERCASE) " +
    "SEPERATOR WORD(WhatAboutA2ndLine) WORD(AreY0uSur3) WORD(areyoureallysure) WORD(TOTALYPOSITIVE) SEPERATOR NUMBER(3) " + 
    "NUMBER(14) NUMBER(159) NUMBER(265359) NUMBER(2.718281828459045) NUMBER(127.0) NUMBER(.0) NUMBER(.1) SEPERATOR WORD(WordsThen) " + 
    "NUMBER(1039257.3) SEPERATOR NUMBER(8792305) WORD(ThenWords) ";

    @Test 
    public void SH_peek() throws Exception{
        StringHandler test = new StringHandler(testString);
        
        for (int i = 0; i<testString.length(); i++){
            Assert.assertEquals(testString.charAt(i), test.peek(i));
            Assert.assertEquals(0, test.getFingerIndex());
        }

        test.swallow(19); // Iterates the finger
        Assert.assertEquals(19, test.getFingerIndex());
        // Checks if it works when the finger index is greater than 0.
        for(int i=0; i<(testString.length()-19); i++)
        // Checks that the finger isn't moved.
        Assert.assertEquals(19, test.getFingerIndex());
    }

    @Test
    public void SH_peekString() throws Exception {
        StringHandler test = new StringHandler(testString);

        Assert.assertEquals("", test.peekString(0));
        Assert.assertEquals("Test", test.peekString(4));
        Assert.assertEquals("TestAlphabeticWord", test.peekString(18));
        test.swallow(70);
        Assert.assertEquals("WhatAboutA2ndLine", test.peekString(17));
        test.swallow(46);
        Assert.assertEquals("TOTALYPOSITIVE\n" + 
                "3 14 159", test.peekString(23));
    }

    @Test
    public void SH_getChar() throws Exception {
        StringHandler test = new StringHandler(testString);


        Assert.assertEquals(0, test.getFingerIndex());
        Assert.assertEquals('T', test.getChar());
        Assert.assertEquals(1, test.getFingerIndex());
        for (int i=1; i<testString.length(); i++){
            Assert.assertEquals(testString.charAt(i), test.getChar());
        }

    }

    @Test
    public void SH_swallow() throws Exception {
        StringHandler test = new StringHandler(testString);

        
        Assert.assertEquals(0, test.getFingerIndex());
        test.swallow(0);
        Assert.assertEquals(0, test.getFingerIndex());
        test.swallow(10);
        Assert.assertEquals(10, test.getFingerIndex());
        test.swallow(90);
        Assert.assertEquals(100, test.getFingerIndex());
    }

    @Test
    public void SH_isDone() throws Exception {
        StringHandler test = new StringHandler(testString);

        String output = "";
        while(!test.isDone()){
            output += test.getChar();
        }
        Assert.assertEquals(testString, output);
    }

    @Test
    public void SH_remainder() throws Exception {
        StringHandler test = new StringHandler(testString);

        Assert.assertEquals(testString, test.remainder());
        test.swallow(99);
        Assert.assertEquals(testString.substring(99), test.remainder());
        test.swallow(96);
        Assert.assertEquals("8792305 ThenWords", test.remainder());
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
        Lexer testLex = new Lexer("/home/danny/GitShit/ICSI311/test1.txt");
        String output = "";
        for (Token t : testLex.lex()){
            output += t.toString() + " ";
        }
        Assert.assertEquals(fullDesiredOutput, output);
    }

    @Test
    public void LEX_processNumber() throws Exception {
        Assert.assertTrue(false);
    }

    @Test
    public void LEX_processWord() throws Exception {
        Assert.assertTrue(false);
    }

    /*
    @Test
    public void Lexer() throws Exception {
        Assert.assertTrue(false);
    }/* */

    @Test
    public void T_toString() throws Exception {
        Token testToken = new Token(Token.Type.WORD, "TestAlphabeticWord", 1, 20);
        Assert.assertEquals("WORD(TestAlphabeticWord)", testToken.toString());
    }
}
