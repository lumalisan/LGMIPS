/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package architecture;

public class FunctionalUnit {
    
    public static void execute(Instruction inst) {
        String opcode = inst.getOpcode();
        switch (opcode) {
            case "ADD":
                Operations.ADD(inst);
                break;
            case "ADDI":
                Operations.ADDI(inst);
                break;
            case "AND":
                Operations.AND(inst);
                break;
            case "ANDI":
                Operations.ANDI(inst);
                break;
            case "LB":
                Operations.LB(inst);
                break;
            case "LH":
                Operations.LH(inst);
                break;
            case "LW":
                Operations.LW(inst);
                break;
            case "NOR":
                Operations.NOR(inst);
                break;
            case "OR":
                Operations.OR(inst);
                break;
            case "ORI":
                Operations.ORI(inst);
                break;
            case "SB":
                Operations.SB(inst);
                break;
            case "SH":
                Operations.SH(inst);
                break;
            case "SLLV":
                Operations.SLLV(inst);
                break;
            case "SRLV":
                Operations.SRLV(inst);
                break;
            case "SUB":
                Operations.SUB(inst);
                break;
            case "SW":
                Operations.SW(inst);
                break;
            case "XOR":
                Operations.XOR(inst);
                break;
            case "XORI":
                Operations.XORI(inst);
                break;
        }
    }
}
