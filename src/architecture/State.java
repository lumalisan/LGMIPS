//TFG 2013-2014


package architecture;

import java.util.HashMap;
import files.ConfigFile;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implements an execution state*/
public class State {

    private int ID; 
    private static HashMap<String, Integer> registers;
    private Memory mem;

    public State(int IDSpecIn, Memory m, HashMap<String, Integer> r) {
        registers = new HashMap<>();
        mem = new Memory(ConfigFile.getStartAdress(), ConfigFile.getFinishAdress());

        this.ID = IDSpecIn;
        mem = m;
        registers = r;
    }

    public int getIDSpecIns() {
        return ID;
    }

    public  HashMap<String, Integer> getRegisters() {
        return registers;
    }

    public Memory getMem() {
        return mem;
    }
}
