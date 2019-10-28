/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import architecture.Architecture;
import java.awt.*;
import javax.swing.*;
import files.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Izar
 */
public class gui {
    
    String path;       
    String configPath = "config.properties";
    String outFile;

    public gui() {

        JFrame window = new JFrame();
        window.setSize(new Dimension(600, 600));
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Container contentP = window.getContentPane();

        SpringLayout layout = new SpringLayout();
        contentP.setLayout(layout);

        int commCycles = files.ConfigFile.getCommitCycles();
        int fetchCycles = files.ConfigFile.getFetchCycles();
        int decodeCycles = files.ConfigFile.getDecodeCycles();
        int exCycles = files.ConfigFile.getExecuteCycles();
        int memCycles = files.ConfigFile.getMemoryCycles();
        int wbCycles = files.ConfigFile.getWriteCycles();

        JLabel sosig = new JLabel("Sosig");
        JTextField sosig_tf = new JTextField();
        sosig_tf.setText(files.ConfigFile.getCommitCycles() + "");
        sosig_tf.setPreferredSize(new Dimension(140, 30));

        contentP.add(sosig);
        contentP.add(sosig_tf);

        JButton test1 = new JButton("Test 1");
        //test1.setSize(new Dimension(200,30));
        test1.setMaximumSize(new Dimension(200, 30));
        contentP.add(test1);
        
        JButton test_update = new JButton("Test Config File Update");
        test1.setMaximumSize(new Dimension(250,30));
        contentP.add(test_update);
        test_update.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                try {
                    ConfigFile debug_conf = new ConfigFile(configPath);
                    debug_conf.debug_testupdate(); // Actualizo
                    debug_conf.loadConfig();       // Vuelvo a cargar la configuración
                    System.out.println("DEBUG - New Fetch cycles value: " + files.ConfigFile.getFetchCycles());
                } catch (IOException ex) {
                    Logger.getLogger(gui.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        
        });

        // Posicionamiento
        layout.putConstraint(SpringLayout.WEST, sosig, 20, SpringLayout.WEST, contentP);
        layout.putConstraint(SpringLayout.NORTH, sosig, 20, SpringLayout.NORTH, contentP);

        layout.putConstraint(SpringLayout.WEST, sosig_tf, 20, SpringLayout.WEST, contentP);
        layout.putConstraint(SpringLayout.NORTH, sosig_tf, 40, SpringLayout.NORTH, contentP);

        layout.putConstraint(SpringLayout.WEST, test1, 80, SpringLayout.WEST, contentP);
        layout.putConstraint(SpringLayout.NORTH, test1, 20, SpringLayout.NORTH, contentP);
        
        layout.putConstraint(SpringLayout.WEST, test_update, 160, SpringLayout.WEST, contentP);
        layout.putConstraint(SpringLayout.NORTH, test_update, 20, SpringLayout.NORTH, contentP);

        test1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ConfigFile conf = new ConfigFile(configPath);
                    conf.loadConfig();
                    conf.showConfig();
                    /*
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
                    */
                } catch (IOException ex) {
                    System.err.println("Error: No se ha podido cargar el archivo de configuración!");
                    ex.printStackTrace(System.err);
                }
            }
        });

        window.setVisible(true);

        /*
        ConfigFile conf = new ConfigFile(configPath);
        conf.loadConfig();
        conf.showConfig();
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
         */
    }

}
