/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servicios;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Adolfo
 */
public class Util {
        
    public static void mensaje_info(String titulo,String mensaje){
        JOptionPane.showMessageDialog(new JFrame(), mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void mensaje_error(String titulo,String mensaje){
        JOptionPane.showMessageDialog(new JFrame(), mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }
    
    public static void mensaje_aviso(String titulo,String mensaje){
        JOptionPane.showMessageDialog(new JFrame(), mensaje, titulo, JOptionPane.WARNING_MESSAGE);
    }
    
}
