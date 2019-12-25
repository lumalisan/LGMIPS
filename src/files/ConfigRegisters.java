//TFG 2013-2014
package files;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
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
    private OutputStream output = null;
    private ArrayList<Integer> configReg = new ArrayList<>();
    private static int nRegister;
    private static String path = "reg.properties";

    public ConfigRegisters() throws IOException {
        try {
            input = new FileInputStream(path);
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

    public int getRegistersNumber() {
        return prop.size();
    }

    public int getRegister(int i) {
        this.loadConfig();
        if (configReg.size() >= i) {
            return configReg.get(i);
        } else {
            return -1;
        }
    }

    public ArrayList<Integer> getConfigReg() {
        return configReg;
    }

    public void setRegister(int nReg, int regValue) throws FileNotFoundException, IOException {
        if (nReg > nRegister) {
            for (int i = nRegister; i < nReg; i++) {
                prop.put("r" + i, 0 + "");  // Añado registros vacíos hasta llegar al registro que queremos añadir
            }
            prop.put("r" + nReg, regValue + "");

        } else {
            prop.setProperty("r" + nReg, regValue + "");  // Si ya existe, actualizo el valor
        }

        System.out.println("DEBUG - prop list");
        prop.list(System.out);

        // Escribimos los cambios
        output = new FileOutputStream(path);
        prop.store(output, path);

        // Recargamos el arrayList de registros
        configReg.clear();
        prop.load(input);
        checkRegNumber();
        loadConfig();
        checkRegNumber();
    }

    public static void updateRegisters(Vector table) {

        FileOutputStream out = null;
        try {
            Properties prop_new = new Properties();
            for (int i = 0; i < table.size(); i++) {
                Vector reg = (Vector) table.elementAt(i);
                prop_new.put(reg.get(0).toString(), reg.get(1).toString());
            }
            
            out = new FileOutputStream(path);
            prop_new.store(out, path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigRegisters.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConfigRegisters.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                Logger.getLogger(ConfigRegisters.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
