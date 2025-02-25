/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter.Highlight;

/**
 *
 * @author Izar
 */
public class Results extends javax.swing.JFrame {

    /**
     * Creates new form Results
     */
    public Results() {
        initComponents();
        this.setLocationRelativeTo(null);
        
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                    System.exit(0);
            }
        });
        
    }
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(320, 240));
        setName("Results"); // NOI18N
        setPreferredSize(new java.awt.Dimension(700, 550));
        setSize(new java.awt.Dimension(700, 550));

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setAlignmentX(0.0F);
        jTextArea1.setAlignmentY(0.0F);
        jTextArea1.setBounds(getBounds());
        String[] res = architecture.Informacio.getRes();
        jTextArea1.setEditable(false);
        jTextArea1.setFont(new Font("monospaced", Font.PLAIN, 12));
        jTextArea1.setTabSize(4);

        int cols = 0;
        String totaltext = "";

        for (int i=0; i<res.length; i++) {
            totaltext += res[i];
            int len = res[i].length();
            if (len > cols)
            cols = len;
        }

        for (String s : res) {
            jTextArea1.append(s);
            jTextArea1.append("\n");
        }

        totaltext = jTextArea1.getText();

        // Cuento las 'S'
        // Es ineficiente pero no puedo usar lambdas
        int count = 0;
        for (int i=0; i<totaltext.length()-1; i++) {
            if (totaltext.charAt(i) == 'S' && totaltext.charAt(i+1) == '\t') {
                count++;
            }
        }
        //System.out.println("DEBUG - count: " + count);

        // Preparo el array de indices
        int[] indexes = new int[count];

        // Calculo los indices de las 'S'
        int idx = 0;
        for (int i=0; i<totaltext.length()-1; i++) {
            if (totaltext.charAt(i) == 'S' && totaltext.charAt(i+1) == '\t') {
                indexes[idx] = i;
                idx++;
            }
        }

        /*
        System.out.print("\nDEBUG - indexes: [");
        for (int i=0; i<indexes.length; i++) {
            if (i == indexes.length-1) {
                System.out.print(indexes[i] + "]");
            }
            else {
                System.out.print(indexes[i] + " ");
            }
        }
        System.out.print("\n");
        */
        try {
            for (int i=0; i<jTextArea1.getLineCount(); i++) {
                if (i % 4 == 0) {
                    jTextArea1.getHighlighter().addHighlight(jTextArea1.getLineStartOffset(i), jTextArea1.getLineEndOffset(i),
                        new DefaultHighlighter.DefaultHighlightPainter(new Color(0.96f, 0.96f, 0.96f)));
                } else if (i % 4 == 2) {
                    jTextArea1.getHighlighter().addHighlight(jTextArea1.getLineStartOffset(i), jTextArea1.getLineEndOffset(i),
                        new DefaultHighlighter.DefaultHighlightPainter(new Color(0.9f, 0.9f, 0.9f)));
                }
            }

            Highlight[] highlights = jTextArea1.getHighlighter().getHighlights();

            for (int i=0; i<count; i++) {
                //System.out.println("DEBUG - Highlighting stall at index " + indexes[i]);
                int index = getHighlightIndex(highlights, indexes[i]);
                if (index == -1) {
                    jTextArea1.getHighlighter().addHighlight(indexes[i], indexes[i]+1, new DefaultHighlighter.DefaultHighlightPainter(Color.RED));
                } else {
                    Thread.sleep(10);
                    int startOff = highlights[index].getStartOffset();
                    int endOff = highlights[index].getEndOffset();
                    //System.out.println("DEBUG - Offsets: " + startOff + ", " + endOff + " | " + indexes[i]);
                    jTextArea1.getHighlighter().removeHighlight(highlights[index]);
                    jTextArea1.getHighlighter().addHighlight(startOff, indexes[i], new DefaultHighlighter.DefaultHighlightPainter(new Color(0.9f, 0.9f, 0.9f)));
                    jTextArea1.getHighlighter().addHighlight(indexes[i], indexes[i]+1, new DefaultHighlighter.DefaultHighlightPainter(Color.RED));
                    jTextArea1.getHighlighter().addHighlight(indexes[i], endOff-1, new DefaultHighlighter.DefaultHighlightPainter(new Color(0.9f, 0.9f, 0.9f)));
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        jTextArea1.setCaretPosition(jTextArea1.getDocument().getDefaultRootElement().getElement(3).getStartOffset());
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 665, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private int getHighlightIndex(Highlight[] highl, int offset) {
        
        for (int i=0; i<highl.length; i++) {
            //System.out.println("DEBUG - Searching for offset " + offset + "in highlight bounds (" + highl[i].getStartOffset() + ", " + highl[i].getEndOffset() + ")");
            if (highl[i].getStartOffset() <= offset && highl[i].getEndOffset() >= offset) {
                //System.out.println("DEBUG - Highlight index found: " + i);
                return i;
            }
        }
        //System.out.println("DEBUG - Error: no highlight index found!");
        return -1;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("System".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(mainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(mainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(mainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(mainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Results().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}
