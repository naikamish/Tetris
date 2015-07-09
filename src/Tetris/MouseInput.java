/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tetris;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 *
 * @author Amish Naik
 */
public class MouseInput implements MouseListener{

    @Override
    //Set mouse event listeners
    public void mouseClicked(MouseEvent e) {
        if(STATE.State==STATE.MENU){
            //Set game speed
            if(MainMenu.beginner.contains(e.getX(),e.getY())||MainMenu.intermediate.contains(e.getX(),e.getY())||MainMenu.expert.contains(e.getX(),e.getY())){
                if(MainMenu.beginner.contains(e.getX(),e.getY()))
                    Game.setSpeed(300);
                else if(MainMenu.intermediate.contains(e.getX(),e.getY()))
                    Game.setSpeed(200);
                else if(MainMenu.expert.contains(e.getX(),e.getY()))
                    Game.setSpeed(100);
                STATE.State=STATE.GAME;
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent me) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent me) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent me) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}