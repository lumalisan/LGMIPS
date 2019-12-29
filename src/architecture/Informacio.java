/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package architecture;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class Informacio {
    
    int instMesLlarga;
    int nBranchs = 0;
    int nBranchsTaken = 0;
    
    public Informacio() {
        instMesLlarga = 0;
    }
    
    public double calculsBots() {
        nBranchs = BranchManager.getnBranch();
        nBranchsTaken = BranchManager.getnBranchTaken();
        
        if (nBranchs == 0 || nBranchsTaken == 0)
            return 0;
        
        return nBranchsTaken / (double)nBranchs*100;
        
    }
    
    public double calculaCPI(int cicle, ArrayList<Instruction> timeline) {
        return cicle / (double)timeline.size();
    }
    
    public void impressioInfo(int cicle, ArrayList<Instruction> timeline) throws IOException {
        
        double CPI = calculaCPI(cicle, timeline);
        double botsRealitzats = calculsBots();
        
        
        String resultat[][] = new String[timeline.size() + 1][cicle + 1];
        // Primera línia
        resultat[0][0] = ""; 
        for (int i = 1; i < cicle + 1; i++) {
            resultat[0][i] = Integer.toString(i) + "\t";
        }
        
        for (int i = 0; i < timeline.size(); i++) {
            Instruction inst = timeline.get(i);
            int fila = i + 1;
            
            // Cambiado
//            String instruccio = String.format("%02d: %s %s", inst.getIDIns(), inst.getOpcode(), inst.getOperands()); // formatar dues xifres
            String instruccio = String.format("%02d: %s %s", fila, inst.getOpcode(), inst.getOperands());
            
            if (instruccio.length() > instMesLlarga) {
                instMesLlarga = instruccio.length();
            }
            resultat[fila][0] = instruccio;
            if (inst.getSfetch() != 0) {
                for (int j = inst.getSfetch(); j <= inst.getFetch(); j++) {
                    resultat[fila][j] = "F\t";
                }
            }
            if (inst.getSdecode() != 0) {
                for (int j = inst.getSdecode(); j <= inst.getDecode(); j++) {
                    resultat[fila][j] = "D\t";
                }
            }
            if (inst.getSexec() != 0) {
                for (int j = inst.getSexec(); j <= inst.getExec(); j++) {
                    resultat[fila][j] = "E\t";
                }
            }
            if (inst.getSmem() != 0) {
                for (int j = inst.getSmem(); j <= inst.getMem(); j++) {
                    resultat[fila][j] = "M\t";
                }
            }
            if (inst.getSwr() != 0) {
                for (int j = inst.getSwr(); j <= inst.getWr(); j++) {
                    resultat[fila][j] = "W\t";
                }
            }
            if (!inst.getStalls().isEmpty()) {
                for (Integer j : inst.getStalls()) {
                    resultat[fila][j] = "S\t";
                }
            }
        }
        
        resultat[0][0] = String.format(String.format("%%0%dd", instMesLlarga), 0).replace("0"," ") + "\t";
        
        // repassam primera columna -> afegir espais al final
        for (int fila = 1; fila < resultat.length; fila++) {
            if (resultat[fila][0].length() < instMesLlarga) {
                resultat[fila][0] +=
                        String.format(String.format("%%0%dd", instMesLlarga -
                                resultat[fila][0].length()), 0).replace("0"," ") + "\t";
            } else {
                resultat[fila][0] += "\t";
            }
        }
        
        String filename = "out_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMuu_HHmmss")) + ".txt";
        System.out.println("DEBUG - Filename: " + filename);
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(new File(filename)))) {
            bw.append("================== RESULTATS ==================");
            bw.append("\r\n");
            bw.append("Branchs taken: "+nBranchsTaken+"\r\n");
            bw.append("Branchs totals: "+nBranchs+"\r\n");
            bw.append("Percentatge de bots realitzats: "+botsRealitzats+"%\r\n"); // Añadido "%"
            bw.append("Cicles: "+cicle+"\r\n");
            bw.append("Número d'instruccions: "+timeline.size()+"\r\n");
            bw.append("CPI: "+CPI+"\r\n");
            bw.append("\r\n");
            bw.append("\r\n");
            
            bw.append("================ DIAGRAMA TEMPORAL ================");
            bw.append("\r\n");
            bw.append("\r\n");
            
            System.out.print("================== RESULTATS ==================");
            System.out.print("\r\n");
            System.out.print("Branchs taken: "+nBranchsTaken+"\r\n");
            System.out.print("Branchs totals: "+nBranchs+"\r\n");
            System.out.print("Percentatge de bots realitzats: "+botsRealitzats+"%\r\n");
            System.out.print("Cicles: "+cicle+"\r\n");
            System.out.print("Número d'instruccions: "+timeline.size()+"\r\n");
            System.out.print("CPI: "+CPI+"\r\n");
            System.out.print("\r\n");
            System.out.print("\r\n");
            
            System.out.print("================ DIAGRAMA TEMPORAL ================");
            System.out.print("\r\n");
            System.out.print("\r\n");
            
            for (String[] resultat1 : resultat) {
                for (String v : resultat1) {
                    bw.append(v == null ? "\t" : v);
                    System.out.print(v == null ? "\t" : v);
                }
                bw.append("\r\n");
                System.out.print("\r\n");
            }
        }
    }
}
