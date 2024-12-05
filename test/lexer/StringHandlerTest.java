package test.lexer;

import org.junit.Assert;
import org.junit.Test;

import lexer.StringHandler;

public class StringHandlerTest {
    String testString1 = "TestAlphabeticWord Tes5tW0rdsW1thNum8ers testlowercase TESTUPPPERCASE\n" +
            "WhatAboutA2ndLine AreY0uSur3 areyoureallysure TOTALYPOSITIVE\n" +
            "3 14 159 265359 2.718281828459045 127.0.0.1\n" +
            "WordsThen 1039257.3\n" +
            "8792305 ThenWords";
    String fullDesiredOutput1 = "WORD(TestAlphabeticWord)WORD(Tes5tW0rdsW1thNum8ers)WORD(testlowercase)WORD(TESTUPPPERCASE)SEPERATORWORD(WhatAboutA2ndLine)"
            +
            "WORD(AreY0uSur3)WORD(areyoureallysure)WORD(TOTALYPOSITIVE)SEPERATORNUMBER(3)NUMBER(14)NUMBER(159)NUMBER(265359)NUMBER(2.718281828459045)NUMBER(127.0)"
            +
            "NUMBER(.0)NUMBER(.1)SEPERATORWORD(WordsThen)NUMBER(1039257.3)SEPERATORNUMBER(8792305)WORD(ThenWords)";
    String testString2 = "test for test while hello test do test break examin if whatabout continue tryan else butwhatif reurn andwecantforgetabout BEGIN waitand END\n"
            +
            "butwhatifthekeywordsaretogether print printf next in delete getline exit nextfile function\n" +
            "\"What about a string literal?\" \"does it recognize \\\"ESCAPESEPTION!?!?!\\\"\" test andIShouldnotforget the\"\" `*patern*`";
    String desiredOutput2 = "";

    @Test
    public void peek() throws Exception {
        StringHandler testHandler = new StringHandler(testString1);

        // loops thru each and every character in the thing to see if it works.
        for (int i = 0; i < testString1.length(); i++) {
            Assert.assertEquals(testString1.charAt(i), testHandler.peek(i));
            Assert.assertEquals(0, testHandler.getCurrentIndex());
        }

        testHandler.swallow(19); // Iterates the finger
        Assert.assertEquals(19, testHandler.getCurrentIndex());
        // Checks if it works when the finger index is greater than 0.
        for (int i = 0; i < (testString1.length() - 19); i++)
            Assert.assertEquals(testString1.charAt(i + 19), testHandler.peek(i));
        // Checks that the finger isn't moved.
        Assert.assertEquals(19, testHandler.getCurrentIndex());
    }

    @Test
    public void peekString() throws Exception {
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
    public void getChar() throws Exception {
        StringHandler testHandler = new StringHandler(testString1);
        Assert.assertEquals(0, testHandler.getCurrentIndex());
        Assert.assertEquals('T', testHandler.getChar());
        Assert.assertEquals(1, testHandler.getCurrentIndex());
        for (int i = 1; i < testString1.length(); i++) {
            Assert.assertEquals(testString1.charAt(i), testHandler.getChar());
        }

    }

    @Test
    public void swallow() throws Exception {
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
    public void isDone() throws Exception {
        StringHandler testHandler = new StringHandler(testString1);

        String output = "";
        while (!testHandler.isDone()) {
            output += testHandler.getChar();
        }
        Assert.assertEquals(testString1, output);
    }

    @Test
    public void remainder() throws Exception {
        StringHandler testHandler = new StringHandler(testString1);

        Assert.assertEquals(testString1, testHandler.remainder());
        testHandler.swallow(99);
        Assert.assertEquals(testString1.substring(99), testHandler.remainder());
        testHandler.swallow(96);
        Assert.assertEquals("8792305 ThenWords", testHandler.remainder());
    }
}
