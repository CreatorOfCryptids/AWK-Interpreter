package test.parser.ast;

import java.util.LinkedList;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import parser.ast.*;

public class BlockNodeTest {
    @Test
    public void BN_toString() throws Exception {
        LinkedList<StatementNode> list = new LinkedList<StatementNode>();
        BlockNode test = new BlockNode(Optional.empty(), list);
        Assert.assertEquals("{ NULL STATEMENT\n}", test.toString());
    }
}
