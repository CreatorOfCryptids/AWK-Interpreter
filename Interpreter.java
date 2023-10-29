import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class Interpreter {

    LineManager lm;
    HashMap<String, InterpreterDataType> globalVars;
    HashMap<String, FunctionDefinitionNode> functions;

    Interpreter(ProgramNode pNode, String filePath){
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

        //print
        Function<HashMap<String, InterpreterDataType>, String> temp;
        temp = (hm)->{
            for(HashMap.Entry<String, InterpreterDataType> entry: hm.entrySet()){
                System.out.print(entry.getValue());
            }
            return "1";// No idea what we're supposed to do here
        };
        functions.put("print", toBIFDN("print", temp, true));
        //TODO: printf
        temp = (hm)->{
            for (int i=0; i<hm.size(); i++)
                System.out.printf(hm.get(toString(i)).getValue()); 
            return "true";  // No idea what we're supposed to do here
        };
        functions.put("printf", toBIFDN("printf", temp, true));
        // getline
        temp = (hm)->{
            lm.splitAndAssign(); return "true";
        };
        functions.put("getline", toBIFDN("getline", temp, false));
        // next
        temp = (hm)->{
            lm.splitAndAssign(); return "true";
        };
        functions.put("next", toBIFDN("next", temp, false));
        // gsub
        temp = (hm)->{
            if(hm.containsKey("target")){
                return hm.get("target").getValue().replaceAll(hm.get("regexp").getValue(), hm.get("replacement").getValue());
            }
            else{
                return hm.get("$0").getValue().replaceAll(hm.get("regexp").getValue(), hm.get("replacement").getValue());
            }
        };
        functions.put("gsub", toBIFDN("gsub", temp, true));
        // index
        temp = (hm)->{
            return toString(hm.get("in").getValue().indexOf(hm.get("find").getValue()));
        };
        functions.put("index", toBIFDN("index", temp, false));
        // length
        temp = (hm)->{
            if(hm.isEmpty()){
                return toString(globalVars.get("$0").getValue().length());
            }  
            else{
                return toString(hm.get("0").getValue().length());
            }
        };
        functions.put("length", toBIFDN("length", temp, true));
        //TODO: match return the matched portion of the string
        temp = (hm)->{
            if(hm.containsKey("array")){
                int retval = hm.get("string").getValue().indexOf(hm.get("regexp").getValue(), 0);
                globalVars.replace(hm.get("array").getValue(), );
                return toString(retval+1);  // Add one because AWK returns "0" for not found, and "1" if found at first index.
            }
            else{
                int retval = hm.get("string").getValue().indexOf(hm.get("regexp").getValue(), 0);
                return toString(retval+1);  // Add one because AWK returns "0" for not found, and "1" if found at first index.
            }
        };
        functions.put("match", toBIFDN("match", temp, true));
        // split
        temp = (hm)->{

        };
        functions.put("split", toBIFDN("split", temp, false));
        // sprintf
        temp = (hm)->{

        };
        functions.put("sprintf", toBIFDN("sprintf", temp, false));
        // sub
        temp = (hm)->{

        };
        functions.put("sub", toBIFDN("sub", temp, false));
        // substr
        temp = (hm)->{

        };
        functions.put("substr", toBIFDN("substr", temp, true));
        // tolower
        temp = (hm)->{
            return hm.get("0").getValue().toLowerCase();
        };
        functions.put("tolower", toBIFDN("tolower", temp, false));
        // toupper
        temp = (hm)->{
            return hm.get("0").getValue().toUpperCase();
        };
        functions.put("toupper", toBIFDN("toupper", temp, false));
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

    private InterpreterDataType toIDT(float value){
        return new InterpreterDataType(Float.toString(value));
    }

    private BuiltInFunctionDefinitionNode toBIFDN(String name, Function<HashMap<String, InterpreterDataType>, String> foo, boolean variadic){
        return new BuiltInFunctionDefinitionNode(name, foo, variadic);
    }

    private String toString(int value){
        return Integer.toString(value);
    }

    private String toString(float value){
        return Float.toString(value);
    }
}
