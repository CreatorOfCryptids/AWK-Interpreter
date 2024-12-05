package test.interpreter;

import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import interpreter.*;
import parser.Parser;
import parser.ast.*;
import lexer.*;

public class InterpreterTest {

    @Test
    public void interpreter() throws Exception {
        Lexer lex = new Lexer("BEGIN {print 2  \" \" \"banana\"}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "../../test3.txt");

        inter.interpretProgram();
    }

    @Test
    public void lineManager() throws Exception {
        String inputFileName = "test3.txt";
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), inputFileName);

        for (int i = 0; i < 10; i++)
            Assert.assertTrue(inter.getLineManager().splitAndAssign());
        Assert.assertFalse(inter.getLineManager().splitAndAssign());

        // Make a new Interpreter to make a new line manager for further testing.
        inter = new Interpreter(parse.parse(), "test3.txt");
        HashMap<String, InterpreterDataType> map = inter.TEST_getGlobals();

        inter.getLineManager().splitAndAssign();
        Assert.assertEquals(map.get("$0").getValue(), "1 Dave 78");
        Assert.assertEquals(map.get("$1").getValue(), "1");
        Assert.assertEquals(map.get("$2").getValue(), "Dave");
        Assert.assertEquals(map.get("$3").getValue(), "78");

        inter.getLineManager().splitAndAssign();
        Assert.assertEquals(map.get("$0").getValue(), "2 Greta 19023");
        Assert.assertEquals(map.get("$1").getValue(), "2");
        Assert.assertEquals(map.get("$2").getValue(), "Greta");
        Assert.assertEquals(map.get("$3").getValue(), "19023");

        inter.getLineManager().splitAndAssign();
        Assert.assertEquals(map.get("$0").getValue(), "3 Tod 789");
        Assert.assertEquals(map.get("$1").getValue(), "3");
        Assert.assertEquals(map.get("$2").getValue(), "Tod");
        Assert.assertEquals(map.get("$3").getValue(), "789");
    }

    @Test
    public void builtInFunctionDefinitionNode() throws Exception {
        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("0", new InterpreterDataType("return value :)"));
        var test = new BuiltInFunctionDefinitionNode("testFoo", (hm) -> (hm.get("0").getValue()), false,
                new LinkedList<String>());
        Assert.assertEquals("return value :)", test.execute(testmap));
    }

    @Test
    public void BIFD_print() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("print");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("array", new InterpreterArrayDataType(new String[] { "test" }));

        Assert.assertEquals("", test.execute(testmap));
    }

    @Test
    public void BIFD_printf() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("printf");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("array", new InterpreterArrayDataType(new String[] { "test" }));
        testmap.put("format", toIDT("%s"));

        Assert.assertEquals("", test.execute(testmap));
    }

    @Test
    public void BIFD_getline() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("getline");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        // testmap.put();

        Assert.assertEquals("1", test.execute(testmap));
    }

    @Test
    public void BIFD_next() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("next");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        // testmap.put();

        Assert.assertEquals("1", test.execute(testmap));
    }

    @Test
    public void BIFD_gsub() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("gsub");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        inter.TEST_getGlobals().put("test", toIDT("@ replace this please ->>@<<- pleeeeeease"));
        testmap.put("target", toIDT("test"));
        testmap.put("regexp", toIDT("(@)"));
        testmap.put("replacement", toIDT("\\$"));

        Assert.assertEquals("1", test.execute(testmap));
        Assert.assertEquals("$ replace this please ->>$<<- pleeeeeease",
                inter.TEST_getGlobals().get("test").getValue());
    }

    @Test
    public void BIFD_match() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("match");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("string", toIDT(" can you find the 1@??????"));
        testmap.put("regexp", toIDT("[0-9]@*"));

        Assert.assertEquals("19", test.execute(testmap));
    }

    @Test
    public void BIFD_sub() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("sub");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        inter.TEST_getGlobals().put("test", toIDT("water, water, everywhere"));
        testmap.put("regexp", toIDT("at"));
        testmap.put("replacement", toIDT("ith"));
        testmap.put("target", toIDT("test"));

        Assert.assertEquals("1", test.execute(testmap));
        Assert.assertEquals("wither, water, everywhere", inter.TEST_getGlobals().get("test").getValue());
    }

    @Test
    public void BIFD_index() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("index");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("find", toIDT("@"));
        testmap.put("in", toIDT("wheres the @?"));

        Assert.assertEquals("11", test.execute(testmap));
    }

    @Test
    public void BIFD_length() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("length");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("string", toIDT("How long?"));

        Assert.assertEquals("9", test.execute(testmap));
    }

    @Test
    public void BIFD_split() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("split");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("string", toIDT("Can you seperate these?"));
        testmap.put("array", toIDT("array"));

        Assert.assertEquals("4", test.execute(testmap));
    }

    @Test
    public void BIFD_sprintf() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("sprintf");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("array", new InterpreterArrayDataType(new String[] { "test" }));
        testmap.put("format", toIDT("%s"));

        Assert.assertEquals("test", test.execute(testmap));
    }

    @Test
    public void BIFD_substr() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("substr");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("string", toIDT("this is a test"));
        testmap.put("start", toIDT(10));

        Assert.assertEquals("test", test.execute(testmap));
    }

    @Test
    public void BIFD_toLower() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("tolower");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("string", toIDT("make This lOWER"));

        Assert.assertEquals("make this lower", test.execute(testmap));
    }

    @Test
    public void BIFD_toUpper() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) inter.getFunctions().get("toupper");

        HashMap<String, InterpreterDataType> testmap = new HashMap<>();
        testmap.put("string", toIDT("make this UPPER"));

        Assert.assertEquals("MAKE THIS UPPER", test.execute(testmap));
    }

    @Test
    public void getIDTFromAssignmentNode() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        AssignmentNode an = new AssignmentNode(new VariableReferenceNode("testVar"),
                new ConstantNode(new Token(Token.Type.NUMBER, "420", 1, 1)));
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();

        Assert.assertEquals("420", inter.TEST_getIDT(an, vars).getValue());

        an = new AssignmentNode(new VariableReferenceNode("testVar"),
                new ConstantNode(new Token(Token.Type.NUMBER, "69", 1, 1)));

        Assert.assertEquals("69", inter.TEST_getIDT(an, vars).getValue());
        Assert.assertEquals("69", vars.get("testVar").getValue());
    }

    @Test
    public void getIDTFromConstantNode() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        ConstantNode cn = new ConstantNode(new Token(Token.Type.NUMBER, "666", 1, 1));
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();

        Assert.assertEquals("666", inter.TEST_getIDT(cn, vars).getValue());
    }

    @Test
    public void getIDTFromFunctionCall() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}\n" +
                "function foo() {return \"\"}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        FunctionCallNode fcn = new FunctionCallNode("foo", new LinkedList<Node>());
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();

        Assert.assertEquals("", inter.TEST_getIDT(fcn, vars).getValue());
    }

    @Test
    public void getIDTFromPatternNode() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        PatternNode pn = new PatternNode(new Token(Token.Type.PATTERN, "*", 1, 1));
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();

        try {
            inter.TEST_getIDT(pn, vars);
        } catch (Exception e) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void getIDTFromTernaryNode() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        TernaryNode tn = new TernaryNode(new ConstantNode(new Token(Token.Type.NUMBER, "1", 1, 1)),
                new ConstantNode(new Token(Token.Type.NUMBER, "true", 1, 1)),
                new ConstantNode(new Token(Token.Type.NUMBER, "false", 1, 1)));
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();

        Assert.assertEquals("true", inter.TEST_getIDT(tn, vars).getValue());

        tn = new TernaryNode(new ConstantNode(new Token(Token.Type.NUMBER, "true (ie, false :)", 1, 1)),
                new ConstantNode(new Token(Token.Type.NUMBER, "true", 1, 1)),
                new ConstantNode(new Token(Token.Type.NUMBER, "false", 1, 1)));

        Assert.assertEquals("false", inter.TEST_getIDT(tn, vars).getValue());
    }

    @Test
    public void getIDTFromVariableReferenceNode() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");

        VariableReferenceNode vrn = new VariableReferenceNode("test");
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();
        vars.put("test", toIDT("correct"));

        Assert.assertEquals("correct", inter.TEST_getIDT(vrn, vars).getValue());

        vrn = new VariableReferenceNode("testArray", new ConstantNode(new Token(Token.Type.NUMBER, "2", 1, 1)));
        vars.put("testArray", new InterpreterArrayDataType(
                new String[] { "not this one", "Not this either", "yes this one", "Nope too far" }));

        Assert.assertEquals("yes this one", inter.TEST_getIDT(vrn, vars).getValue());
    }

    @Test
    public void getIDTFromOperationNode() throws Exception {
        Lexer lex = new Lexer("{print $2  \" \"$2}");
        Parser parse = new Parser(lex.lex());
        Interpreter inter = new Interpreter(parse.parse(), "/home/danny/GitShit/ICSI311/test3.txt");
        HashMap<String, InterpreterDataType> vars = new HashMap<String, InterpreterDataType>();
        vars.put("tests", new InterpreterArrayDataType(new String[] { "correct" }));
        vars.put("testNum", toIDT(3));

        OperationNode on = new OperationNode(toConstantNode(3), OperationNode.Operation.ADD, toConstantNode(3));
        Assert.assertEquals("6.0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(3), OperationNode.Operation.AND, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode(0), OperationNode.Operation.AND, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode("This and "), OperationNode.Operation.CONCATENATION,
                toConstantNode("that"));
        Assert.assertEquals("This and that", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(69), OperationNode.Operation.DIVIDE, toConstantNode(3));
        Assert.assertEquals("23.0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(3), OperationNode.Operation.EQ, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode(3.0f), OperationNode.Operation.EQ, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode("many"), OperationNode.Operation.EQ, toConstantNode("many"));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode("many"), OperationNode.Operation.EQ, toConstantNode("more"));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(3), OperationNode.Operation.EXPONENT, toConstantNode(3));
        Assert.assertEquals("27.0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(3), OperationNode.Operation.GE, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode(4), OperationNode.Operation.GE, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode(2), OperationNode.Operation.GE, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(3), OperationNode.Operation.GT, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode(4), OperationNode.Operation.GT, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode(2), OperationNode.Operation.GT, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(3), OperationNode.Operation.LE, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode(4), OperationNode.Operation.LE, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode(2), OperationNode.Operation.LE, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(3), OperationNode.Operation.LT, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode(4), OperationNode.Operation.LT, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode(2), OperationNode.Operation.LT, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(0), OperationNode.Operation.IN, new VariableReferenceNode("tests"));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(new PatternNode(new Token(Token.Type.PATTERN, "[0-9]*.p", 0, 0)),
                OperationNode.Operation.MATCH, toConstantNode("hi 2gamma.p!"));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(4), OperationNode.Operation.MODULO, toConstantNode(3));
        Assert.assertEquals("1.0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(3), OperationNode.Operation.MULTIPLY, toConstantNode(3));
        Assert.assertEquals("9.0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(3), OperationNode.Operation.NE, toConstantNode(3));
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());
        on = new OperationNode(toConstantNode(4), OperationNode.Operation.NE, toConstantNode(3));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(new PatternNode(new Token(Token.Type.PATTERN, "[1-9]*.p", 0, 0)),
                OperationNode.Operation.MATCH, toConstantNode("hi 2gamma.p!"));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(3), OperationNode.Operation.OR, toConstantNode(0));
        Assert.assertEquals("1", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(3), OperationNode.Operation.SUBTRACT, toConstantNode(2));
        Assert.assertEquals("1.0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(3), OperationNode.Operation.NOT);
        Assert.assertEquals("0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(new VariableReferenceNode("testNum"), OperationNode.Operation.POSTDEC);
        Assert.assertEquals("3.0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(new VariableReferenceNode("testNum"), OperationNode.Operation.POSTINC);
        Assert.assertEquals("2.0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(new VariableReferenceNode("testNum"), OperationNode.Operation.PREDEC);
        Assert.assertEquals("2.0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(new VariableReferenceNode("testNum"), OperationNode.Operation.PREINC);
        Assert.assertEquals("3.0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(69), OperationNode.Operation.UNARYNEG);
        Assert.assertEquals("-69.0", inter.TEST_getIDT(on, vars).getValue());

        on = new OperationNode(toConstantNode(-3), OperationNode.Operation.UNARYPOS);
        Assert.assertEquals("3.0", inter.TEST_getIDT(on, vars).getValue());
    }

    @Test
    public void ReturnType() throws Exception {
        ReturnType retty = new ReturnType(ReturnType.Result.BREAK);
        Assert.assertEquals("BREAK", retty.toString());
        Assert.assertFalse(retty.hasValue());
        Assert.assertEquals(ReturnType.Result.BREAK, retty.getResult());

        retty = new ReturnType(ReturnType.Result.RETURN, "I am the return variable");
        Assert.assertEquals("RETURN \"I am the return variable\"", retty.toString());
        Assert.assertTrue(retty.hasValue());
        Assert.assertEquals("I am the return variable", retty.getValue().get());
        Assert.assertEquals(ReturnType.Result.RETURN, retty.getResult());
    }

    // #region Quality of Life Functions

    private InterpreterDataType toIDT(String value) {
        return new InterpreterDataType(value);
    }

    private InterpreterDataType toIDT(int value) {
        return new InterpreterDataType(Integer.toString(value));
    }

    private ConstantNode toConstantNode(String value) {
        return new ConstantNode(new Token(Token.Type.STRINGLITERAL, value, 0, 0));
    }

    private ConstantNode toConstantNode(int value) {
        return new ConstantNode(new Token(Token.Type.NUMBER, Integer.toString(value), 0, 0));
    }

    private ConstantNode toConstantNode(float value) {
        return new ConstantNode(new Token(Token.Type.NUMBER, Float.toString(value), 0, 0));
    }

    // #endregion
}
