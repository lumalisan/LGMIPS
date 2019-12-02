//TFG 2013-2014

package main;

import files.ConfigFile;
import architecture.Architecture;
import gui.*;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author Belen Bermejo Gonzalez
 */

/*Class that implements the main program*/
public class LGMIPSV2 {

    static String path;       
    static String configPath = "config.properties";
    static String outFile;
    
    public void init() throws IOException{
        ConfigFile conf = new ConfigFile(configPath);
        conf.loadConfig();
        ConfigFile.showConfig();
        String option = ConfigFile.getRandomFile();
        switch (option) {
            case "y":               
                break;
            case "n":
                path = ConfigFile.getSourcePath();
                //  System.out.println("PATH: " + path);
                break;
            default:
                System.out.println("OPTION ERROR");
        }
        Architecture a = new Architecture(ConfigFile.getnBits(), path);
        a.simulateMIPS();
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        NewJFrame.main(null);
    }
}
