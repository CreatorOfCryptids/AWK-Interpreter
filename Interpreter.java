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

        Function<HashMap<String, InterpreterDataType>, String> temp;
        temp = (hm)->{String retval; for(HashMap.Entry<String, InterpreterDataType> entry: hm.entrySet())
                                                                System.out.print(entry); return};
        functions.put("print", toBIFDN("print", temp, true));
        
        
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

    }
}
