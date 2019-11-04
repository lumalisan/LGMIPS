//TFG 2013-2014
package architecture;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Belén Bermejo Gonzalez
 */

/*Class that implemets an interface in order to develop new schedulers*/
public class Scheduler {

    private static String type; //kind of sheduling

    private final File fileSch;

    public Scheduler(String type) {
        Scheduler.type = type;
        fileSch = new File("shedResult.txt");
    }

    public ArrayList<Instruction> scheduling(ArrayList<Instruction> toShed) throws IOException {
        switch (type) {
            case "order":
                toShed = Architecture.getListOfIntructions();
                printSch(toShed);
                return inOrder(toShed);
            default:
                return null;
        }
    }

    private ArrayList<Instruction> inOrder(ArrayList<Instruction> toShed) {
        return toShed;
    }

    private void printSch(ArrayList<Instruction> schList) throws IOException {
        String out;
        FileWriter fw = new FileWriter(fileSch.getAbsoluteFile());
        try (BufferedWriter bw = new BufferedWriter(fw)) {
            out="--------SCHEDULING RESULT--------------\r\n";
            bw.write(out);
            for (Instruction schList1 : schList) {
                out=schList1.toString()+"\r\n";
                bw.write(out);
            }
        }
    }

    public static String getType() {
        return type;
    }
}
