package test.parser.ast;

import org.junit.Assert;
import org.junit.Test;

import parser.ast.*;

public class VariableReferenceNodeTest {
    @Test
    public void VRN_toString() throws Exception {
        VariableReferenceNode test = new VariableReferenceNode("variable");
        Assert.assertEquals("variable", test.toString());
        test = new VariableReferenceNode("array", new VariableReferenceNode("index"));
        Assert.assertEquals("array[index]", test.toString());
    }
}
