//TFG 2013-2014
package architecture;

import java.util.HashMap;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implement all of the operations*/
public class Operations {

    /*We start the address position*/
    private static int adressStore = (Memory.getFinishPos() - Memory.getStartPos()) / 2;

    //memory-access inst
    public static void LB(Instruction inst) {
        //access to memory
        String[] listOperands = inst.getListOperands();
        String adress = listOperands[1];
        Memory mem = Architecture.getMem();
        Word w = mem.getContent(adress);

        int aux = w.getDecimalContent(0, 1);
        String dst = listOperands[0];
        HashMap<String, Integer> reg = Architecture.getRegisters();
        reg.put(dst, aux);
    }

    public static void LH(Instruction inst) {
        String[] listOperands = inst.getListOperands();
        String adress = listOperands[1];
        Memory mem = Architecture.getMem();
        Word w = mem.getContent(adress);

        int aux = w.getDecimalContent(0, 2);
        String dst = listOperands[0];
        HashMap<String, Integer> reg = Architecture.getRegisters();
        reg.put(dst, aux);
    }

    public static void LW(Instruction inst) {
        String[] listOperands = inst.getListOperands();
        String adress = listOperands[1];
        Memory mem = Architecture.getMem();
        Word w = mem.getContent(adress);

        int aux = w.getDecimalContent(0, 4);
        String dst = listOperands[0];
        HashMap<String, Integer> reg = Architecture.getRegisters();
        reg.put(dst, aux);
    }

    public static void SB(Instruction inst) {
        String[] listOperands = inst.getListOperands();
        String adress = listOperands[1];
        Memory mem = Architecture.getMem();
        HashMap<String, Integer> reg = Architecture.getRegisters();
        String r = listOperands[0];
        int value = reg.get(r);
        String v = String.valueOf(value);

        Word w = new Word(v, "byte", adressStore);
        adressStore++;
        mem.storeValue(adress, w);
      
    }

    public static void SH(Instruction inst) {
        String[] listOperands = inst.getListOperands();
        String adress = listOperands[1];
        Memory mem = Architecture.getMem();
        HashMap<String, Integer> reg = Architecture.getRegisters();
        String r = listOperands[0];
        int value = reg.get(r);
        String v = String.valueOf(value);

        Word w = new Word(v, "half", adressStore);
        adressStore++;
        mem.storeValue(adress, w);
       
    }

    public static void SW(Instruction inst) {
        String[] listOperands = inst.getListOperands();
        String adress = listOperands[1];
        Memory mem = Architecture.getMem();
        HashMap<String, Integer> reg = Architecture.getRegisters();
        String r = listOperands[0];
        int value = reg.get(r);
        String v = String.valueOf(value);

        Word w = new Word(v, "word", adressStore);
        mem.storeValue(adress, w);
        
    }

    //immediate ALUs
    public static void ADDI(Instruction inst) {
        String s[] = inst.getListOperands();

        HashMap<String, Integer> reg = Architecture.getRegisters();
        String key1 = s[1];
        int src1 = reg.get(key1);

        String key2 = s[2];
        //Bug al coger el segundo parámetro desde REG y no directamente 
        //con un parseInt
        //int src2 = reg.get(key2);
        int src2 = Integer.parseInt(key2);

        String keyDst = s[0];
        int result = src2 + src1;
        reg.put(keyDst, result);

    }

    public static void ANDI(Instruction inst) {
        String s[] = inst.getListOperands();

        HashMap<String, Integer> reg = Architecture.getRegisters();
        String key1 = s[1];
        int src1 = reg.get(key1);

        String key2 = s[2];
        //int src2 = reg.get(key2);
        int src2 = Integer.parseInt(key2);

        String keyDst = s[0];
        int result = src2 & src1;
        reg.put(keyDst, result);
    }

    public static void ORI(Instruction inst) {
        String s[] = inst.getListOperands();

        HashMap<String, Integer> reg = Architecture.getRegisters();
        String key1 = s[1];
        int src1 = reg.get(key1);

        String key2 = s[2];
        //int src2 = reg.get(key2);
        int src2 = Integer.parseInt(key2);

        String keyDst = s[0];
        int result = src2 | src1;
        reg.put(keyDst, result);
    }

    public static void XORI(Instruction inst) {
        String s[] = inst.getListOperands();
        HashMap<String, Integer> reg = Architecture.getRegisters();
        String key1 = s[1];
        int src1 = reg.get(key1);

        String key2 = s[2];
        //int src2 = reg.get(key2);
        int src2 = Integer.parseInt(key2);

        String keyDst = s[0];
        int result = src2 ^ src1;
        reg.put(keyDst, result);
    }

    //ALUs reg-reg
    public static void ADD(Instruction inst) {
        String s[] = inst.getListOperands();

        HashMap<String, Integer> reg = Architecture.getRegisters();
        String key1 = s[1];
        int src1 = reg.get(key1);

        String key2 = s[2];
        int src2 = reg.get(key2);

        String keyDst = s[0];
        int result = src2 + src1;

        reg.put(keyDst, result);
    }

    public static void SUB(Instruction inst) {
        String s[] = inst.getListOperands();
        //agafar els valor del hash sent la clau el registre
        HashMap<String, Integer> reg = Architecture.getRegisters();
        String key1 = s[1];
        int src1 = reg.get(key1);
        
        String key2 = s[2];
        int src2 = reg.get(key2);

        String keyDst = s[0];
        int result = src2 - src1;

        reg.put(keyDst, result);
    }

    public static void AND(Instruction inst) {
        String s[] = inst.getListOperands();

        HashMap<String, Integer> reg = Architecture.getRegisters();
        String key1 = s[1];
        int src1 = reg.get(key1);

        String key2 = s[2];
        int src2 = reg.get(key2);

        String keyDst = s[0];
        int result = src2 & src1;
        reg.put(keyDst, result);
    }

    public static void OR(Instruction inst) {
        String s[] = inst.getListOperands();

        HashMap<String, Integer> reg = Architecture.getRegisters();
        String key1 = s[1];
        int src1 = reg.get(key1);

        String key2 = s[2];
        int src2 = reg.get(key2);

        String keyDst = s[0];
        int result = src2 | src1;
        reg.put(keyDst, result);
    }

    public static void XOR(Instruction inst) {
        String s[] = inst.getListOperands();
        //agafar els valor del hash sent la clau el registre
        HashMap<String, Integer> reg = Architecture.getRegisters();
        String key1 = s[1];
        int src1 = reg.get(key1);

        String key2 = s[2];
        int src2 = reg.get(key2);

        String keyDst = s[0];
        int result = src2 ^ src1;
        reg.put(keyDst, result);
    }

    public static void NOR(Instruction inst) {
        String s[] = inst.getListOperands();
        //agafar els valor del hash sent la clau el registre
        HashMap<String, Integer> reg = Architecture.getRegisters();
        String key1 = s[1];
        int src1 = reg.get(key1);

        String key2 = s[2];
        int src2 = reg.get(key2);

        String keyDst = s[0];
        int result = src2 | src1;
        int resultNOR = ~result;
        reg.put(keyDst, resultNOR);
    }

    //Shift
    public static void SLLV(Instruction inst) {
        String s[] = inst.getListOperands();

        HashMap<String, Integer> reg = Architecture.getRegisters();
        String key1 = s[1];
        int src1 = reg.get(key1);

        String key2 = s[2];
        int src2 = reg.get(key2);

        String keyDst = s[0];
        int result = src1 << src2;

        reg.put(keyDst, result);
    }

    public static void SRLV(Instruction inst) {
        String s[] = inst.getListOperands();

        HashMap<String, Integer> reg = Architecture.getRegisters();
        String key1 = s[1];
        int src1 = reg.get(key1);

        String key2 = s[2];
        int src2 = reg.get(key2);

        String keyDst = s[0];
        int result = src1 >> src2;

        reg.put(keyDst, result);
    }
}
