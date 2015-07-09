/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tetris;

import javax.swing.JFrame;

/**
 *
 * @author naika
 */
public class Tetris{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrame application = new JFrame();
        Game panel = new Game();
        application.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        application.add(panel);
        application.setSize(275,350);
        application.setVisible(true);
    }
    
}
