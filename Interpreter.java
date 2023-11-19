import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Interpreter {

    private LineManager lm;
    private HashMap<String, InterpreterDataType> globalVars;
    private HashMap<String, FunctionDefinitionNode> functions;
    private ProgramNode prog;

    Interpreter(ProgramNode pNode, String filePath) throws Exception{

        prog = pNode;

        globalVars = new HashMap<String, InterpreterDataType>();
        globalVars.put("FS", toIDT(" "));
        globalVars.put("OFMT", toIDT("%.6g"));
        globalVars.put("OFS", toIDT(" "));
        globalVars.put("ORS", toIDT("\n"));
        globalVars.put("FILENAME", toIDT(filePath));
        globalVars.put("NF", toIDT(0));
        globalVars.put("FNR", toIDT(0));
        globalVars.put("NR", toIDT(0));
        
        try{
            Path myPath = Paths.get(filePath);
            String file = new String(Files.readAllBytes(myPath));

                LinkedList<String> lines = new LinkedList<String>();

            for(String s : file.split("\n"))
                lines.add(s);
            
            lm = new LineManager(lines);
        }
        catch(IOException e){
            lm = new LineManager(/**/new LinkedList<String>()/**/);
        }
        
        functions = new HashMap<String, FunctionDefinitionNode>();
        LinkedList<FunctionDefinitionNode> functionList = prog.getFunctionNodes();
        for(FunctionDefinitionNode n : functionList)
            functions.put(n.getName(), n);

        initializeBIFDNs();
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
            for(String s : array)
                System.out.print(s); 
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

    public void interpretProgram() throws Exception{
        // BeginNodes
        LinkedList<Node> blocks = prog.getBeginNodes();

        for(Node b : blocks)
            interpretBlock((BlockNode) b);

        blocks = prog.getOtherNodes();
        while (lm.splitAndAssign()){
            for(Node b : blocks){
                interpretBlock((BlockNode) b);
            }
        }
        

        blocks = prog.getEndNodes();
        for(Node b : blocks)
            interpretBlock((BlockNode) b);
    }

    private void interpretBlock(BlockNode block) throws Exception{
        if(block.getCondition().isEmpty() || getIDT(block.getCondition().get(), globalVars).toBoolean()){
            processStatementList(block.getStatements(), new HashMap<String, InterpreterDataType>());
        }
    }

    private ReturnType processStatementList(LinkedList<StatementNode> statements, HashMap<String, InterpreterDataType> localVars) throws Exception {
        for(StatementNode s : statements){
            ReturnType retval = processStatement(localVars, s);    // Store the result to check and return if needed.

            if (retval.getResult() != ReturnType.Result.NORMAL)
                return retval;
        }

        return new ReturnType(ReturnType.Result.NORMAL);    // If nothing is found, return NORMAL result.
    }

    private ReturnType processStatement(HashMap<String, InterpreterDataType> localVars, StatementNode statement) throws Exception{
        // Check for each type of statement.
        if (statement instanceof BreakNode){
            return new ReturnType(ReturnType.Result.BREAK);
        }
        else if (statement instanceof ContinueNode){
            return new ReturnType(ReturnType.Result.CONTINUE);
        }
        else if (statement instanceof DeleteNode){

            VariableReferenceNode deleteMe = ((DeleteNode) statement).getDeletedVariable();
            HashMap<String, InterpreterDataType> tempMap;

            // Get the right variable map.
            if(localVars.containsKey(deleteMe.getName()))
                tempMap = localVars;
            else if (globalVars.containsKey(deleteMe.getName()))
                tempMap = globalVars;
            else
                throw new Exception("The variable " + deleteMe.getName() + " cannot be deleted because it does not exist.");
            
            // Check if it is an array reference.
            if (deleteMe.isArray() && tempMap.get(deleteMe.getName()) instanceof InterpreterArrayDataType){
                InterpreterArrayDataType iadt = (InterpreterArrayDataType) tempMap.get(deleteMe.getName());
                iadt.remove(getIDT(deleteMe.getIndex().get(), localVars).getValue());
            }
            else 
                tempMap.remove(deleteMe.getName());

            return new ReturnType(ReturnType.Result.NORMAL);
        }
        else if (statement instanceof DoWhileNode){
            DoWhileNode doWhileNode = (DoWhileNode) statement;
            ReturnType retval;

            do{
                retval = processStatementList(doWhileNode.getStatements(), localVars);
            } 
            while(getIDT(doWhileNode.getCondition(), localVars).toBoolean());

            return retval;
        }
        else if (statement instanceof ForNode){

            ForNode fNode = (ForNode) statement;
            ReturnType retval = new ReturnType(ReturnType.Result.NORMAL);

            if (fNode.getInitialization() instanceof StatementNode)
                processStatement(localVars, (StatementNode)fNode.getInitialization());
            
            while(getIDT(fNode.getCondition(), localVars).toBoolean()){
                retval = processStatementList(fNode.getStatements(), localVars);
                getIDT(fNode.getIterator(), localVars);   // Update the iterator.
            }

            return retval;
        }
        else if (statement instanceof ForEachNode){

            ForEachNode forEachNode = (ForEachNode) statement;
            HashMap<String, InterpreterDataType> tempMap;
            ReturnType retval = new ReturnType(ReturnType.Result.NORMAL);

            // Get the correct list.
            if(localVars.containsKey(forEachNode.getList().getName()) && localVars.get(forEachNode.getList().getName()) instanceof InterpreterArrayDataType){
                tempMap = ((InterpreterArrayDataType) localVars.get(forEachNode.getList().getName())).getMap();
            }
            else if (globalVars.containsKey(forEachNode.getList().getName()) && globalVars.get(forEachNode.getList().getName()) instanceof InterpreterArrayDataType){
                tempMap = ((InterpreterArrayDataType) globalVars.get(forEachNode.getList().getName())).getMap();
            }
            else
                throw new Exception("The list \"" + forEachNode.getList().getName() + "\" cannot be resolved");

            String current = "0";
            while(Integer.parseInt(current) < tempMap.size()){
                localVars.put(forEachNode.getIterator().getName(), tempMap.get(current));    // Store the current array index into the iterator.

                retval = processStatementList(forEachNode.getStatements(), localVars);

                current = Integer.toString(Integer.parseInt(current) + 1);
            }

            return retval;
        }
        else if (statement instanceof IfNode){

            IfNode ifNode = (IfNode) statement;

            // Loop thru each if statement until we find one that evaluates to true
            while (ifNode.getCondition().isPresent() && getIDT(ifNode.getCondition().get(), localVars).toBoolean() == false){
                if(ifNode.getNext().isPresent())
                    ifNode = ifNode.getNext().get();
                else 
                    return new ReturnType(ReturnType.Result.NORMAL); 
            }

            return processStatementList(ifNode.getStatements(), localVars);
        }
        else if (statement instanceof ReturnNode){
            ReturnNode retNode = (ReturnNode) statement;

            return new ReturnType(ReturnType.Result.RETURN, getIDT(retNode.getReturnValue(), localVars).getValue());
        }
        else if (statement instanceof WhileNode){

            WhileNode whileNode = (WhileNode) statement;
            ReturnType retval = new ReturnType(ReturnType.Result.NORMAL);

            while(getIDT(whileNode.getCondition(), localVars).toBoolean()){
                processStatementList(whileNode.getStatements(), localVars);
            } 

            return retval;
        }
        else{
            getIDT(statement, localVars); // Make sure that getIDT returns a valid value.
            return new ReturnType(ReturnType.Result.NORMAL);
        }
    }

    private InterpreterDataType getIDT(Node n, HashMap<String, InterpreterDataType> localVar) throws Exception{
        if(n instanceof AssignmentNode){
            AssignmentNode assignment = ((AssignmentNode)n);
            Node left = assignment.getLeft();
            String variableName;
            InterpreterDataType retval;

            if(left instanceof VariableReferenceNode){
                variableName = ((VariableReferenceNode) left).getName();
                retval = getIDT(assignment.getRight(), localVar);
                // Replace it in the HashMap, if it already exists, otherwize, add to map.
                if(localVar.replace(variableName, retval) == null)
                    localVar.put(variableName, retval);
            }
            else if (left instanceof OperationNode && ((OperationNode)left).getOperation() == OperationNode.Operation.DOLLAR){
                variableName = '$' + getIDT(((OperationNode)left).getLeft(), localVar).getValue();
                retval = getIDT(assignment.getRight(), localVar);
                if(localVar.replace(variableName, retval) == null)
                    localVar.put(variableName, retval);
            }
            else
                throw new Exception("Expected a Variable Reference as left node of Assignment.");
            
            return retval;
        }
        else if(n instanceof ConstantNode){
            ConstantNode constant = ((ConstantNode) n);
            return toIDT(constant.getValue());
        }
        else if(n instanceof FunctionCallNode){
            FunctionCallNode funcitionCall = ((FunctionCallNode) n);
            return toIDT(runFunctionCall(funcitionCall, localVar));
        }
        else if(n instanceof PatternNode){
            throw new Exception("Unexpected Pattern");
        }
        else if(n instanceof TernaryNode){
            TernaryNode ternary = (TernaryNode) n;
            InterpreterDataType booleanExpression = getIDT(ternary.getExpression(), localVar);
            // I felt that using a ternary was fitting. 
            return booleanExpression.toBoolean() ? getIDT(ternary.getTrueCase(), localVar) : getIDT(ternary.getFalseCase(), localVar);
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
                    return iadt.getValue(index);
                else
                    throw new Exception("The array " + varReference.getName() + " does not contain an entry in the index " + index);
            }
            else{
                if(localVar.containsKey(varReference.getName())){
                    return localVar.get(varReference.getName());
                }
                else if (globalVars.containsKey(varReference.getName())){
                    return globalVars.get(varReference.getName());
                }
                else{
                    throw new Exception("The variable " + varReference.getName() + " was never initialized.");
                }
            }
        }
        else if(n instanceof OperationNode || n instanceof PrePostIterator){

            if (n instanceof PrePostIterator){
                n = ((PrePostIterator)n).getNode();
            }

            OperationNode operation = (OperationNode) n;
            InterpreterDataType leftIDT;
            // Looking at the left throws an exception if it's a pattern node, but if the left is a pattern node then it might be a match/notmatch.
            try{
                leftIDT = getIDT(operation.getLeft(), localVar);
            }
            catch(Exception e){
                if (operation.getOperation() == OperationNode.Operation.MATCH && operation.hasRight()){
                    if (!(operation.getLeft() instanceof PatternNode))
                            throw new Exception("Expected a pattern token.");

                        Pattern mPattern = Pattern.compile(((PatternNode) operation.getLeft()).getPattern());
                        Matcher mMatcher = mPattern.matcher(getIDT(operation.getRight().get(), localVar).getValue());

                        return toIDT(mMatcher.find());
                }
                else if (operation.getOperation() == OperationNode.Operation.NOTMATCH && operation.hasRight()){
                    if (!(operation.getLeft() instanceof PatternNode))
                            throw new Exception("Expected a pattern token.");

                        Pattern nmPattern = Pattern.compile(((PatternNode) operation.getLeft()).getPattern());
                        Matcher nmMatcher = nmPattern.matcher(getIDT(operation.getRight().get(), localVar).getValue());

                        return toIDT(!nmMatcher.find());
                }
                else{
                    throw e;
                }
            }

            if (operation.hasRight()){
                InterpreterDataType rightIDT = getIDT(operation.getRight().get(), localVar);
                switch (operation.getOperation()){
                    case ADD:
                        return toIDT(leftIDT.toFloat() + rightIDT.toFloat());

                    case AND:
                        return toIDT(leftIDT.toBoolean() && rightIDT.toBoolean());

                    case CONCATENATION:
                        return toIDT(leftIDT.getValue() + rightIDT.getValue());

                    case DIVIDE:
                        return toIDT(leftIDT.toFloat() / rightIDT.toFloat());

                    case EQ:
                        // Use string compare because if they're both the same number value they will still have the same string.
                        try{
                            return toIDT(leftIDT.toFloat() == rightIDT.toFloat());
                        }  
                        catch(Exception e){
                            return toIDT(leftIDT.getValue().compareTo(rightIDT.getValue()) == 0);
                        }

                    case EXPONENT:
                        return toIDT((float) Math.pow(leftIDT.toFloat(), rightIDT.toFloat()));

                    case GE:
                        // If one isn't a float, then it will throw and exception. So then we should just compare as strings.
                        try{
                            return toIDT(leftIDT.toFloat() >= rightIDT.toFloat());
                        }  
                        catch(Exception e){
                            return toIDT(leftIDT.getValue().compareTo(rightIDT.getValue()) >= 0);
                        }

                    case GT:
                        try{
                            return toIDT(leftIDT.toFloat() > rightIDT.toFloat());
                        }  
                        catch(Exception e){
                            return toIDT(leftIDT.getValue().compareTo(rightIDT.getValue()) > 0);
                        }

                    case LE:
                        try{
                            return toIDT(leftIDT.toFloat() <= rightIDT.toFloat());
                        }  
                        catch(Exception e){
                            return toIDT(leftIDT.getValue().compareTo(rightIDT.getValue()) <= 0);
                        }
                    
                    case LT:
                        try{
                            return toIDT(leftIDT.toFloat() < rightIDT.toFloat());
                        }  
                        catch(Exception e){
                            return toIDT(leftIDT.getValue().compareTo(rightIDT.getValue()) > 0);
                        }

                    case IN:
                        if(operation.getRight().get() instanceof VariableReferenceNode){
                            String rightVarName = ((VariableReferenceNode) operation.getRight().get()).getName();
                            if (localVar.containsKey(rightVarName) && localVar.get(rightVarName) instanceof InterpreterArrayDataType){
                                return toIDT(((InterpreterArrayDataType) localVar.get(rightVarName)).contains(leftIDT.getValue()));
                            }
                            else if(globalVars.containsKey(rightVarName) && globalVars.get(rightVarName) instanceof InterpreterArrayDataType){
                                return toIDT(((InterpreterArrayDataType) globalVars.get(rightVarName)).contains(leftIDT.getValue()));
                            }
                            else 
                                throw new Exception("The variable " + rightVarName + "could not be found.");
                        }
                        else
                            throw new Exception("Expected two variable references");
                    
                    /*case MATCH:
                        if (!(operation.getLeft() instanceof PatternNode))
                            throw new Exception("Expected a pattern token.");

                        Pattern mPattern = Pattern.compile(((PatternNode) operation.getLeft()).getPattern());
                        Matcher mMatcher = mPattern.matcher(rightIDT.getValue());

                        return toIDT(mMatcher.find());*
                    case NOTMATCH:
                        if (!(operation.getLeft() instanceof PatternNode))
                            throw new Exception("Expected a pattern token.");

                        Pattern nmPattern = Pattern.compile(((PatternNode) operation.getLeft()).getPattern());
                        Matcher nmMatcher = nmPattern.matcher(rightIDT.getValue());

                        return toIDT(!nmMatcher.find());
                        /**/
                    case MODULO:
                        return toIDT(leftIDT.toFloat() % rightIDT.toFloat());

                    case MULTIPLY:
                        return toIDT(leftIDT.toFloat() * rightIDT.toFloat());

                    case NE:
                        try{
                            return toIDT(leftIDT.toFloat() != rightIDT.toFloat());
                        }  
                        catch(Exception e){
                            return toIDT(leftIDT.getValue().compareTo(rightIDT.getValue()) != 0);
                        }
                    
                    case OR:
                        return toIDT(leftIDT.toBoolean() || rightIDT.toBoolean());

                    case SUBTRACT:
                        return toIDT(leftIDT.toFloat() - rightIDT.toFloat());

                    default:
                        throw new Exception("Unexpected " + operation.getOperation() + "found");
                }
            }
            else {
                if (operation.getOperation() == OperationNode.Operation.DOLLAR){
                    InterpreterDataType retval = globalVars.get("$" + leftIDT.getValue());
                    if (retval == null){
                        throw new Exception("The item $" + leftIDT.getValue() + " does not exist in this enviornment in line " + lm.getLineNumber());
                    }
                    return retval;
                }
                else if (operation.getOperation() == OperationNode.Operation.NOT){
                    // If the operation returns true return a false IDT, otherwize return true.
                    return leftIDT.toBoolean() ? toIDT("0") : toIDT("1");
                }
                else if (operation.getOperation() == OperationNode.Operation.POSTDEC){
                    if (operation.getLeft() instanceof VariableReferenceNode){
                        InterpreterDataType retval = toIDT(leftIDT.toFloat());
                        VariableReferenceNode var = (VariableReferenceNode) operation.getLeft();

                        if (localVar.containsKey(var.getName()))
                            localVar.replace(var.getName(), toIDT(leftIDT.toFloat() - 1));
                        else if (globalVars.containsKey(var.getName()))
                            globalVars.replace(var.getName(), toIDT(leftIDT.toFloat() - 1));
                        else
                            throw new Exception("The variable " + var.getName() + " has not been initialized");

                        return retval;
                        }
                    else
                        throw new Exception("The post-dec operator can only be used on a variable");
                }
                else if (operation.getOperation() == OperationNode.Operation.POSTINC){
                    if (operation.getLeft() instanceof VariableReferenceNode){
                        InterpreterDataType retval = toIDT(leftIDT.toFloat());
                        VariableReferenceNode var = (VariableReferenceNode) operation.getLeft();

                        if (localVar.containsKey(var.getName()))
                            localVar.replace(var.getName(), toIDT(leftIDT.toFloat() + 1));
                        else if (globalVars.containsKey(var.getName()))
                            globalVars.replace(var.getName(), toIDT(leftIDT.toFloat() + 1));
                        else
                            throw new Exception("The variable " + var.getName() + " has not been initialized");

                        return retval;
                    }
                    else
                        throw new Exception("The post-inc operator can only be used on a variable");
                }
                else if (operation.getOperation() == OperationNode.Operation.PREDEC){
                    if (operation.getLeft() instanceof VariableReferenceNode){
                        InterpreterDataType retval = toIDT(leftIDT.toFloat() - 1);
                        VariableReferenceNode var = (VariableReferenceNode) operation.getLeft();

                        if (localVar.containsKey(var.getName()))
                            localVar.replace(var.getName(), toIDT(leftIDT.toFloat() - 1));
                        else if (globalVars.containsKey(var.getName()))
                            globalVars.replace(var.getName(), toIDT(leftIDT.toFloat() - 1));
                        else
                            throw new Exception("The variable " + var.getName() + " has not been initialized");

                        return retval;
                    }
                    else
                        throw new Exception("The pre-dec operator can only be used on a variable");
                }
                else if (operation.getOperation() == OperationNode.Operation.PREINC){
                    if (operation.getLeft() instanceof VariableReferenceNode){
                        InterpreterDataType retval = toIDT(leftIDT.toFloat() + 1);
                        VariableReferenceNode var = (VariableReferenceNode) operation.getLeft();

                        if (localVar.containsKey(var.getName()))
                            localVar.replace(var.getName(), toIDT(leftIDT.toFloat() + 1));
                        else if (globalVars.containsKey(var.getName()))
                            globalVars.replace(var.getName(), toIDT(leftIDT.toFloat() + 1));
                        else
                            throw new Exception("The variable " + var.getName() + " has not been initialized");
                        
                        return retval;
                    }
                    else
                        throw new Exception("The pre-inc operator can only be used on a variable");
                }
                else if (operation.getOperation() == OperationNode.Operation.UNARYNEG){
                    return toIDT(leftIDT.toFloat() < 0 ? leftIDT.toFloat() : -leftIDT.toFloat());
                }
                else if (operation.getOperation() == OperationNode.Operation.UNARYPOS){
                    return toIDT(leftIDT.toFloat() < 0 ? -leftIDT.toFloat() : leftIDT.toFloat());
                }                        
                else
                    throw new Exception("Unexpected " + operation.getOperation() + "found");
            }
        }
        else {
            throw new Exception("Unrecognized node passed to getIDT.");
        }
    }

    private String runFunctionCall(FunctionCallNode fcn, HashMap<String, InterpreterDataType> localVars)throws Exception{
        
        FunctionDefinitionNode func;

        if(functions.containsKey(fcn.getName()))
            func = functions.get(fcn.getName());
        else 
            throw new Exception("The function \"" + fcn.getName() + "\" has not been defined.");

        HashMap<String, InterpreterDataType> map = new HashMap<>();

        // Put things into the map in the right order.
        if (func.isVariadic()){
            for(int i =0; i<fcn.getParameters().size(); i++){
                if (func.getParameters().get(i).equals("array")){
                    // Put everything else into the array
                    InterpreterArrayDataType array = new InterpreterArrayDataType();

                    for(; i<fcn.getParameters().size(); i++){
                        array.add(getIDT(fcn.getParameters().get(i), localVars));
                    }
                    map.put("array", array);
                }
                else {
                    map.put(func.getParameters().get(i), getIDT(fcn.getParameters().get(i), localVars));
                }
            }
        }
        else if (fcn.getParameters().size() == func.getParameters().size()){
            for(int i =0; i<func.getParameters().size(); i++)
                map.put(func.getParameters().get(i), getIDT(fcn.getParameters().get(i), localVars));
        }
        else
            throw new Exception("Not enough parameters supplied to the function \"" + fcn.getName() + "\"");

        if (func instanceof BuiltInFunctionDefinitionNode)
            return ((BuiltInFunctionDefinitionNode)func).execute(map);
        else 
            return processStatementList(func.getStatements(), map).getValue().get();
    }

    public class LineManager{
        Optional<List<String>> fileInput;
        Optional<Scanner> scanner;
        int lineNum;

        /**
         * The standard constructor.
         * @param file The file to be read by the program.
         */
        LineManager(List<String> file){
            this.fileInput = Optional.of(file);
            scanner = Optional.empty();
            lineNum = 0;
        }

        /**
         * The InLine constructor.
         * This constructor opens a scanner so that the interpreter can read in lines from the terminal.
         */
        LineManager(){
            lineNum = 0;
            fileInput = Optional.empty();
            scanner = Optional.of(new Scanner(System.in));
        }

        boolean splitAndAssign(){
            if (fileInput.isPresent()){
                List<String> file = fileInput.get();
                // Make sure the line is valid 
                if (lineNum >= file.size())
                    return false;
                
                if(lineNum == 0)
                    globalVars.put("$0", toIDT(file.get(lineNum)));
                else
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
                    globalVars.put("$" + Integer.toString(i+1), toIDT(currentLine[i]));

                // Update NF
                globalVars.replace("NF", toIDT(Integer.toString(currentLine.length)));
                return true;
            }
            else{// Decided to add inline functionality.
                Scanner scnr = scanner.get();
                String nextLine = scnr.nextLine();

                if(lineNum == 0)
                    globalVars.put("$0", toIDT(nextLine));
                else
                    globalVars.replace("$0", toIDT(nextLine));

                String[] currentLine = nextLine.split(globalVars.get("FS").getValue());
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
                    globalVars.put("$" + Integer.toString(i+1), toIDT(currentLine[i]));

                // Update NF
                globalVars.replace("NF", toIDT(Integer.toString(currentLine.length)));
                return true;
            }
        }

        public int getLineNumber(){
            return lineNum;
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

    private String toString(int value){
        return Integer.toString(value);
    }

    // TODO: Delete after testing.
    public LineManager getLineManager(){
        return lm;
    }

    public HashMap<String, InterpreterDataType> TEST_getGlobals(){
        return globalVars;
    }

    public HashMap<String, FunctionDefinitionNode> getFunctions() {
        return functions;
    }

    public ReturnType TEST_processStatements(HashMap<String, InterpreterDataType> localVars, StatementNode statements) throws Exception{
        return processStatement(localVars, statements);
    }

    public InterpreterDataType TEST_getIDT(Node n, HashMap<String, InterpreterDataType> localVar)throws Exception{
        return getIDT(n, localVar);
    }

    public String TEST_runFunctionCall(FunctionCallNode fcn, HashMap<String, InterpreterDataType> localVars) throws Exception{
        return runFunctionCall(fcn, localVars);
    }

    public ReturnType TEST_interpreteStatementList(LinkedList<StatementNode> statements, HashMap<String, InterpreterDataType> localVars) throws Exception{
        return processStatementList(statements, localVars);
    }
}
