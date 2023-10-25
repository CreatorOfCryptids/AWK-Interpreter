import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Interpreter {

    LineManager lm;
    HashMap<String, InterpreterDataType> globalVars;
    HashMap<String, FunctionDefinitionNode> functions;

    Interpreter(ProgramNode pNode, String filePath){
        Path myPath = Paths.get(filePath);
        try{
            String file = new String(Files.readAllBytes(myPath));
            LineManager(file.split("\n"));
        }
        catch(IOException e){
            LineManager(new LinkedList<String>());
        }
        

    }
    
    private class LineManager{
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

            String[] currentLine = file.get(lineNum).split(globalVars.get("FS").getValue());
            lineNum++;
            // Make sure the line isn't blank.
            if(currentLine.length <=0){
                
                // Update NR and FNR even if the line is blank.
                int FNR = Integer.parseInt(globalVars.get("FNR").getValue());
                globalVars.replace("FNR", toIDT(FNR++));
                int NR = Integer.parseInt(globalVars.get("NR").getValue());
                globalVars.replace("NR", toIDT(NR++));
                return false;
            }

            // Set the $i variables.
            for(int i=0; i<currentLine.length; i++){
                globalVars.replace("$" + i, toIDT(currentLine[1]));
            }

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
}
