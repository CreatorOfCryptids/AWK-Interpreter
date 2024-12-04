package test.parser.ast;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import parser.ast.*;

public class ProgramNodeTest {
    @Test
    public void ProgramNode_toString() throws Exception {
        ProgramNode testNode = new ProgramNode();
        Assert.assertEquals("", testNode.toString());
        testNode.add(new FunctionDefinitionNode("banana", null, null));
        Assert.assertEquals("function banana () { NULL STATEMENTS\n}\n", testNode.toString());
    }

    @Test
    public void add() throws Exception {
        ProgramNode test = new ProgramNode();
        Assert.assertEquals("", test.toString());
        FunctionDefinitionNode func = new FunctionDefinitionNode("funky", null, null);
        test.add(func);
        Assert.assertEquals("function funky () { NULL STATEMENTS\n}\n", test.toString());
        BlockNode begin = new BlockNode(Optional.empty(), null);
        test.addBeginBlock(begin);
        test.addEndBlock(begin);
        test.toString();
        Assert.assertEquals(
                "function funky () { NULL STATEMENTS\n}\nBEGIN { NULL STATEMENT\n}\nEND { NULL STATEMENT\n}\n",
                test.toString());

    }
}
