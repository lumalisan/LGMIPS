//TFG 2013-2014
package architecture;

import java.util.HashMap;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implements memory operations*/
public class Memory {

    private static int startPos;
    private static int finishPos;

//Define memory
    private static final HashMap<String, Word> memory = new HashMap<>();

    public Memory(int startPos, int finishPos) {
        Memory.startPos = startPos;
        Memory.finishPos = finishPos;        
    }

    /*Method that adds a variable in a memory adress*/
    public static void addVariable(String name, String value, String add) { //little endian
        if (Memory.startPos > Memory.finishPos) {
            System.out.println("Segmentation fault");
        }
        Word w = new Word(value, add, Memory.startPos);
        memory.put(name, w);
        w.storeValue();
        Memory.startPos++;
    }

    /*Function that return true if an adress memory is correct*/
    public static boolean correctPos(String pos) {
        boolean posOK = false;
        char[] Array = pos.toCharArray();
        if (Array[0] == '#') {
            String posEdit = (String) pos.subSequence(1, pos.length());
            int finalPos = Integer.parseInt(posEdit);
            if (finalPos >= startPos && finalPos <= finishPos) {
                posOK = true;
            } else {
                System.out.println("Instruction " + SourceFile.getIdIns() + " memory adress out of range");
            }
        } else {                    
            if (memory.containsKey(pos)) {
                posOK = true;
            } else {
                System.out.println("Instruction " + SourceFile.getIdIns() + " variable non define");
            }
        }
        return posOK;
    }

    public Word getContent(String adress) {
        return memory.get(adress);
    }

    public static HashMap<String, Word> getMemory() {
        return memory;
    }

    public static void clearMemory() {
        memory.clear();
    }

    public void storeValue(String key, Word w) {
        memory.put(key, w);
    }

    public static int getStartPos() {
        return startPos;
    }

    public static int getFinishPos() {
        return finishPos;
    }
}
