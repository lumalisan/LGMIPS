//TFG 2013-2014
package architecture;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implements the read file and its code analysis*/
public class SourceFile {

    private static File f = null;
    private static FileReader fr = null;
    BufferedReader br = null;
    private static int nLine = 0;
    private static int IdIns = 1;
    ArrayList<Instruction> listOfIntructions = new ArrayList<>();
    ArrayList<String> linesFileList = new ArrayList<>();

    /*Errors at src file*/
    private static int errors = 0;
    private static ArrayList<String> var = new ArrayList<>();

    public SourceFile(String path) throws FileNotFoundException {
        f = new File(path);
        fr = new FileReader(f);
        br = new BufferedReader(fr);
    }

    public void analSyntaxCode() throws IOException {
        readFile();
        searchBranchPoints();
        chechSyntaxCode();
    }

    public int countLines() throws IOException {
        String linia;
        int nLines = 0;
        while ((linia = br.readLine()) != null) {
            nLines++;
        }
        return nLines;
    }

    //Search brach points in source code
    private void searchBranchPoints() {
        int start = 0;
        int nLineLocal = 0;
        for (int i = 0; i < linesFileList.size(); i++) {
            String s = linesFileList.get(i).trim();
            if (s.equals(".text")) {
                start = i + 1;    
                nLineLocal++;
            }
        }
        ArrayList<String> list = new ArrayList<>();
        for (int i = start; i < linesFileList.size(); i++) {
            list.add(linesFileList.get(i));
        }
        for (int i = 0; i < list.size(); i++) {
            int points = list.get(i).indexOf(":");
            if (points != -1) {                
                String label = list.get(i).substring(0, points);
                label = label.replaceAll("\\s+", "");
                Architecture.addBranchPoint(label, nLineLocal);
                //System.out.println("LABEL + "+label +" i "+nLine);
            }
            //Bug que no dejaba ejecutar un programa con dos Labels arreglado
            nLineLocal++;
        }
    }

    // Check correct syntax and create arrayList of instructions
    private void chechSyntaxCode() {
        for (int i = 0; i < linesFileList.size(); i++) {
            String line = linesFileList.get(i);
            String lineAux;
            int posSpace;
            String opcode = "";
            String operands = "";
            if (line.length() != 0) { //if line is not white
                nLine++;
                line = line.trim();
                lineAux = line.replaceAll("\\s", "");
                char[] Array = lineAux.toCharArray();
                if (Array[0] == '/' && Array.length != 0) {  //comment
                } else if (Array[0] == '.' && Array.length != 0) {
                    String dataOrBody = lineAux.substring(1, lineAux.length());
                    switch (dataOrBody) {
                        case "data":
                            break;
                        case "text":
                            //create instructions data structures
                            Instruction.createInsSet();
                            break;
                        default:
                            errors++;
                            System.out.println("Line " + nLine + " syntax error");
                    }
                } else {
                    int dash = lineAux.indexOf("_");
                    int add = lineAux.indexOf(".");
                    int comment = lineAux.indexOf("/");
                    if (dash != -1) { //variables
                        String name = (String) lineAux.subSequence(0, dash);
                        String type = (String) lineAux.subSequence(add + 1, add + 5);
                        String value;
                        if (comment == -1) {
                            value = (String) lineAux.subSequence(add + 5, lineAux.length());
                        } else {
                            value = (String) lineAux.subSequence(add + 5, comment);
                        }                        
                        /*Add in memory
                         age: .word 30  (name, type, value)*/
                        var.add(name);
                        Memory.addVariable(name, value, type);
                    } else { //instruction set                       
                        int points = lineAux.indexOf(":");
                        posSpace = line.indexOf(" ");
                        if (points != -1) {
                            if (posSpace != -1) {
                                opcode = (String) line.subSequence(points + 1, posSpace);
                                if (comment != -1) {
                                    operands = (String) line.subSequence(posSpace + 1, comment + 1);
                                } else {
                                    operands = (String) line.subSequence(posSpace + 1, line.length());
                                }
                            }
                        } else {
                            if (posSpace != -1) {
                                opcode = (String) line.subSequence(0, posSpace);
                                if (comment != -1) {
                                    operands = (String) line.subSequence(posSpace + 1, comment + 1);
                                } else {
                                    operands = (String) line.subSequence(posSpace + 1, line.length());
                                }
                            }
                        }
                        Instruction ins = new Instruction(opcode, operands, IdIns);
                        ins.checkOpcode();
                        ins.checkOperand();
                        listOfIntructions.add(ins);
                        IdIns++;
                    }
                }
            }
        }
    }

    private void readFile() throws IOException {
        String readLine;
        while ((readLine = br.readLine()) != null) {
            linesFileList.add(readLine);
        }
        br.close();
    }

    public ArrayList getListIns() {
        return listOfIntructions;
    }

    public static ArrayList<String> getVar() {
        return var;
    }

    public static int getErrors() {
        return errors;
    }

    public static int getnLine() {
        return nLine;
    }

    public static int getIdIns() {
        return IdIns;
    }

    public void closeFile() throws Exception {
        fr.close();
    }

}
