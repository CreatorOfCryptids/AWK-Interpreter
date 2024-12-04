package test.parser.ast;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import parser.ast.*;

public class FunctionDefinitionNodeTest {
    @Test
    public void FDN_toString() throws Exception {
        String expectedOutcome = "function Name (a, b, c, ) { NULL STATEMENTS\n" +
                "}";
        LinkedList<String> param = new LinkedList<String>();
        param.add("a");
        param.add("b");
        param.add("c");
        FunctionDefinitionNode fnode = new FunctionDefinitionNode("Name", param, null);
        Assert.assertEquals(expectedOutcome, fnode.toString());
    }
}
