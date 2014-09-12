/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package compiladores.view;

import java.awt.HeadlessException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 *
 * @author Siloe
 */
public class FramePrincipal extends JFrame {

    public FramePrincipal() throws HeadlessException {
        super("Compilador legal de Silo");
        criarTela();
        this.setVisible(true);
        this.setSize(800, 600);
        this.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }
    
    private void criarTela(){
        JButton btnCompilar = new JButton();
        JTextArea taCodigo = new JTextArea();
        
        btnCompilar.setText("Compilar");
        
        this.add(btnCompilar);
        this.add(taCodigo);
    }
    
    
    
}
