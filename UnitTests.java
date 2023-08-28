import static org.junit.Assert.*;
import org.junit.Assert;
import org.junit.Test;

public class UnitTests {

    String fileName = "text1.txt";
    String testString = "TestAlphabeticWord Test_Words_With_Underscores Tes5tW0rdsW1thNum8ers testLowercase TEST_UPPPERCASE\n" +
            "WhatAboutA2ndLine Does_That_Still_Work AreY0uSur3 are_you_really_sure TOTALY_POSITIVE\n" + 
            "3 14 159 265359 2.718281828459045 127.0.0.1 \n" + 
            "WordsThen 1039257.3\n" + 
            "8792305 ThenWords";

    @Test 
    public void SH_peek() throws Exception{
        StringHandler test = new StringHandler(
            "TestAlphabeticWord Test_Words_With_Underscores Tes5tW0rdsW1thNum8ers testLowercase TEST_UPPPERCASE\n" +
            "WhatAboutA2ndLine Does_That_Still_Work AreY0uSur3 are_you_really_sure TOTALY_POSITIVE\n" + 
            "3 14 159 265359 2.718281828459045 127.0.0.1 \n" + 
            "WordsThen 1039257.3\n" + 
            "8792305 ThenWords");
        
        // Check to see if the fingerIndex changes.
        Assert.assertEquals(0, test.getFingerIndex());
        // Checks an assortment of characters.
        Assert.assertEquals('A', test.peek(4));
        Assert.assertEquals(' ', test.peek(18));
        Assert.assertEquals('_', test.peek(23));
        Assert.assertEquals('5', test.peek(50));
        // Checks new line characters
        Assert.assertEquals('\n', test.peek(98));
        // Checks lines after a new line character
        Assert.assertEquals('W', test.peek(99));
        // Checks numbers
        Assert.assertEquals('3', test.peek(185));
        // Checks periods
        Assert.assertEquals('.', test.peek(202));
        // Makes sure that it doesn't change the fingerIndex.
        Assert.assertEquals(0, test.getFingerIndex());

        test.swallow(19); // Iterates the finger
        Assert.assertEquals(19, test.getFingerIndex());
        // Checks if it works when the finger index is greater than 0.
        Assert.assertEquals('_', test.peek(4));
        // Checks that the finger isn't moved.
        Assert.assertEquals(19, test.getFingerIndex());
    }

    @Test
    public void SH_peekString() throws Exception {
        StringHandler test = new StringHandler(
            "TestAlphabeticWord Test_Words_With_Underscores Tes5tW0rdsW1thNum8ers testLowercase TEST_UPPPERCASE\n" +
            "WhatAboutA2ndLine Does_That_Still_Work AreY0uSur3 are_you_really_sure TOTALY_POSITIVE\n" + 
            "3 14 159 265359 2.718281828459045 127.0.0.1 \n" + 
            "WordsThen 1039257.3\n" + 
            "8792305 ThenWords");
        Assert.assertEquals("", test.peekString(0));
        Assert.assertEquals("Test", test.peekString(4));
        Assert.assertEquals("TestAlphabeticWord", test.peekString(18));
        test.swallow(99);
        Assert.assertEquals("WhatAboutA2ndLine", test.peekString(17));
        test.swallow(70);
        Assert.assertEquals("TOTALY_POSITIVE\n" + 
                "3 14 159", test.peekString(24));
    }

    @Test
    public void SH_getChar() throws Exception {
        StringHandler test = new StringHandler(
            "TestAlphabeticWord Test_Words_With_Underscores Tes5tW0rdsW1thNum8ers testLowercase TEST_UPPPERCASE\n" +
            "WhatAboutA2ndLine Does_That_Still_Work AreY0uSur3 are_you_really_sure TOTALY_POSITIVE\n" + 
            "3 14 159 265359 2.718281828459045 127.0.0.1 \n" + 
            "WordsThen 1039257.3\n" + 
            "8792305 ThenWords");

        Assert.assertEquals(0, test.getFingerIndex());
        Assert.assertEquals('T', test.getChar());
        Assert.assertEquals(1, test.getFingerIndex());
        for (int i=1; i<testString.length(); i++){
            Assert.assertEquals(testString.charAt(i), test.getChar());
        }

    }

    @Test
    public void SH_swallow() throws Exception {
        StringHandler test = new StringHandler(
            "TestAlphabeticWord Test_Words_With_Underscores Tes5tW0rdsW1thNum8ers testLowercase TEST_UPPPERCASE\n" +
            "WhatAboutA2ndLine Does_That_Still_Work AreY0uSur3 are_you_really_sure TOTALY_POSITIVE\n" + 
            "3 14 159 265359 2.718281828459045 127.0.0.1 \n" + 
            "WordsThen 1039257.3\n" + 
            "8792305 ThenWords");
        
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
        StringHandler test = new StringHandler(
            "TestAlphabeticWord Test_Words_With_Underscores Tes5tW0rdsW1thNum8ers testLowercase TEST_UPPPERCASE\n" +
            "WhatAboutA2ndLine Does_That_Still_Work AreY0uSur3 are_you_really_sure TOTALY_POSITIVE\n" + 
            "3 14 159 265359 2.718281828459045 127.0.0.1 \n" + 
            "WordsThen 1039257.3\n" + 
            "8792305 ThenWords");

        Assert.assertFalse(test.isDone());
        test.swallow(100);
        Assert.assertFalse(test.isDone());
        test.swallow(166);
        Assert.assertFalse(test.isDone());
        test.swallow(1);
        Assert.assertTrue(test.isDone());
    }

    @Test
    public void SH_remainder() throws Exception {
        StringHandler test = new StringHandler(
            "TestAlphabeticWord Test_Words_With_Underscores Tes5tW0rdsW1thNum8ers testLowercase TEST_UPPPERCASE\n" +
            "WhatAboutA2ndLine Does_That_Still_Work AreY0uSur3 are_you_really_sure TOTALY_POSITIVE\n" + 
            "3 14 159 265359 2.718281828459045 127.0.0.1 \n" + 
            "WordsThen 1039257.3\n" + 
            "8792305 ThenWords");
        Assert.assertEquals(testString, test.remainder());
        test.swallow(99);
        Assert.assertEquals(
            "WhatAboutA2ndLine Does_That_Still_Work AreY0uSur3 are_you_really_sure TOTALY_POSITIVE\n" + 
            "3 14 159 265359 2.718281828459045 127.0.0.1 \n" + 
            "WordsThen 1039257.3\n" + 
            "8792305 ThenWords", 
            test.remainder());
        test.swallow(86+45+20);
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
    public void LEX_lex() throws Exception{
        
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
        Token testToken = new Token();
        Assert.assertEquals("WORD(TestAlphabeticWord)", testToken.toString());
    }

    /*
    @Test
    public void Token() throws Exception {
        Assert.assertTrue(false);
    }/* */
}
