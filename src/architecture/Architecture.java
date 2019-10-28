//TFG 2013-2014
package architecture;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import files.ConfigFile;
import files.ConfigRegisters;

/**
 *
 * @author Belen Bermejo Gonzalez
 */
public final class Architecture implements MIPS {

    public static int numberOfBits = ConfigFile.getnBits(); //number of bits of the architecture

    //file that contains the source code
    public static String srcPath;
    public SourceFile f;

    //Architecture structure data
    private static ArrayList<Instruction> listOfIntructions = new ArrayList<>();
    public static HashMap<String, Integer> registers = new HashMap<>();
    private static final HashMap<String, Integer> branchPoints = new HashMap<>();
   // public static final ROB rob = new ROB(ConfigFile.getROB());
    public static Memory mem;


    /*Constructing the architecture*/
    public Architecture(int numberOfBits, String srcPath) throws FileNotFoundException, IOException {
        if (numberOfBits == 32) {
            Architecture.numberOfBits = numberBits.x32;
        } else if (numberOfBits == 64) {
            Architecture.numberOfBits = numberBits.x64;
        }

        if (srcPath != null) {
            Architecture.srcPath = srcPath;
            f = new SourceFile(Architecture.srcPath);
        }
        createRegisters();
        createMemory();
    }

    /*Method that simulate MIPS execution*/
    public void simulateMIPS() {
        try {
            /*Detect syntax mistakes*/
            f.analSyntaxCode();
            listOfIntructions = f.getListIns();
            try {
                f.closeFile();
            } catch (Exception ex) {
                Logger.getLogger(Architecture.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*we will simulate if number of errors are 0*/
            if (Instruction.getError() == 0 && SourceFile.getErrors() == 0 && Word.getErrorWord() == 0) {

                /*It identify what are the read and write operands of each instruction*/
                for (Instruction listOfIntruction : listOfIntructions) {
                    listOfIntruction.decodeInstruction();
                }

                /*Dependence analisy*/
                Dependence.checkDependences(listOfIntructions);

                /*Sheduling the instructions*/
                Scheduler sch = new Scheduler("order");
                listOfIntructions = sch.scheduling(f.getListIns());

                /*Pipelined execution*/
                Pipeline p = new Pipeline();
                p.executePipe();

                // Memory.clearMemory();
            } else {
                int totalErrors = f.getErrors() + Instruction.getError() + Word.getErrorWord();
                System.out.println("The source code has " + totalErrors + " errors.");
            }
            f.closeFile();
        } catch (IOException ex) {
            Logger.getLogger(Architecture.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Architecture.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*Method that check if a brach point was declarated before in the source code*/
    public static boolean checkBrachPoint(String brachPoint) {
        return branchPoints.containsKey(brachPoint);
    }

    /*Method that add the branch points to its structure*/
    public static void addBrachPoint(String label, int idIns) {
        branchPoints.put(label, idIns);
    }

    /*Method that create the main memory*/
    public void createMemory() {
        mem = new Memory(ConfigFile.getStartAdress(), ConfigFile.getFinishAdress());
    }

    /*Method that create general-porpose registers*/
    public void createRegisters() throws IOException {
        ConfigRegisters cr = new ConfigRegisters();
        if (ConfigRegisters.getnRegister() != numberOfRegistersGP) {
            System.out.println("Number of registers are incorrect. Please check config file.");
        } else {
            cr.loadConfig();
            ArrayList<Integer> reg = new ArrayList<>();
            reg = cr.getConfigReg();
            String name;
            int value;
            for (int i = 0; i < numberOfRegistersGP; i++) {
                name = "r" + i;
                value = reg.get(i);
                registers.put(name, value);
            }
        }
    }
    
//    public static ROB getRob() {
//        return rob;
//    }

    public static Memory getMem() {
        return mem;
    }

    public static ArrayList<Instruction> getListOfIntructions() {
        return listOfIntructions;
    }

    public static HashMap<String, Integer> getRegisters() {
        return registers;
    }

    public static HashMap<String, Integer> getBranchPoints() {
        return branchPoints;
    }
}
