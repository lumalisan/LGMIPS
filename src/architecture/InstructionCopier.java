/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package architecture;

import architecture.Instruction;

public class InstructionCopier {
    public static Instruction copy(Instruction i0) {
        Instruction i = new Instruction(i0.getOpcode(), i0.getOperands(), i0.getIDIns());
        i.setCommit(i0.getCommit());
        i.setCycleDecode(i0.getCycleDecode());
        i.setDecode(i0.getCycleDecode());
        i.setExec(i0.getExec());
        i.setFU(i0.getFU());
        i.setFetch(i0.getFetch());
        i.setFlagDecode(i0.isFlagDecode());
        i.setIsJump(i0.isIsJump());
        i.setIsALU(i0.isIsALU()); // Añadido
        i.setIsLW(i0.isIsLW()); // Añadido
        i.setIsSource(i0.isIsSource());
        i.setListDependences(i0.getListDependences());
        i.setMem(i0.getMem());
        //i.setOperandsList();
        i.setRAW(i0.getRAW()); //i.setRAW(i0.isRAW()); // Cambiado
        //i.setRegisters(i.get);
        i.setScommit(i0.getScommit());
        i.setSdecode(i0.getSdecode());
        i.setSexec(i0.getSexec());
        i.setSfetch(i0.getSfetch());
        i.setSmem(i0.getSmem());
        i.setSpeculate(i0.getSpeculate());
        i.setState(i0.getState());
        i.setStop(i0.isStop());
        i.setSwr(i0.getSwr());
//        i.setStalls(i0.getStalls()); // Añadido
        return i;
    }
}
