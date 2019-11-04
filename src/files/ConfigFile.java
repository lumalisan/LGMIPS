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
    private static String randomFile;
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
    private static String ROB;

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
        randomFile = (normalizeKey(prop.getProperty("randomFile(y/n)")));
        nBits = Integer.parseInt(normalizeKey(prop.getProperty("nBits")));
        startAdress = Integer.parseInt(normalizeKey(prop.getProperty("startAdress")));
        finishAdress = Integer.parseInt(normalizeKey(prop.getProperty("finishAdress")));
        nRegister = Integer.parseInt(normalizeKey(prop.getProperty("nRegister")));
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
        ROB = normalizeKey(prop.getProperty("ROB(y/n)"));

        // Añadido
        try {
            tipusPrediccio = Pipeline.predictionType.valueOf(normalizeKey(prop.getProperty("tipusPrediccio")));
        } catch (Exception e) {
            System.out.println("**Error leyendo tipo predicción, estableciendo por defecto NONTAKEN**");
            tipusPrediccio = Pipeline.predictionType.NONTAKEN;
        }
        try {
            addressPredictor = normalizeKey(prop.getProperty("addressPredictor(y/n)")).equals("y");
        } catch (Exception e) {
            System.out.println("**Error leyendo preferencia de predicción direcciones, estableciendo valor por defecto ACTIVA**");
            addressPredictor = true;
        }
        try {
            forwarding = normalizeKey(prop.getProperty("forwarding(y/n)")).equals("y");
        } catch (Exception e) {
            System.out.println("**Error leyendo preferencia de forwarding, estableciendo valor por defecto ACTIVO**");
            forwarding = true;
        }
        
        try {
            loopUnrolling = normalizeKey(prop.getProperty("loopUnrolling(y/n)")).equals("y");
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
        boolean rob = false;
        if (ROB.equals("n")) {
            rob = false;
        } else if (ROB.equals("y")) {
            rob = true;
        }
        return rob;
    }

    public static int getRetirROB() {
        return retirROB;
    }

    public static int getScalability() {
        return scalability;
    }

    public static String getRandomFile() {
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

    public static void setSourcePath(String sourcePath) {
        ConfigFile.sourcePath = sourcePath;
    }

    public static void setRandomFile(String randomFile) {
        ConfigFile.randomFile = randomFile;
    }

    public static void setnBits(int nBits) {
        ConfigFile.nBits = nBits;
    }

    public static void setStartAdress(int startAdress) {
        ConfigFile.startAdress = startAdress;
    }

    public static void setFinishAdress(int finishAdress) {
        ConfigFile.finishAdress = finishAdress;
    }

    public static void setnRegister(int nRegister) {
        ConfigFile.nRegister = nRegister;
    }

    public static void setFetchCycles(int fetchCycles) {
        ConfigFile.fetchCycles = fetchCycles;
    }

    public static void setDecodeCycles(int decodeCycles) {
        ConfigFile.decodeCycles = decodeCycles;
    }

    public static void setExecuteCycles(int executeCycles) {
        ConfigFile.executeCycles = executeCycles;
    }

    public static void setMemoryCycles(int memoryCycles) {
        ConfigFile.memoryCycles = memoryCycles;
    }

    public static void setWriteCycles(int writeCycles) {
        ConfigFile.writeCycles = writeCycles;
    }

    public static void setCommitCycles(int commitCycles) {
        ConfigFile.commitCycles = commitCycles;
    }

    public static void setSchedulerName(String schedulerName) {
        ConfigFile.schedulerName = schedulerName;
    }

    public static void setnRenameRegister(int nRenameRegister) {
        ConfigFile.nRenameRegister = nRenameRegister;
    }

    public static void setScalability(int scalability) {
        ConfigFile.scalability = scalability;
    }

    public static void setSizeROB(int sizeROB) {
        ConfigFile.sizeROB = sizeROB;
    }

    public static void setnFUGeneric(int nFUGeneric) {
        ConfigFile.nFUGeneric = nFUGeneric;
    }

    public static void setnFUAdd(int nFUAdd) {
        ConfigFile.nFUAdd = nFUAdd;
    }

    public static void setnFUMult(int nFUMult) {
        ConfigFile.nFUMult = nFUMult;
    }

    public static void setLatencyGeneric(int latencyGeneric) {
        ConfigFile.latencyGeneric = latencyGeneric;
    }

    public static void setLatencyAdd(int latencyAdd) {
        ConfigFile.latencyAdd = latencyAdd;
    }

    public static void setLatencyMult(int latencyMult) {
        ConfigFile.latencyMult = latencyMult;
    }

    public static void setRetirROB(int retirROB) {
        ConfigFile.retirROB = retirROB;
    }

    public static void setROB(String ROB) {
        ConfigFile.ROB = ROB;
    }

    public static void setTipusPrediccio(Pipeline.predictionType tipusPrediccio) {
        ConfigFile.tipusPrediccio = tipusPrediccio;
    }

    public static void setAddressPredictor(boolean addressPredictor) {
        ConfigFile.addressPredictor = addressPredictor;
    }

    public static void setForwarding(boolean forwarding) {
        ConfigFile.forwarding = forwarding;
    }

    public void updateConfigFile() {
        
        try {
            if (prop == null)
                loadConfig();
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
