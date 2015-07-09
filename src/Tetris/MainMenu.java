/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Amish
 */
public class MainMenu{
    public static Rectangle beginner = new Rectangle(70,125,125,30);
    public static Rectangle intermediate = new Rectangle(70,175,125, 30);
    public static Rectangle expert = new Rectangle(70,225,125,30);
    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        Font titleFont = new Font("TimesRoman", Font.BOLD, 50);
        g.setFont(titleFont);
        g.setColor(Color.WHITE);
        g.drawString("TETRIS", 40, 50);
        
        Font textFont = new Font("TimesRoman", Font.BOLD, 20);
        g.setFont(textFont);
        g2d.draw(beginner);
        g.drawString("Beginner", 92, 148);
        g2d.draw(intermediate);
        g.drawString("Intermediate", 75, 198);
        g2d.draw(expert);
        g.drawString("Expert", 103, 248);
    }
}
