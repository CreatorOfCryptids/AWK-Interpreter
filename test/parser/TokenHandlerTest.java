package test.parser;

import org.junit.Assert;
import org.junit.Test;

import lexer.Lexer;
import lexer.Token;
import parser.TokenHandler;

public class TokenHandlerTest {

    String testTH = "test for test while hello test do test break examin if whatabout continue tryAn else butwhatif return andwecantforgetabout BEGIN waitand END\nbutwhatifthekeywordsaretogether print printf next in delete getline exit nextfile function\n\"What about a string literal?\" \"does it recognize \\\"ESCAPESEPTION!?!?!\\\"\" test andIShouldNotforget the \"\" `*patern*`\nI am going to try to string literal \"Is it working???\" banana ; fish \"What about \\\"NOW?\\\"\" What if I just \"\" \n>= ++ -- <= == != ^= %= *= 3/=4 += -= !~ && >> whatAboutACurveBall ||\n{ } [ ] ( ) $ ~ = < > ! + ^ - ? :test * / % ; curveBall | ,\n`test` `124` `next to``eachother` `` banananana`patterns|with&symbols`";

    @Test
    public void matchAndRemove() throws Exception {
        Lexer lex = new Lexer(testTH);
        TokenHandler testTH = new TokenHandler(lex.lex());

        while (testTH.moreTokens()) {
            Assert.assertEquals(testTH.peek(), testTH.matchAndRemove(testTH.peek().get().getType()));
        }
    }

    @Test
    public void moreTokens() throws Exception {
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
    public void peek() throws Exception {
        Lexer lex = new Lexer("if for while banananana 24 \"no\"");
        TokenHandler testTH = new TokenHandler(lex.lex());

        Assert.assertEquals("IF", testTH.peek().get().toString());
        Assert.assertEquals("FOR", testTH.peek(1).get().toString());
        Assert.assertEquals("WHILE", testTH.peek(2).get().toString());
        Assert.assertEquals("WORD(banananana)", testTH.peek(3).get().toString());
        Assert.assertEquals("NUMBER(24)", testTH.peek(4).get().toString());
        Assert.assertEquals("STRINGLITERAL(no)", testTH.peek(5).get().toString());
    }
}
