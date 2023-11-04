//import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
//import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
//import java.math.*;

public class Interpreter {

    // TODO temperary public for testing
    public LineManager lm;
    public HashMap<String, InterpreterDataType> globalVars;
    public HashMap<String, FunctionDefinitionNode> functions;

    Interpreter(ProgramNode pNode, String filePath) throws Exception{
        globalVars = new HashMap<String, InterpreterDataType>();
        globalVars.put("FS", toIDT(" "));
        globalVars.put("OFMT", toIDT("%.6g"));
        globalVars.put("OFS", toIDT(" "));
        globalVars.put("ORS", toIDT("\n"));
        globalVars.put("FILENAME", toIDT(filePath));
        globalVars.put("NF", toIDT(0));
        globalVars.put("FNR", toIDT(0));
        globalVars.put("NR", toIDT(0));
        
        //try{
            /*
            Path myPath = Paths.get(fileName);
            String file = new String(Files.readAllBytes(myPath));
             */
            Path myPath = Paths.get(filePath);
            String file = new String(Files.readAllBytes(myPath));
            LinkedList<String> lines = new LinkedList<String>();
            for(String s : file.split("\n"))
                lines.add(s);
            
            lm = new LineManager(lines);
        /*}
        catch(IOException e){
            throw new Exception("TEST: Files didn't work");
            //lm = new LineManager(new LinkedList<String>());
        }*/
        functions = new HashMap<String, FunctionDefinitionNode>();
        LinkedList<FunctionDefinitionNode> functionList = pNode.getFunctionNodes();
        for(FunctionDefinitionNode n : functionList)
            functions.put(n.getName(), n);

        initializeBIFDNs();
    }

    private InterpreterDataType getIDT(Node n, HashMap<String, InterpreterDataType> localVar) throws Exception{
        InterpreterDataType retval;
        if(n instanceof AssignmentNode){
            AssignmentNode assignment = ((AssignmentNode)n);
            Node left = assignment.getLeft();
            String variableName;
            
            if(left instanceof VariableReferenceNode){
                variableName = ((VariableReferenceNode) left).getName();
                retval = getIDT(assignment.getRight(), localVar);
                localVar.replace(variableName, retval);
            }
            else if (left instanceof OperationNode && ((OperationNode)left).getOperation() == OperationNode.Operation.DOLLAR){
                variableName = '$' + getIDT(((OperationNode)left).getLeft(), localVar).getValue();
                retval = getIDT(assignment.getRight(), localVar);
                localVar.replace(variableName, retval);
            }
            else
                throw new Exception("Expected a Variable Reference as left node of Assignment.");
        }
        else if(n instanceof ConstantNode){
            ConstantNode constant = ((ConstantNode) n);
            retval = toIDT(constant.getValue());
        }
        else if(n instanceof FunctionCallNode){
            FunctionCallNode funcitionCall = ((FunctionCallNode) n);
            retval = toIDT(runFunctionCall(funcitionCall, localVar));
        }
        else if(n instanceof PatternNode){
            throw new Exception("Unexpected Pattern");
        }
        else if(n instanceof TernaryNode){
            TernaryNode ternary = (TernaryNode) n;
            InterpreterDataType booleanExpression = getIDT(ternary.getExpression(), localVar);
            // I felt that using a ternary was fitting. 
            retval = booleanExpression.toBoolean() ? getIDT(ternary.getTrueCase(), localVar) : getIDT(ternary.getFalseCase(), localVar);
        }
        else if(n instanceof VariableReferenceNode){
            VariableReferenceNode varReference = (VariableReferenceNode) n;
            if (varReference.isArray()){
                // Get IDT from HashMaps.
                InterpreterDataType temp;

                if (localVar.containsKey(varReference.getName()))
                    temp = localVar.get(varReference.getName());
                else if (globalVars.containsKey(varReference.getName()))
                    temp = globalVars.get(varReference.getName());
                else 
                    throw new Exception("The variable " + varReference.getName() + " was never initialized.");
                
                // Make sure the the IDT is an array.
                InterpreterArrayDataType iadt;
                if (temp instanceof InterpreterArrayDataType)
                    iadt = (InterpreterArrayDataType) temp;
                else
                    throw new Exception("The variable " + varReference.getName() + " cannot be called as an array entry.");

                // Get correct entry
                InterpreterDataType entryIndexIDT = getIDT(varReference.getIndex().get(), localVar);
                String index = entryIndexIDT.getValue();
                if (iadt.contains(index))
                    retval = iadt.getValue(index);
                else
                    throw new Exception("The array " + varReference.getName() + " does not contain an entry in the index " + index);
            }
            else{
                if(localVar.containsKey(varReference.getName())){
                    retval = localVar.get(varReference.getName());
                }
                else if (globalVars.containsKey(varReference.getName())){
                    retval = globalVars.get(varReference.getName());
                }
                else{
                    throw new Exception("The variable " + varReference.getName() + " was never initialized.");
                }
            }
        }
        else if(n instanceof OperationNode){

            OperationNode operation = (OperationNode) n;
            InterpreterDataType leftIDT = getIDT(operation.getLeft(), localVar);

            if (operation.hasRight()){
                InterpreterDataType rightIDT = getIDT(operation.getRight().get(), localVar);
                switch (operation.getOperation()){
                    case ADD:
                        retval = toIDT(leftIDT.toFloat() + rightIDT.toFloat());
                        break;

                    case AND:
                        if (leftIDT.toBoolean() && rightIDT.toBoolean())
                            retval = toIDT(1);
                        else
                            retval = toIDT(0);
                        break;

                    case CONCATENATION:
                        retval = toIDT(leftIDT.getValue() + rightIDT.getValue());
                        break;

                    case DIVIDE:
                        retval = toIDT(leftIDT.toFloat() / rightIDT.toFloat());
                        break;

                    case EQ:
                        // Use string compare because if they're both the same number value they will still have the same string.
                        retval = toIDT(leftIDT.toString().equals(rightIDT.toString()));
                        break;

                    case EXPONENT:
                        retval = toIDT((float) Math.pow(leftIDT.toFloat(), rightIDT.toFloat()));
                        break;

                    case GE:

                        break;

                    case GT:

                        break;

                    case LE:

                        break;

                    case LT:

                        break;

                    case IN:

                    
                    case MATCH:
                        PatternNode patNode;
                        if (operation.getLeft() instanceof PatternNode)
                            patNode = (PatternNode) operation.getLeft();
                        else
                            throw new Exception("Expected a pattern token.");

                        Pattern pattern = Pattern.compile(patNode.getPattern());
                        Matcher matcher = pattern.matcher(rightIDT.getValue());

                        retval = toIDT(matcher.find());
                        break;

                    case MODULO:
                        retval = toIDT(leftIDT.toFloat() % rightIDT.toFloat());
                        break;

                    case MULTIPLY:
                        retval = toIDT(leftIDT.toFloat() * rightIDT.toFloat());
                        break;

                    case NE:
                        if (leftIDT.toString().equals(rightIDT.toString()))
                            retval = toIDT(0);
                        else
                            retval = toIDT(1);
                        break;

                    case NOTMATCH:
                        PatternNode patNode;
                        if (operation.getLeft() instanceof PatternNode)
                            patNode = (PatternNode) operation.getLeft();
                        else
                            throw new Exception("Expected a pattern token.");

                        Pattern pattern = Pattern.compile(patNode.getPattern());
                        Matcher matcher = pattern.matcher(rightIDT.getValue());

                        retval = toIDT(!matcher.find());
                        break;

                    case OR:
                        if (leftIDT.toBoolean() || rightIDT.toBoolean())
                            retval = toIDT(1);
                        else
                            retval = toIDT(0);
                        break;

                    case SUBTRACT:
                        retval = toIDT(leftIDT.toFloat() - rightIDT.toFloat());
                        break;

                    default:
                        throw new Exception("Unexpected " + operation.getOperation() + "found");
                }
            }
            else {
                switch (operation.getOperation()){
                    case DOLLAR:
                        retval = globalVars.get(leftIDT.toString());
                        if (retval == null){
                            throw new Exception("The item $" + leftIDT.toString() + " does not exist in this enviornment.");
                        }
                        break;
                    case NOT:
                        // If the operation returns true return a false IDT, otherwize return true.
                        retval = leftIDT.toBoolean() ? toIDT("0") : toIDT("1");
                        break;
                    case POSTDEC:
                        if (operation.getLeft() instanceof VariableReferenceNode){
                            retval = toIDT(leftIDT.toFloat());
                            VariableReferenceNode var = (VariableReferenceNode) operation.getLeft();
                            if (localVar.containsKey(var.getName()))
                                localVar.replace(var.getName(), toIDT(leftIDT.toFloat() - 1));
                            else if (globalVars.containsKey(var.getName()))
                                globalVars.replace(var.getName(), toIDT(leftIDT.toFloat() - 1));
                            else
                                throw new Exception("The variable " + var.getName() + " has not been initialized");
                        }
                        else{
                            throw new Exception("The post-dec operator can only be used on a variable");
                        }
                        break;
                    case POSTINC:
                        if (operation.getLeft() instanceof VariableReferenceNode){
                            retval = toIDT(leftIDT.toFloat());
                            VariableReferenceNode var = (VariableReferenceNode) operation.getLeft();
                            if (localVar.containsKey(var.getName()))
                                localVar.replace(var.getName(), toIDT(leftIDT.toFloat() + 1));
                            else if (globalVars.containsKey(var.getName()))
                                globalVars.replace(var.getName(), toIDT(leftIDT.toFloat() + 1));
                            else
                                throw new Exception("The variable " + var.getName() + " has not been initialized");
                        }
                        else{
                            throw new Exception("The post-dec operator can only be used on a variable");
                        }
                        break;
                    case PREDEC:
                        if (operation.getLeft() instanceof VariableReferenceNode){
                            retval = toIDT(leftIDT.toFloat() - 1);
                            VariableReferenceNode var = (VariableReferenceNode) operation.getLeft();
                            if (localVar.containsKey(var.getName()))
                                localVar.replace(var.getName(), toIDT(leftIDT.toFloat() - 1));
                            else if (globalVars.containsKey(var.getName()))
                                globalVars.replace(var.getName(), toIDT(leftIDT.toFloat() - 1));
                            else
                                throw new Exception("The variable " + var.getName() + " has not been initialized");
                        }
                        else{
                            throw new Exception("The post-dec operator can only be used on a variable");
                        }
                        break;
                    case PREINC:
                        if (operation.getLeft() instanceof VariableReferenceNode){
                            retval = toIDT(leftIDT.toFloat() + 1);
                            VariableReferenceNode var = (VariableReferenceNode) operation.getLeft();
                            if (localVar.containsKey(var.getName()))
                                localVar.replace(var.getName(), toIDT(leftIDT.toFloat() + 1));
                            else if (globalVars.containsKey(var.getName()))
                                globalVars.replace(var.getName(), toIDT(leftIDT.toFloat() + 1));
                            else
                                throw new Exception("The variable " + var.getName() + " has not been initialized");
                        }
                        else{
                            throw new Exception("The post-dec operator can only be used on a variable");
                        }
                        break;
                    case UNARYNEG:
                        retval = toIDT(- leftIDT.toFloat());
                        break;
                    case UNARYPOS:
                        retval = toIDT(+ leftIDT.toFloat());
                        break;
                    default:
                        throw new Exception("Unexpected " + operation.getOperation() + "found");
                }
            }
        }
        return retval;
    }

    private String runFunctionCall(FunctionCallNode fcn, HashMap<String, InterpreterDataType> localVars){
        // TODO: later
        return "";
    }

    private void initializeBIFDNs(){
        String[] args;  // Using an array of strings to hold the args because it's easier than makeing a LinkedList. It will be changed to a LinkeList in the toBIFDN method.
        Function<HashMap<String, InterpreterDataType>, String> temp;
        
        //print
        temp = (hm)->{// (array)
            String[] array; // Stores the array entries to pass to printf
            InterpreterArrayDataType IADTarray; // Stores the IADT passed to the function.
            // Make sure that the item passed is an IADT.
            if(hm.get("array") instanceof InterpreterArrayDataType){
                IADTarray = ((InterpreterArrayDataType)hm.get("array"));
                array = new String[IADTarray.getSize()];
            }
            else
                return "";
            // Put all the entries into array
            for(int i=0; i<array.length; i++)
                array[i] = IADTarray.getValue(toString(i)).getValue();

            // Print them.
            System.out.print(array); 
            return "";
        };
        args = new String[]{"array"};
        functions.put("print", toBIFDN("print", temp, true, args));
        
        //printf
        temp = (hm)->{// (format, array)
            String[] array; // Stores the array entries to pass to printf
            InterpreterArrayDataType IADTarray; // Stores the IADT passed to the function.
            // Make sure that the item passed is an IADT.
            if(hm.get("array") instanceof InterpreterArrayDataType){
                IADTarray = ((InterpreterArrayDataType)hm.get("array"));
                array = new String[IADTarray.getSize()];
            }
            else
                return "";
            for(int i=0; i<array.length; i++)
                array[i] = IADTarray.getValue(toString(i)).getValue();

            System.out.printf(hm.get("format").getValue(), (Object) array); 
            return "";
        };
        args = new String[]{"format", "aray"};
        functions.put("printf", toBIFDN("printf", temp, true, args));
        
        // getline
        temp = (hm)->{lm.splitAndAssign(); return "1";};
        args = new String[]{};
        functions.put("getline", toBIFDN("getline", temp, false, args));
        
        // next
        temp = (hm)->{lm.splitAndAssign(); return "1";};
        args = new String[]{};
        functions.put("next", toBIFDN("next", temp, false, args));
        
        // gsub
        temp = (hm) -> {//(regexp, replacement [, target])
            Pattern regex = Pattern.compile(hm.get("regexp").getValue());
            Matcher matcher;    // Initialize here to get a matcher with the chosen target out of the if blocks.
            String target;
            // If the target is specificed, search the target instead of the current line.
            if (hm.containsKey("target"))
                target = hm.get("target").getValue();
            else
                target = "$0";
            
            matcher = regex.matcher(globalVars.get(target).getValue()); // Send the right string to the matcher.

            int replacements = matcher.groupCount();

            String replaced = matcher.replaceAll(hm.get("replacement").getValue());

            globalVars.replace(target, toIDT(replaced));

            return toString(replacements);
        };
        args = new String[]{"regexp", "replacement", "target"};
        functions.put("gsub", toBIFDN("gsub", temp, true, args));
        
        // index
        temp = (hm)->{//(in, find)
            return toString(hm.get("in").getValue().indexOf(hm.get("find").getValue()));
        };
        args = new String[]{"in", "find"};
        functions.put("index", toBIFDN("index", temp, false, args));
        
        // length
        temp = (hm)->{//([string])
            if(hm.containsKey("string")){
                return toString(hm.get("string").getValue().length());
            }  
            else{
                return toString(globalVars.get("$0").getValue().length());
            }
        };
        args = new String[]{"string"};
        functions.put("length", toBIFDN("length", temp, true, args));
        
        // match
        temp = (hm)->{//(string, regexp [, array])
            Pattern regex = Pattern.compile(hm.get("regexp").getValue());
            Matcher matcher = regex.matcher(hm.get("string").getValue());

            if(matcher.find()){
                if(hm.containsKey("array")){
                    globalVars.replace(hm.get("array").getValue(), toIDT(hm.get("String").getValue().substring(matcher.start(), matcher.end())));
                    return toString(matcher.start()+1);  // Add one because AWK returns "0" for not found, and uses 1 as the first index.
                }
                else{
                    return toString(matcher.start()+1);  // Add one because AWK returns "0" for not found, and "1" if found at first index.
                }
            }
            else{
                return toString(0);
            }
        };
        args = new String[]{"string", "regexp", "array"};
        functions.put("match", toBIFDN("match", temp, true, args));
        
        // split
        temp = (hm)->{//(string, array [, fieldsep [, seps ]])
            String string = hm.get("string").getValue();
            String[] array; 
            String seperator = globalVars.get("FS").getValue();

            // Check if the user wants a different seperator.
            if(hm.containsKey("fieldsep"))
                seperator = hm.get("fieldsep").getValue();
            
            array = string.split(seperator);
            globalVars.replace(hm.get("array").getValue(), new InterpreterArrayDataType(array));
            return toString(array.length);
        };
        args = new String[]{"string", "array", "fieldsep", "seps"};
        functions.put("split", toBIFDN("split", temp, true, args));
        
        // sprintf
        temp = (hm)->{//(format, array)
            String[] array;
            InterpreterArrayDataType IADTarray;
            if(hm.get("array") instanceof InterpreterArrayDataType){
                IADTarray = ((InterpreterArrayDataType)hm.get("array"));
                array = new String[IADTarray.getSize()];
            }
            else
                return "false";
            for(int i=0; i<array.length; i++)
                array[i] = (String) IADTarray.getValue(toString(i)).getValue();

            return String.format(hm.get("format").getValue(), (String[]) array); 
        };
        args = new String[]{"format", "array"};
        functions.put("sprintf", toBIFDN("sprintf", temp, true, args));
        
        // sub
        temp = (hm)->{//(regexp, replacement [, target])
            Pattern regex = Pattern.compile(hm.get("regexp").getValue());
            Matcher matcher;    // Initialize here to get a matcher with the chosen target out of the if blocks.
            String target;
            // If the target is specificed, search the target instead of the current line.
            if (hm.containsKey("target"))
                target = hm.get("target").getValue();
            else
                target = "$0";
            
            matcher = regex.matcher(globalVars.get(target).getValue()); // Send the right string to the matcher.

            int replacements = matcher.find() ? 1 : 0;  // If we find something, we return a value of 1, otherwize, 0

            String replaced = matcher.replaceFirst(hm.get("replacement").getValue());

            globalVars.replace(target, toIDT(replaced));

            return toString(replacements);
        };
        args = new String[]{"regexp", "replacement", "target"};
        functions.put("sub", toBIFDN("sub", temp, true, args));
        
        // substr
        temp = (hm)->{//(string, start [, length ])
            if(hm.containsKey("length")){
                return hm.get("string").getValue().substring(Integer.parseInt(hm.get("start").getValue()), Integer.parseInt(hm.get("start").getValue())+Integer.parseInt(hm.get("length").getValue()));
            }
            else{
                return hm.get("string").getValue().substring(Integer.parseInt(hm.get("start").getValue()));
            }
        };
        args = new String[]{"string", "start", "length"};
        functions.put("substr", toBIFDN("substr", temp, true, args));
        
        // tolower
        temp = (hm)->{//(string)
            return hm.get("string").getValue().toLowerCase();
        };
        args = new String[]{"string"};
        functions.put("tolower", toBIFDN("tolower", temp, false, args));
       
        // toupper
        temp = (hm)->{//(string)
            return hm.get("string").getValue().toUpperCase();
        };
        args = new String[]{"string"};
        functions.put("toupper", toBIFDN("toupper", temp, false, args));
    }
    
    public class LineManager{
        List<String> file;
        int lineNum;

        LineManager(List<String> file){
            this.file = file;
            lineNum = 0;
        }

        boolean splitAndAssign(){
            // Make sure the line is valid 
            if (lineNum >= file.size())
                return false;
            
            globalVars.replace("$0", toIDT(file.get(lineNum)));

            String[] currentLine = file.get(lineNum).split(globalVars.get("FS").getValue());
            lineNum++;
            
            // Remove old values.
            for(int i = 1; i<=Integer.parseInt(globalVars.get("NF").getValue()); i++)
                globalVars.remove("$" + i);

            // Update NR and FNR
            int FNR = Integer.parseInt(globalVars.get("FNR").getValue());
            globalVars.replace("FNR", toIDT(FNR++));
            int NR = Integer.parseInt(globalVars.get("NR").getValue());
            globalVars.replace("NR", toIDT(NR++));

            // Set the $i variables.
            for(int i = 0; i<currentLine.length; i++)
                globalVars.replace("$" + i+1, toIDT(currentLine[i]));

            // Update NF
            globalVars.replace("NF", toIDT(Integer.toString(currentLine.length)));
            return true;
        }
    }

    // Quality of life methods:
    private InterpreterDataType toIDT(String value){
        return new InterpreterDataType(value);
    }

    private InterpreterDataType toIDT(int value){
        return new InterpreterDataType(Integer.toString(value));
    }

    private InterpreterDataType toIDT(float value){
        return new InterpreterDataType(Float.toString(value));
    }

    private InterpreterDataType toIDT(boolean value){
        return value ? new InterpreterDataType("1") : new InterpreterDataType("0");
    }

    private BuiltInFunctionDefinitionNode toBIFDN(String name, Function<HashMap<String, InterpreterDataType>, String> foo, boolean variadic, String[] args){
        LinkedList<String> argsList = new LinkedList<String>();
        for(String s: args)
            argsList.add(s);
        return new BuiltInFunctionDefinitionNode(name, foo, variadic, argsList);
    }

    /*private int IDTtoInt(InterpreterDataType idt) throws Exception{
        try{
            int retval = Integer.parseInt(idt.getValue());
            return retval;
        }  
        catch(NumberFormatException e){
            throw new Exception("Expected an Integer, but was actually: " + idt.getValue());
        }
    }*/

    private String toString(int value){
        return Integer.toString(value);
    }

    /*private static String toString(float value){
        return Float.toString(value);
    }*/

    /*private float toFloat(InterpreterDataType idt)throws Exception{
        try{
            return Float.parseFloat(idt.getValue());
        }
        catch (NumberFormatException e){
            throw new Exception("Expected a parseable float instead of \"" + idt.getValue() + "\"");
        }

    }*/

    /*private boolean toBoolean(String value){
        try{
            float fakeBoolean = Float.parseFloat(value);
            if(fakeBoolean == 0)
                return false;
            else return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }*/
}
