//TFG 2013-2014

package architecture;

import files.ConfigFile;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Interface that implements MIPS features*/
public interface MIPS {

    public class numberBits {

        public static final int X32 = 32;
        public static final int X64 = 64;
    }

    public enum typeFU {

        GENERIC, ADD, MULT
    }

    public enum instState {

        THROW, EXECUTING, FINISH
    }
    public static int NUMBEROFREGISTERSGP = ConfigFile.getnRegister(); 
   
}
