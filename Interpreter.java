//import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

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

    private InterpreterDataType getIDT(Node n, Optional<HashMap<String, InterpreterDataType>> localVar){
        
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
                array[i] = IADTarray.getValue(toString(i));

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
                array[i] = IADTarray.getValue(toString(i));

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
        
        // TODO gsub
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
            /*if(!hm.get("target").getValue().equals("")){    // If the target entry is empty then it wasn't passed to the function.
                matcher = regex.matcher(hm.get("target").getValue());
            }
            else{
                matcher = regex.matcher(hm.get("$0").getValue());
            }

            while (matcher.groupCount()>replacements){// Replace all the instances of the pattern
                matcher.replaceAll(matcher.group(replacements), hm.get("replacement").getValue());
                replacements++;
            }
            if(!hm.get("target").getValue().equals(""))
                hm.replace("target", toIDT(target));
            else
                globalVars.replace("$0", toIDT(target));
            return toString(replacements); */
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
        
        // TODO match
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
            
            /*Pattern regex;  
            Matcher matcher;
            LinkedList<String> splitStrings = new LinkedList<String>();
            LinkedList<String> seperators = new LinkedList<String>();
            String target = hm.get("string").getValue();
            int count = 0;
            
            // Initialze the regex as either the default feild seperator or the passed feild seperator
            if(!hm.get("fieldsep").getValue().equals(""))
                regex = Pattern.compile(hm.get("fieldsep").getValue());
            else
                regex = Pattern.compile(globalVars.get("FS").getValue());

            //Loop thru the target string.
            matcher = regex.matcher(target);
            while(matcher.groupCount()<count){
                splitStrings.add(target.substring(0, matcher.start()));
                seperators.add(matcher.group(count++));
            }

            String[] splitStringsArray = (String[]) splitStrings.toArray();
            globalVars.put(hm.get("array").getValue(), new InterpreterArrayDataType(splitStringsArray));

            

            if(hm.get("seps").getValue().equals("")){
                String[] seperatorsArray = (String[]) seperators.toArray();
                globalVars.replace(hm.get("seps").getValue(), new InterpreterArrayDataType(splitStringsArray));
            }

            return toString(splitStrings.size());*/
            /*if(!hm.get("feildsep").getValue().equals(""))
                splitStrings = hm.get("string").getValue().split(hm.get("feildsep").getValue());
            else
                splitStrings = hm.get("string").getValue().split(globalVars.get("FS").getValue());*/
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
                array[i] = (String) IADTarray.getValue(toString(i));

            return String.format(hm.get("format").getValue(), (String[]) array); 
        };
        args = new String[]{"format", "array"};
        functions.put("sprintf", toBIFDN("sprintf", temp, true, args));
        
        // TODO: sub
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

    /*private static InterpreterDataType toIDT(float value){
        return new InterpreterDataType(Float.toString(value));
    }*/

    private static BuiltInFunctionDefinitionNode toBIFDN(String name, Function<HashMap<String, InterpreterDataType>, String> foo, boolean variadic, String[] args){
        LinkedList<String> argsList = new LinkedList<String>();
        for(String s: args)
            argsList.add(s);
        return new BuiltInFunctionDefinitionNode(name, foo, variadic, argsList);
    }

    /*private static int IDTtoInt(InterpreterDataType idt) throws Exception{
        try{
            int retval = Integer.parseInt(idt.getValue());
            return retval;
        }  
        catch(NumberFormatException e){
            throw new Exception("Expected an Integer, but was actually: " + idt.getValue());
        }
    }*/

    private static String toString(int value){
        return Integer.toString(value);
    }

    /*private static String toString(float value){
        return Float.toString(value);
    }*/
}
