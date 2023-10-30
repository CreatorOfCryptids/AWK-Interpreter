import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Interpreter {

    LineManager lm;
    HashMap<String, InterpreterDataType> globalVars;
    HashMap<String, FunctionDefinitionNode> functions;

    Interpreter(ProgramNode pNode, String filePath) throws Exception{
        globalVars.put("FS", toIDT(" "));
        globalVars.put("OFMT", toIDT("%.6g"));
        globalVars.put("OFS", toIDT(" "));
        globalVars.put("ORS", toIDT("\n"));
        globalVars.put("FILENAME", toIDT(filePath));
        
        Path myPath = Paths.get(filePath);

        try{
            String file = new String(Files.readAllBytes(myPath));
            LinkedList<String> lines = new LinkedList<String>();
            for(String s : file.split("\n"))
                lines.add(s);
            
            lm = new LineManager(lines);
        }
        catch(IOException e){
            lm = new LineManager(new LinkedList<String>());
        }
        
        LinkedList<FunctionDefinitionNode> functionList = pNode.getFunctionNodes();
        for(FunctionDefinitionNode n : functionList)
            functions.put(n.getName(), n);

        String[] args;  // Using an array of strings to hold the args because it's easier than makeing a LinkedList. It will be changed to a LinkeList in the toBIFDN method.
        //print
        Function<HashMap<String, InterpreterDataType>, String> temp;
        temp = (hm)->{
             String[] array;
            InterpreterArrayDataType IADTarray;
            if(hm.get("array") instanceof InterpreterArrayDataType){
                IADTarray = ((InterpreterArrayDataType)hm.get("array"));
                array = new String[IADTarray.getSize()];
            }
            else
                return "false";
            for(int i=0; i<array.length; i++)
                array[i] = IADTarray.getValue(toString(i));

            System.out.print(array); 
            return "true";
        };
        args = new String[]{"array"};
        functions.put("print", toBIFDN("print", temp, true, args));
        
        //printf
        temp = (hm)->{
            String[] array;
            InterpreterArrayDataType IADTarray;
            if(hm.get("array") instanceof InterpreterArrayDataType){
                IADTarray = ((InterpreterArrayDataType)hm.get("array"));
                array = new String[IADTarray.getSize()];
            }
            else
                return "false";
            for(int i=0; i<array.length; i++)
                array[i] = IADTarray.getValue(toString(i));

            System.out.printf(hm.get("format").getValue(), (Object) array); 
            return "true";  //TODO: No idea what we're supposed to return here
        };
        args = new String[]{"format", "aray"};
        functions.put("printf", toBIFDN("printf", temp, true, args));
        
        // getline
        temp = (hm)->{
            lm.splitAndAssign(); return "true";
        };
        args = new String[]{};
        functions.put("getline", toBIFDN("getline", temp, false, args));
        
        // next
        temp = (hm)->{
            lm.splitAndAssign(); return "true";
        };
        args = new String[]{};
        functions.put("next", toBIFDN("next", temp, false, args));
        
        // gsub
        temp = (hm)->{//(regexp, replacement [, target])
            Pattern regex = Pattern.compile(hm.get("regexp").getValue());
            Matcher matcher;
            int replacements = 0;
            // If the target is specificed, search the target instead of the current line.
            if(hm.containsKey("target"))
                matcher = regex.matcher(hm.get("target").getValue());
            else
                matcher = regex.matcher(hm.get("$0").getValue());
            while (matcher.matches()){// Replace all the instances of the pattern
                hm.get("target").getValue().replace(matcher.group(1), hm.get("replacement").getValue());
                replacements++;
            }    
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
            if(hm.get("string").getValue().equals("")){
                return toString(globalVars.get("$0").getValue().length());
            }  
            else{
                return toString(hm.get("string").getValue().length());
            }
        };
        args = new String[]{"string"};
        functions.put("length", toBIFDN("length", temp, true, args));
        
        // match
        temp = (hm)->{//(string, regexp [, array])
            Pattern regex = Pattern.compile(hm.get("regexp").getValue());
            Matcher matcher = regex.matcher(hm.get("string").getValue());
            if(hm.containsKey("array")){
                globalVars.replace(hm.get("array").getValue(), toIDT(hm.get("String").getValue().substring(matcher.start(), matcher.end())));
                return toString(matcher.start()+1);  // Add one because AWK returns "0" for not found, and "1" if found at first index.
            }
            else{
                return toString(matcher.start()+1);  // Add one because AWK returns "0" for not found, and "1" if found at first index.
            }
        };
        args = new String[]{"string", "regexp", "array"};
        functions.put("match", toBIFDN("match", temp, true, args));
        
        // split
        temp = (hm)->{//(string, array [, fieldsep [, seps ]])
            Pattern regex;
            if(hm.containsKey("fieldsep"))
                regex = Pattern.compile(hm.get("feildsep").getValue());
            else
                regex = Pattern.compile(globalVars.get("FS").getValue());
            Matcher matcher = regex.matcher(hm.get("string").getValue());
        };
        args = new String[]{"string", "array", "fieldsep", "seps"};
        functions.put("split", toBIFDN("split", temp, true, args));
        
        // sprintf
        temp = (hm)->{//(format, expression1, …)
            
        };
        args = new String[]{"format", "expr1", "expr2","expr3","expr4","expr5","expr6","expr7","expr8","expr9","expr10",
                            "expr11", "expr12","expr13","expr14","expr15","expr16","expr17","expr18","expr19","expr20"};
        functions.put("sprintf", toBIFDN("sprintf", temp, true, args));
        
        // sub
        temp = (hm)->{//(regexp, replacement [, target])
            Pattern regex = Pattern.compile(hm.get("regexp").getValue());
            Matcher matcher;
            int replacements = 0;
            // If a target is specified, search the target instead of the current line.
            if(!hm.get("target").getValue().equals("")){
                matcher = regex.matcher(hm.get("target").getValue());
            }  
            else{
                matcher = regex.matcher(hm.get("$0").getValue());
            }
            if (matcher.matches()){//replace only one instance of the pattern
                if(!hm.get("target").getValue().equals(""))
                    hm.get("target").getValue().replace(matcher.group(1), hm.get("replacement").getValue());
                else
                    hm.get("$0").getValue().replace(matcher.group(1), hm.get("replacement").getValue());
                replacements++;
            }    
            return toString(replacements);
        };
        args = new String[]{"regexp", "replacement", "target"};
        functions.put("sub", toBIFDN("sub", temp, true, args));
        
        // substr
        temp = (hm)->{//(string, start [, length ])
            if(!hm.get("length").getValue().equals("")){
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
    
    class LineManager{
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

    private InterpreterDataType toIDT(String value){
        return new InterpreterDataType(value);
    }

    private InterpreterDataType toIDT(int value){
        return new InterpreterDataType(Integer.toString(value));
    }

    private static InterpreterDataType toIDT(float value){
        return new InterpreterDataType(Float.toString(value));
    }

    private static BuiltInFunctionDefinitionNode toBIFDN(String name, Function<HashMap<String, InterpreterDataType>, String> foo, boolean variadic, String[] args){
        LinkedList<String> argsList = new LinkedList<String>();
        for(String s: args)
            argsList.add(s);
        return new BuiltInFunctionDefinitionNode(name, foo, variadic, argsList);
    }

    private static int IDTtoInt(InterpreterDataType idt) throws Exception{
        try{
            int retval = Integer.parseInt(idt.getValue());
            return retval;
        }  
        catch(NumberFormatException e){
            throw new Exception("Expected an Integer, but was actually: " + idt.getValue());
        }
    }

    private static String toString(int value){
        return Integer.toString(value);
    }

    private static String toString(float value){
        return Float.toString(value);
    }
}
