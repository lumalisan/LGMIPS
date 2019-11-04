package architecture;

public class BranchManager {

    private static int PC = -1;
    private static boolean doBranch = false;
    private static boolean manangmentFinish = false;

    private static int nBranch;
    private static int nBranchTaken;
    
    // Añadido
    // 2 values: 0 predict not-taken, 1 predict taken.
    private static int onebitPredictor;
    // The two bit predictor takes 4 values: 0, 1, 2 and 3. 0 and 1 predict
    // non-taken, 2 and 3 predict taken.
    private static int twobitPredictor = 0;
    // Last address where a branch has jumped
    private static int predictedAddress = -1;
    
    public static void branchManager(Instruction inst) {
        int op0;
        int op1;
        //int target;
        //int branch;
        nBranch++;
        
        // Añadido
        if (Pipeline.getTIPUSPREDICCIO() == Pipeline.predictionType.ONEBIT) {
            modifyOneBitPredictor();
        } else if (Pipeline.getTIPUSPREDICCIO() == Pipeline.predictionType.TWOBIT){
            modifyTwoBitPredictor();
        }
        
        String[] op = inst.getListOperands();
        switch (inst.getOpcode()) {
            case "J":
                doBranch(op, 0);
                break;
            case "BEQ":
                op0 = Architecture.getRegisters().get(op[0]);
                op1 = Architecture.getRegisters().get(op[1]);
                if (op0 == op1) {
                    doBranch(op, 2);
                } else {
                    doBranch = false;
                }
                break;
            case "BNE":
                op0 = Architecture.getRegisters().get(op[0]);
                op1 = Architecture.getRegisters().get(op[1]);
                if (op0 != op1) {
                    doBranch(op, 2);
                } else {
                    doBranch = false;
                }
                break;
            case "BLEZ":
                op0 = Architecture.getRegisters().get(op[0]);
                if (op0 <= 0) {
                    doBranch(op, 1);
                } else {
                    doBranch = false;
                }
                break;
            case "BGTZ":
                op0 = Architecture.getRegisters().get(op[0]);
                //System.out.println("***VALOR = " + op0 + " volem botar a " + op[1]);
                if (op0 > 0) {
                    doBranch(op, 1);
                } else {
                    doBranch = false;
                }
                break;
            case "BLTZ":
                op0 = Architecture.getRegisters().get(op[0]);
                if (op0 < 0) {
                    doBranch(op, 1);
                } else {
                    doBranch = false;
                }
                break;
            case "BGEZ":
                op0 = Architecture.getRegisters().get(op[0]);
                if (op0 >= 0) {
                    doBranch(op, 1);
                } else {
                    doBranch = false;
                }
                break;
        }
        if (doBranch)
            nBranchTaken++;
        manangmentFinish = true;
    }
    
    // This method performs the branch by modifying PC and control variables
    private static void doBranch(String [] op, int operandPosition) {
        
        // Añadido
        predictedAddress = PC;
        
        // Cambiado (Etiquetas)
        try {
            PC = Integer.parseInt(op[operandPosition]); // Si op[] es un número (la linea a la que saltar)
        } catch (NumberFormatException e) {
            PC = Architecture.getBRANCHPOINTS().get(op[operandPosition]); // Si es una etiqueta
            // usa op[] de "key" y devuelve el número (la linea a la que saltar)
        }

        doBranch = true;
    }
    
    // Method in charge of updating the one bit predictor with every branch // Añadido
    private static void modifyOneBitPredictor() {
        if (doBranch) {
            onebitPredictor = 1;
        } else {
            onebitPredictor = 0;
        }
    }

    // Method in charge of updating the two bit predictor with every branch
    private static void modifyTwoBitPredictor() {
        
        // Cambiado (Si no te gusta déjalo como estaba)
        if (doBranch) {
            if (twobitPredictor < 3) {
                twobitPredictor++;
            }
        } else {
            if (twobitPredictor > 0) {
                twobitPredictor--;
            }
        }

    }
    
    public static int getPC() {
        return PC;
    }

    public static boolean isDoBranch() {
        return doBranch;
    }

    public static boolean isManangmentFinish() {
        return manangmentFinish;
    }
    
    public static int getnBranch() {
        return nBranch;
    }

    public static int getnBranchTaken() {
        return nBranchTaken;
    }
    
    public static int getOnebitPredictor() {
        return onebitPredictor;
    }

    public static int getTwobitPredictor() {
        return twobitPredictor;
    }

    public static int getPredictedAddress() {
        return predictedAddress;
    }
    
}
