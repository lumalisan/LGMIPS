//TFG 2013-2014

package files;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*This class reads the inicial value of general-proporse registers*/
public final class ConfigRegisters {

    private final Properties prop = new Properties();
    private InputStream input = null;
    private ArrayList<Integer> configReg = new ArrayList<>();
    private static int nRegister;

    public ConfigRegisters() throws IOException {
        try {
            input = new FileInputStream("reg.properties");
            prop.load(input);
            checkRegNumber();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadConfig() {
        for (int i = 0; i < nRegister; i++) {
            configReg.add(Integer.parseInt(prop.getProperty("r" + i)));
        }       
    }

    public void checkRegNumber() throws IOException {
        nRegister = prop.size();
    }

    public static int getnRegister() {
        return nRegister;
    }

    public ArrayList<Integer> getConfigReg() {
        return configReg;
    }      

}
