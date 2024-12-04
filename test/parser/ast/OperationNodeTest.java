package test.parser.ast;

import org.junit.Assert;
import org.junit.Test;

import parser.ast.*;

public class OperationNodeTest {

    @Test
    public void OpN_toString() throws Exception {
        OperationNode test = new OperationNode(new VariableReferenceNode("leftVariable"),
                OperationNode.Operation.PREDEC);
        Assert.assertEquals("(--leftVariable)", test.toString());
        test = new OperationNode(new VariableReferenceNode("leftVariable"), OperationNode.Operation.DIVIDE,
                new VariableReferenceNode("5"));
        Assert.assertEquals("(leftVariable/5)", test.toString());
    }
}
