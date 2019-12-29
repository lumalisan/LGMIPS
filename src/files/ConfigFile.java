//TFG 2013-2014 
package files;

import architecture.Pipeline;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Belen Bermejo Gonzalez
 */
public class ConfigFile {

    private final Properties prop = new Properties();
    private InputStream input = null;
    private OutputStream out = null;
    private String path;

    /*Config params. to read*/
    private static String sourcePath;
    private static boolean randomFile;
    private static int nBits;
    private static int startAdress;
    private static int finishAdress;
    private static int nRegister;
    private static int fetchCycles;
    private static int decodeCycles;
    private static int executeCycles;
    private static int memoryCycles;
    private static int writeCycles;
    private static int commitCycles;
    private static String schedulerName;
    private static int nRenameRegister;
    private static int scalability;
    private static int sizeROB;
    private static int nFUGeneric;
    private static int nFUAdd;
    private static int nFUMult;
    private static int latencyGeneric;
    private static int latencyAdd;
    private static int latencyMult;
    private static int retirROB;
    private static boolean ROB;

    // Añadido
    private static Pipeline.predictionType tipusPrediccio;
    private static boolean addressPredictor;
    private static boolean forwarding;
    
    private static boolean loopUnrolling;

    public ConfigFile(String path) {
        try {
            this.path = path;
            input = new FileInputStream(path);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadConfig() throws IOException {
        prop.load(input);
        sourcePath = normalizeKey(prop.getProperty("sourcePath"));
        nBits = Integer.parseInt(normalizeKey(prop.getProperty("nBits")));
        startAdress = Integer.parseInt(normalizeKey(prop.getProperty("startAdress")));
        finishAdress = Integer.parseInt(normalizeKey(prop.getProperty("finishAdress")));
        fetchCycles = Integer.parseInt(normalizeKey(prop.getProperty("fetchCycles")));
        decodeCycles = Integer.parseInt(normalizeKey(prop.getProperty("decodeCycles")));
        executeCycles = Integer.parseInt(normalizeKey(prop.getProperty("executeCycles")));
        memoryCycles = Integer.parseInt(normalizeKey(prop.getProperty("memoryCycles")));
        writeCycles = Integer.parseInt(normalizeKey(prop.getProperty("writeCycles")));
        schedulerName = normalizeKey(prop.getProperty("schedulerName"));
        nRenameRegister = Integer.parseInt(normalizeKey(prop.getProperty("nRenameRegister")));
        scalability = Integer.parseInt(normalizeKey(prop.getProperty("scalable")));
        commitCycles = Integer.parseInt(normalizeKey(prop.getProperty("commitCycles")));
        sizeROB = Integer.parseInt(normalizeKey(prop.getProperty("sizeROB")));
        nFUGeneric = Integer.parseInt(normalizeKey(prop.getProperty("nFUGeneric")));
        nFUAdd = Integer.parseInt(normalizeKey(prop.getProperty("nFUAdd")));
        nFUMult = Integer.parseInt(normalizeKey(prop.getProperty("nFUMult")));
        latencyGeneric = Integer.parseInt(normalizeKey(prop.getProperty("latencyGeneric")));
        latencyAdd = Integer.parseInt(normalizeKey(prop.getProperty("latencyAdd")));
        latencyMult = Integer.parseInt(normalizeKey(prop.getProperty("latencyMult")));
        retirROB = Integer.parseInt(normalizeKey(prop.getProperty("retirementROB")));        
        nRegister = Integer.parseInt(normalizeKey(prop.getProperty("nRegister")));

        // Añadido
        try {
            tipusPrediccio = Pipeline.predictionType.valueOf(normalizeKey(prop.getProperty("tipusPrediccio")));
        } catch (Exception e) {
            System.out.println("**Error leyendo tipo predicción, estableciendo por defecto NONTAKEN**");
            tipusPrediccio = Pipeline.predictionType.NONTAKEN;
        }
        try {
            addressPredictor = normalizeKey(prop.getProperty("addressPredictor")).equals("y");
        } catch (Exception e) {
            System.out.println("**Error leyendo preferencia de predicción direcciones, estableciendo valor por defecto ACTIVA**");
            addressPredictor = true;
        }
        try {
            forwarding = normalizeKey(prop.getProperty("forwarding")).equals("y");
        } catch (Exception e) {
            System.out.println("**Error leyendo preferencia de forwarding, estableciendo valor por defecto ACTIVO**");
            forwarding = true;
        }
        
        try {
            randomFile = normalizeKey(prop.getProperty("randomFile")).equals("y");
        } catch (Exception e) {
            System.out.println("**Error leyendo preferencia de randomFile, estableciendo valor por defecto INACTIVO**");
            randomFile = false;
        }
        
        try {
            ROB = normalizeKey(prop.getProperty("ROB")).equals("y");
        } catch (Exception e) {
            System.out.println("**Error leyendo preferencia de ROB, estableciendo valor por defecto INACTIVO**");
            ROB = false;
        }
        
        try {
            loopUnrolling = normalizeKey(prop.getProperty("loopUnrolling")).equals("y");
        } catch (Exception e) {
            System.out.println("**Error leyendo preferencia de loop unrolling, estableciendo valor por defecto INACTIVO**");
            loopUnrolling = false;
        }
        

    }

    public static void showConfig() {
        System.out.println("============ PREFERENCIAS ============");
        System.out.println("Source path        : " + sourcePath);
        System.out.println("Random file        : " + randomFile);
        System.out.println("N. bits            : " + nBits);
        System.out.println("Starting address   : " + startAdress);
        System.out.println("Finish address     : " + finishAdress);
        System.out.println("N. of registers    : " + nRegister);
        System.out.println("Fetch cycles       : " + fetchCycles);
        System.out.println("Decode cycles      : " + decodeCycles);
        System.out.println("Execute cycles     : " + executeCycles);
        System.out.println("Memory cycles      : " + memoryCycles);
        System.out.println("Writeback cycles   : " + writeCycles);
        System.out.println("Scheduler name     : " + schedulerName);
        System.out.println("N. rename reg.s    : " + nRenameRegister);
        System.out.println("Scalability        : " + scalability);
        System.out.println("Commit cycles      : " + commitCycles);
        System.out.println("ROB size           : " + sizeROB);
        System.out.println("N. generic F.U.    : " + nFUGeneric);
        System.out.println("N. add F.U.        : " + nFUAdd);
        System.out.println("N. mult. F.U.      : " + nFUMult);
        System.out.println("Generic latency    : " + latencyGeneric);
        System.out.println("Add latency        : " + latencyAdd);
        System.out.println("Mult. latency      : " + latencyMult);
        System.out.println("Retirement ROB     : " + retirROB);
        System.out.println("ROB                : " + ROB);

        // Añadido
        System.out.println("Prediction type    : " + tipusPrediccio);

        System.out.println("Address predict.   : " + addressPredictor);
        System.out.println("Forwarding         : " + forwarding);
        System.out.println("Loop Unrolling     : " + loopUnrolling);
        System.out.println("============ FIN PREFERENCIAS ============\n");
    }

    /*Method that ignore comments at config. file*/
    public String normalizeKey(String key) {
        String normalize = key;
        normalize = normalize.trim();
        int i = normalize.indexOf("#");
        if (i > 0) {
            normalize = normalize.substring(0, i);
        }
        return normalize;
    }

    public static boolean getROB() {
        return ROB;
    }

    public static int getRetirROB() {
        return retirROB;
    }

    public static int getScalability() {
        return scalability;
    }

    public static boolean getRandomFile() {
        return randomFile;
    }

    public static String getSourcePath() {
        return sourcePath;
    }

    public static int getnBits() {
        return nBits;
    }

    public static int getStartAdress() {
        return startAdress;
    }

    public static int getFinishAdress() {
        return finishAdress;
    }

    public static int getnRegister() {
        return nRegister;
    }

    public static int getFetchCycles() {
        return fetchCycles;
    }

    public static int getDecodeCycles() {
        return decodeCycles;
    }

    public static int getExecuteCycles() {
        return executeCycles;
    }

    public static int getMemoryCycles() {
        return memoryCycles;
    }

    public static int getWriteCycles() {
        return writeCycles;
    }

    public static String getSchedulerName() {
        return schedulerName;
    }

    public static int getnRenameRegister() {
        return nRenameRegister;
    }

    public static int getCommitCycles() {
        return commitCycles;
    }

    public static int getSizeROB() {
        return sizeROB;
    }

    public static int getnFUGeneric() {
        return nFUGeneric;
    }

    public static int getnFUAdd() {
        return nFUAdd;
    }

    public static boolean isLoopUnrolling() {
        return loopUnrolling;
    }

    public static void setLoopUnrolling(boolean loopUnrolling) {
        ConfigFile.loopUnrolling = loopUnrolling;
    }

    public static int getnFUMult() {
        return nFUMult;
    }

    public static int getLatencyGeneric() {
        return latencyGeneric;
    }

    public static int getLatencyAdd() {
        return latencyAdd;
    }

    public static int getLatencyMult() {
        return latencyMult;
    }

    // Añadido
    public static Pipeline.predictionType getTipusPrediccio() {
        return tipusPrediccio;
    }

    // Añadido
    public static boolean isAddressPredictor() {
        return addressPredictor;
    }

    // Añadido
    public static boolean hasForwarding() {
        return forwarding;
    }

    public void setInput(InputStream input) {
        this.input = input;
    }

    public void setSourcePath(String sourcePath) {
        prop.setProperty("sourcePath", sourcePath+"");
    }

    public void setRandomFile(boolean randomFile) {
        if (randomFile)
            prop.setProperty("randomFile", "y");
        else
            prop.setProperty("randomFile", "n");
    }

    public void setnBits(int nBits) {
        prop.setProperty("nBits", nBits+"");
    }

    public void setStartAdress(int startAdress) {
        prop.setProperty("startAdress", startAdress+"");
    }

    public void setFinishAdress(int finishAdress) {
        prop.setProperty("finishAdress", finishAdress+"");
    }

    public void setnRegister(int nRegister) {
        prop.setProperty("nRegister", nRegister+"");
    }

    public void setFetchCycles(int fetchCycles) {
        prop.setProperty("fetchCycles", fetchCycles+"");
    }

    public void setDecodeCycles(int decodeCycles) {
        prop.setProperty("decodeCycles", decodeCycles+"");
    }

    public void setExecuteCycles(int executeCycles) {
        prop.setProperty("executeCycles", executeCycles+"");
    }

    public void setMemoryCycles(int memoryCycles) {
        prop.setProperty("memoryCycles", memoryCycles+"");
    }

    public void setWriteCycles(int writeCycles) {
        prop.setProperty("writeCycles", writeCycles+"");
    }

    public void setCommitCycles(int commitCycles) {
        prop.setProperty("commitCycles", commitCycles+"");
    }

    public void setSchedulerName(String schedulerName) {
        prop.setProperty("schedulerName", schedulerName+"");
    }

    public void setnRenameRegister(int nRenameRegister) {
        prop.setProperty("nRenameRegister", nRenameRegister+"");
    }

    public void setScalability(int scalability) {
        prop.setProperty("scalable", scalability+"");
    }

    public void setSizeROB(int sizeROB) {
        prop.setProperty("sizeROB", sizeROB+"");
    }

    public void setnFUGeneric(int nFUGeneric) {
        prop.setProperty("nFUGeneric", nFUGeneric+"");
    }

    public void setnFUAdd(int nFUAdd) {
        prop.setProperty("nFUAdd", nFUAdd+"");
    }

    public void setnFUMult(int nFUMult) {
        prop.setProperty("nFUMult", nFUMult+"");
    }

    public void setLatencyGeneric(int latencyGeneric) {
        prop.setProperty("latencyGeneric", latencyGeneric+"");
    }

    public void setLatencyAdd(int latencyAdd) {
        prop.setProperty("latencyAdd", latencyAdd+"");
    }

    public void setLatencyMult(int latencyMult) {
        prop.setProperty("latencyMult", latencyMult+"");
    }

    public void setRetirROB(int retirROB) {
        prop.setProperty("retirementROB", retirROB+"");
    }

    public void setROB(boolean ROB) {
        if (ROB)
            prop.setProperty("ROB", "y");
        else
            prop.setProperty("ROB", "n");
    }

    public void setTipusPrediccio(Pipeline.predictionType tipusPrediccio) {
        prop.setProperty("tipusPrediccio", tipusPrediccio.toString()+"");
    }

    public void setAddressPredictor(boolean addressPredictor) {
        if (addressPredictor)
            prop.setProperty("addressPredictor", "y");
        else
            prop.setProperty("addressPredictor", "n");
    }

    public void setForwarding(boolean forwarding) {
        if (forwarding)
            prop.setProperty("forwarding", "y");
        else
            prop.setProperty("forwarding", "n");
    }

    public void updateConfigFile() {
        
        try {
            if (prop == null) {
                loadConfig();
                System.out.println("DEBUG - updateConfigFile(): prop is null!");
            }
            out = new FileOutputStream(path);
            prop.store(out, path);
        } catch (IOException ex) {
            System.err.println("Error: No se ha podido actualizar el fichero de configuración!");
            ex.printStackTrace(System.err);
        }
    }

    public void debug_testupdate() {
        
        Random rng = new Random();
        int cycles = rng.nextInt(200);
        
        try {
            loadConfig();
            prop.list(System.out);
            System.out.printf("****DEBUG - Setting fetch cycles to %d\n****",cycles);
            prop.setProperty("fetchCycles", cycles+"");
            updateConfigFile();
            prop.list(System.out);

        } catch (IOException ex) {
            Logger.getLogger(ConfigFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
