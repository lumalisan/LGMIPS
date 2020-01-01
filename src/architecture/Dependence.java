//TFG 2013-2014
package architecture;

import java.util.ArrayList;
import java.util.Collection;
import java.io.*;

/**
 *
 * @author Belen Bermejo Gonzalez
 */
public class Dependence {
    
    private int IdSrc;
    private int IdCmp;
    private String type;

    public Dependence(int Src, int Cmp, String type) {
        IdSrc = Src; //master
        IdCmp = Cmp; //la ins compared
        this.type = type;
    }
    private static FileWriter fstream;
    private static BufferedWriter out;
    private static String msgOut;

    /*Method that detect binary dependence between two instructions*/
    public static void checkDependences(ArrayList<Instruction> listIns) throws IOException {
        createFile();
        Instruction Imaster;
        Instruction Islave;
        Collection<String> readSlave;
        Collection<String> writeSlave;
        for (int i = 0; i < listIns.size(); i++) {
            Imaster = listIns.get(i);
            for (int j = i + 1; j < listIns.size(); j++) {
                Islave = listIns.get(j);
                readSlave = Islave.getRead();
                writeSlave = Islave.getWrite();
                /*compare read and write operands of master instruction with operands (R and W) of slave Ins*/
                Object[] toArray = Imaster.getRead().toArray();
                for (Object toArray1 : toArray) {
                    //WAR dependence
                    if (writeSlave.contains(toArray1)) {
                        msgOut = "WAR de " + Imaster.getIDIns() + " a " + Islave.getIDIns() + "\n";
                        Dependence d = new Dependence(Imaster.getIDIns(), Islave.getIDIns(), "WAR");
                        writeFile(msgOut);
                    }
                }
                //WAW dependence
                for (int x = 0; x < Imaster.getWrite().size(); x++) {
                    if (writeSlave.containsAll(Imaster.getWrite())) {
                        msgOut = "WAW de " + Imaster.getIDIns() + " a " + Islave.getIDIns() + "\n";
                        Dependence d = new Dependence(Imaster.getIDIns(), Islave.getIDIns(), "WAW");
                        writeFile(msgOut);

                    }
                    //RAW dependence
                    if (readSlave.containsAll(Imaster.getWrite())) {
                        msgOut = "RAW de " + Imaster.getIDIns() + " a " + Islave.getIDIns() + "\n";
                        Dependence d = new Dependence(Imaster.getIDIns(), Islave.getIDIns(), "RAW");
                        Islave.addDependence(d);
                        /*The instruction is a master of dependence and it has to signat its instruction slaves*/
                        Imaster.setIsSource(true);
                        Imaster.addSignal(Islave.getIDIns());
                        writeFile(msgOut);
                    }
                }
            }
        }
        closeFile();
    }

    public static void createFile() throws IOException {
        fstream = new FileWriter("dependences.rtf");
        out = new BufferedWriter(fstream);
    }

    public static void closeFile() throws IOException {
        out.close();
    }

    public static void writeFile(String msg) {
        try {
            out.write(msg);
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public int getIdSrc() {
        return IdSrc;
    }

    public int getIdCmp() {
        return IdCmp;
    }

    public String getType() {
        return type;
    }
}
