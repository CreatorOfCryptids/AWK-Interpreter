package test.lexer;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import lexer.*;

public class LexerTest {

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
    public void lexer() throws Exception {
        Lexer testLex = new Lexer(testString1);
        String output = "";
        for (Token t : testLex.lex()) {
            output += t.toString();
        }
        Assert.assertEquals(fullDesiredOutput1, output);
    }

    @Test
    public void processNumber() throws Exception {
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
    public void processWord() throws Exception {
        Lexer testLex = new Lexer(
                "lowercaseword PascalCaseWord camalCaseWord UPPERCASEWORD w0rdW1thNum8ber5 word_with_underscores");
        LinkedList<Token> output = testLex.lex();
        Assert.assertEquals(output.get(0).toString(), "WORD(lowercaseword)");
        Assert.assertEquals(output.get(1).toString(), "WORD(PascalCaseWord)");
        Assert.assertEquals(output.get(2).toString(), "WORD(camalCaseWord)");
        Assert.assertEquals(output.get(3).toString(), "WORD(UPPERCASEWORD)");
        Assert.assertEquals(output.get(4).toString(), "WORD(w0rdW1thNum8ber5)");
        Assert.assertEquals(output.get(5).toString(), "WORD(word_with_underscores)");
    }

    @Test
    public void processStringLiteral() throws Exception {
        Lexer testLex = new Lexer(
                "I am going to try to string literal \"Is it working???\" banana \"What about \\\"NOW?\\\"\" What if I just \"\"");
        LinkedList<Token> output = testLex.lex();
        Assert.assertEquals("STRINGLITERAL(Is it working???)", output.get(8).toString());
        Assert.assertEquals("STRINGLITERAL(What about \"NOW?\")", output.get(10).toString());
        Assert.assertEquals("STRINGLITERAL()", output.get(15).toString());
    }

    @Test
    public void processDoubleCharacters() throws Exception {
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
    public void processSingleChar() throws Exception {
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
    public void processPattern() throws Exception {
        Lexer testLex = new Lexer("`test` `124` `next to``eachother` `` banananana`patterns|with&symbols`");
        LinkedList<Token> output = testLex.lex();

        Assert.assertEquals(output.get(0).toString(), "PATTERN(test)");
        Assert.assertEquals(output.get(1).toString(), "PATTERN(124)");
        Assert.assertEquals(output.get(2).toString(), "PATTERN(next to)");
        Assert.assertEquals(output.get(3).toString(), "PATTERN(eachother)");
        Assert.assertEquals(output.get(4).toString(), "PATTERN()");
        Assert.assertEquals(output.get(6).toString(), "PATTERN(patterns|with&symbols)");
    }

}
