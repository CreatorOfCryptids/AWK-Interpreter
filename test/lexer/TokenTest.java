package test.lexer;

import org.junit.Assert;
import org.junit.Test;

import lexer.Token;

public class TokenTest {
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
