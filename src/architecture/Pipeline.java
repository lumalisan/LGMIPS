package architecture;

import files.ConfigFile;
import java.io.IOException;
import java.util.ArrayList;


/*Class to implements the pipelined execution*/
public class Pipeline implements MIPS {

    public enum pipelineStages {
        FETCH, DECODE, EXECUTION, MEMORY, WRITE
    }
    
    // Añadido
    public enum predictionType {
        TAKEN, NONTAKEN, ONEBIT, TWOBIT
    }

    protected static ArrayList<Instruction> listOfInstructions = new ArrayList<>();
    private final ArrayList<Instruction> timeline = new ArrayList<>();
    private final int SCALAB = ConfigFile.getScalability();
    
    // Instruccions que entren a una fase i hi estan uns cicles
    private final ArrayList<Instruction> memF = new ArrayList<>();
    private final ArrayList<Instruction> memD = new ArrayList<>();
    private final ArrayList<Instruction> memE = new ArrayList<>();
    private final ArrayList<Instruction> memM = new ArrayList<>();
    private final ArrayList<Instruction> memW = new ArrayList<>();
    
    // Instruccions que passen de fase a fase
    private final ArrayList<Instruction> bufferFD = new ArrayList<>();
    private final ArrayList<Instruction> bufferDE = new ArrayList<>();
    private final ArrayList<Instruction> bufferEM = new ArrayList<>();
    private final ArrayList<Instruction> bufferMW = new ArrayList<>();

    //Stage latency
    private final int FETCH = ConfigFile.getFetchCycles();
    private final int DECODE = ConfigFile.getDecodeCycles();
    private final int EXECUTE = ConfigFile.getExecuteCycles();
    private final int MEMORY = ConfigFile.getMemoryCycles();
    private final int WRITE = ConfigFile.getWriteCycles();
    
    private int pc;
    private int cicle;
    private boolean fi;
    // Cambiado (Sólo el nombre; antes era pendentBot)
    private boolean doStall; // Stall por dependencias
    private boolean penalitzacioBot;
    private int darreraInstruccio;
    
    // Añadido
    private boolean branchFailed = false;
    
    // Añadido
    private static final predictionType TIPUSPREDICCIO = ConfigFile.getTipusPrediccio(); 
    private static final boolean ADDRESSPREDICTOR = ConfigFile.isAddressPredictor(); 
    private static final boolean FORWARDING = ConfigFile.hasForwarding();
    private static final boolean LOOPUNROLLING = ConfigFile.isLoopUnrolling();
    
    public Instruction source(Instruction i) {
        return listOfInstructions.get(i.getIDIns() - 1);
    }

    public void initPipe() {
        listOfInstructions = Architecture.getListOfIntructions();
        if (listOfInstructions.isEmpty())
            return;
        pc = 0;
        cicle = 0;
        fi = false;
        doStall = false;
        penalitzacioBot = false;
        
        darreraInstruccio = listOfInstructions.get(listOfInstructions.size() - 1).getIDIns();
    }

    public void executePipe() throws IOException {
        initPipe();
        if (listOfInstructions.isEmpty())
            return;
        // La variable 'cicle' representaria el PC (Program Counter)
        while (!fi) {
            cicle++;

            write();
            memory();
            execute();
            decode();
            fetch();
        }
        
        Informacio i = new Informacio();
        i.impressioInfo(cicle, timeline);
    }

    public void fetch() {
        // Per cada instrucció que poguem llançar en un cicle (scalability)
        for (int i = 0; i < SCALAB; i++) {
            if (pc < listOfInstructions.size()) {
                
                Instruction i0 = listOfInstructions.get(pc);
                Instruction inst = InstructionCopier.copy(i0);
                
                if (doStall || penalitzacioBot) {
                    // Añadido
                    inst.setStall(cicle); // Hacer stall
                    break;
                }
                
                // Añadido
                if (branchFailed) {
                    inst.setStall(cicle-1);
                    branchFailed = false;
                }
                
                // Añadido
                i0.setCopy(inst); // Guardamos la copia de la instrucción en la 
                                  // instrucción original (se usa para las 
                                  // dependencias)
                
                timeline.add(inst);
                inst.setSfetch(cicle);
                memF.add(inst);
                pc += 1;

            }
        }
        if (doStall) {
            doStall = false;
        }
        if (penalitzacioBot) {
            penalitzacioBot = false;
        }
        
        for (int i = 0; i < memF.size(); i++) {
            Instruction inst = memF.get(i);
            // Comprovam si de les instruccions posades anteriorment toquen
            // haver acabat per latència de Fetch
            if (cicle - inst.getSfetch() + 1 >= FETCH) {
                inst.setFetch(cicle);
                memF.remove(i);
                i--;
                bufferFD.add(inst);
            }
        }
    }

    public void decode() {
        for (int i = 0; i < SCALAB; i++) {
            if (!bufferFD.isEmpty()) {
                Instruction inst = bufferFD.get(0);
                
                if (inst.isRAW()) {
                    doStall = true;
                    // Añadido
                    inst.setStall(cicle); // Apuntar stall
                    break;
                }

                if (inst.isIsJump()) {
                    BranchManager.branchManager(source(inst));
                    
                    // Añadido y Cambiado
                    // Ha habido un salto y el tipo de prediccion es non-taken
                    if (BranchManager.isDoBranch() && (!predictTaken())) {
                        // Stall
                        penalitzacioBot = true;
                        branchFailed = true;
                        // Cogemos la dirección de salto
                        pc = BranchManager.getPC() - 1;
                        // Invalidam les instruccions posteriors que han entrat
                        for (Instruction f : memF) {
                            f.setFetch(cicle);
                            f.setStall(cicle-1);
                        }
                        memF.clear();
                        bufferFD.clear();
                    } // Si el tipo de predicción es taken
                    else if (predictTaken()) {
                        // Si hay salto y ha acertado con la prediccion de la dirección (Con ADDRESSPREDICTOR activado)
                        if (BranchManager.isDoBranch() && ADDRESSPREDICTOR && BranchManager.getPredictedAddress() == BranchManager.getPC()) {
                            // No hay stall
                            penalitzacioBot = false;
                            // Cogemos la dirección de salto
                            pc = BranchManager.getPC()-1;
                        } // Si hay salto y no ha acertado con la prediccion (necesita calcular la dirección de salto)
                        else if (BranchManager.isDoBranch() && (!ADDRESSPREDICTOR || BranchManager.getPredictedAddress() != BranchManager.getPC())){
                            // Cogemos la dirección de salto
                            pc = BranchManager.getPC()-1;
                            //Stall
                            penalitzacioBot = true;
                            branchFailed = true;
                        } // Si no hay salto
                        else {
                            // Stall
                            penalitzacioBot = true;
                            branchFailed = true;
                        }
                        
                        // Invalidam les instruccions posteriors que han entrat
                        for (Instruction f : memF) {
                            f.setFetch(cicle);
                            f.setStall(cicle-1);
                        }
                        memF.clear();
                        bufferFD.clear();
                    } else {
                        bufferFD.remove(0);
                    }
                    
                } else {
                    bufferFD.remove(0);
                }
                memD.add(inst);
                inst.setSdecode(cicle);
            }
        }
        for (int i = 0; i < memD.size(); i++) {
            Instruction inst = memD.get(i);
            // Comprovam si de les instruccions posades anteriorment toquen
            // haver acabat per latència de Decode
            if (cicle - inst.getSdecode() + 1 >= DECODE) {
                inst.setDecode(cicle);
                memD.remove(i);
                i--;
                bufferDE.add(inst);
            }
        }
    }

    public void execute() {
        // Actualitzar taula de temps
        for (int i = 0; i < SCALAB; i++) {
            if (!bufferDE.isEmpty()) {
                Instruction inst = bufferDE.get(0);
                bufferDE.remove(0);
                FunctionalUnit.execute(source(inst));
                memE.add(inst);
                inst.setSexec(cicle);
                
            }
        }
        for (int i = 0; i < memE.size(); i++) {
            Instruction inst = memE.get(i);
            // Comprovam si de les instruccions posades anteriorment toquen
            // haver acabat per latència de Decode
            if (cicle - inst.getSexec() + 1 >= EXECUTE) {
                inst.setExec(cicle);
                memE.remove(i);
                i--;
                bufferEM.add(inst);
                
                //Añadido
                // Si hay FORWARDING activado y la instrucción es de tipo ALU
                if (FORWARDING && inst.isIsALU()) {
                    // Senyalitzar instruccions que esperaven per dependència
                    ArrayList<Integer> senyals = source(inst).getSignal();
                    for (Integer id : senyals) {
                        Instruction instruccion = (listOfInstructions.get(id - 1));
                        if (instruccion.getCopy() != null) {
                            instruccion.getCopy().setRAW(false);
                        } else {
                            instruccion.setRAW(false);
                        }
                    }
                    
                }
            }
        }
    }

    public void memory() {
        // actualitzar cicles
        for (int i = 0; i < SCALAB; i++) {
            if (!bufferEM.isEmpty()) {
                Instruction inst = bufferEM.get(0);
                bufferEM.remove(0);
                memM.add(inst);
                inst.setSmem(cicle);
            }
        }
        for (int i = 0; i < memM.size(); i++) {
            Instruction inst = memM.get(i);
            // Comprovam si de les instruccions posades anteriorment toquen
            // haver acabat per latència de Decode
            if (cicle - inst.getSmem() + 1 >= MEMORY) {
                inst.setMem(cicle);
                memM.remove(i);
                i--;
                bufferMW.add(inst);
                
                //Añadido
                // Si hay FORWARDING activado y la instrucción es de tipo LW
                if (FORWARDING && inst.isIsLW()) {
                    // Senyalitzar instruccions que esperaven per dependència
                    ArrayList<Integer> senyals = source(inst).getSignal();
                    for (Integer id : senyals) {
                        Instruction instruccion = (listOfInstructions.get(id - 1));
                        if (instruccion.getCopy() != null) {
                            instruccion.getCopy().setRAW(false);
                        } else {
                            instruccion.setRAW(false);
                        }
                    }
                    
                }
            }
        }
    }

    public void write() {
        // actualitzar cicles
        for (int i = 0; i < SCALAB; i++) {
            if (!bufferMW.isEmpty()) {
                Instruction inst = bufferMW.get(0);
                
                bufferMW.remove(0);
                memW.add(inst);
                inst.setSwr(cicle);
            }
        }
        for (int i = 0; i < memW.size(); i++) {
            Instruction inst = memW.get(i);
            // Comprovam si de les instruccions posades anteriorment toquen
            // haver acabat per latència de Decode
            if (cicle - inst.getSwr()+ 1 >= WRITE) {
                inst.setWr(cicle);
                memW.remove(i);
                i--;
                
                // Cambiado
                // Si hay FORWARDING activado y la instrucción es de tipo LW
                if (!FORWARDING || (FORWARDING && !inst.isIsALU() && !inst.isIsLW())) {
                    // Senyalitzar instruccions que esperaven per dependència
                    ArrayList<Integer> senyals = source(inst).getSignal();
                    for (Integer id : senyals) {

                        Instruction instruccion = (listOfInstructions.get(id - 1));
                        if (instruccion.getCopy() != null) {
                            instruccion.getCopy().setRAW(false);
                        } else {
                            instruccion.setRAW(false);
                        }

                    }
                    
                }
                // Comprovam si acabam
                if (inst.getIDIns() == darreraInstruccio) {
                    // Comprovam que si és un bot, no es fa
                    if (!inst.isIsJump() || (inst.isIsJump() && !BranchManager.isDoBranch())) {
                        fi = true;
                    }
                }
            }
        }
    }
    
    // Añadido
    // Método que devuelve true si el tipo de predicción es taken
    private boolean predictTaken() { 
        return TIPUSPREDICCIO == predictionType.TAKEN || 
                (TIPUSPREDICCIO == predictionType.ONEBIT && BranchManager.getOnebitPredictor() == 1) ||
                (TIPUSPREDICCIO == predictionType.TWOBIT && BranchManager.getTwobitPredictor() > 1);
    }

    // Añadido
    public static predictionType getTIPUSPREDICCIO() {
        return TIPUSPREDICCIO;
    }
    
}
