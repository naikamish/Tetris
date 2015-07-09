/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//save scores and names

package Tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;



public class Game extends JPanel{
    private final MainMenu menu;
    
    private boolean[][] rectArray = new boolean[ARRAY_X_SIZE][ARRAY_Y_SIZE];//stores game piece locations
    private Color[][] colorArray = new Color[ARRAY_X_SIZE][ARRAY_Y_SIZE];//stores color locations
    private final Color[] colors={Color.BLUE,Color.GREEN,Color.ORANGE,Color.RED,Color.YELLOW, Color.DARK_GRAY, Color.CYAN, Color.MAGENTA, Color.PINK};
    private int currentColor, nextColor;
    private final boolean[][][] tetramino= 
                {{{false,false,false,false},
                  {true,true,true,true},
                  {false,false,false,false},
                  {false,false,false,false}},
                  
                 {{false,true,false,false},
                  {false,true,false,false},
                  {false,true,true,false},
                  {false,false,false,false}},
                 
                 {{false,true,false,false},
                  {false,true,false,false},
                  {true,true,false,false},
                  {false,false,false,false}},
                
                {{false,false,false,false},
                  {false,true,true,false},
                  {false,true,true,false},
                  {false,false,false,false}},
                
                {{false,false,false,false},
                  {true,true,false,false},
                  {false,true,true,false},
                  {false,false,false,false}},
                
                {{false,false,false,false},
                  {false,true,true,false},
                  {true,true,false,false},
                  {false,false,false,false}},
                
                {{false,false,false,false},
                  {true,true,true,false},
                  {false,true,false,false},
                  {false,false,false,false}}};
    
    private boolean[][] currentPiece = new boolean[4][4];
    
    private static final int ARRAY_X_SIZE = 10, ARRAY_Y_SIZE = 20, SIZE=15;
    private static final int MIN_X_BOUND=0, MAX_X_BOUND=(ARRAY_X_SIZE-1)*SIZE;
    private static final int MIN_Y_BOUND=0, MAX_Y_BOUND=(ARRAY_Y_SIZE-1)*SIZE;
    private static int xPos=(ARRAY_X_SIZE-4)*SIZE/2, yPos=MIN_Y_BOUND;
    private static final Random randomPiece = new Random();
    private static int nextPiece, nextnextPiece;
    private static int score;
    private static boolean paused=false, gameOver=false;
    private static Timer timer;
    
    public Game(){
       setBackground(Color.BLACK);
       menu=new MainMenu();
       
       nextPiece=randomPiece.nextInt(7);
       nextnextPiece=randomPiece.nextInt(7);
       currentColor=randomPiece.nextInt(colors.length);
       nextColor=randomPiece.nextInt(colors.length);
       
       for(int j=0;j<4;j++){
            for(int i=0;i<4;i++){
                currentPiece[j][i]=
                        tetramino[nextPiece][j][i];
            }
       }
       timer = new Timer( 100 , new ActionListener(){
           @Override
           public void actionPerformed(ActionEvent e){
               if(!paused&&STATE.State==STATE.GAME)
               moveDown();
           }
       });
       timer.start();
       this.addMouseListener(new MouseInput());  
       
       getInputMap().put(KeyStroke.getKeyStroke("A"), "Move Left");
       getActionMap().put("Move Left", new KeyAction("A"));

       
       getInputMap().put(KeyStroke.getKeyStroke("D"), "Move Right");
       getActionMap().put("Move Right", new KeyAction("D"));
       
       getInputMap().put(KeyStroke.getKeyStroke("S"), "Move Down");
       getActionMap().put("Move Down", new KeyAction("S"));
       
       getInputMap().put(KeyStroke.getKeyStroke("W"), "Rotate");
       getActionMap().put("Rotate", new KeyAction("W"));
       
       getInputMap().put(KeyStroke.getKeyStroke("P"), "Pause");
       getActionMap().put("Pause", new KeyAction("P"));
       
       getInputMap().put(KeyStroke.getKeyStroke("SPACE"), "Drop");
       getActionMap().put("Drop", new KeyAction("SPACE"));
       
       getInputMap().put(KeyStroke.getKeyStroke("R"), "Restart");
       getActionMap().put("Restart", new KeyAction("R"));
       
       getInputMap().put(KeyStroke.getKeyStroke("M"), "Menu");
       getActionMap().put("Menu", new KeyAction("M"));
    }
    
    public static void setSpeed(int speed){
        timer.setDelay(speed);
        timer.restart();
    }
    
    public void restartGame(){
        rectArray = new boolean[ARRAY_X_SIZE][ARRAY_Y_SIZE];
        colorArray = new Color[ARRAY_X_SIZE][ARRAY_Y_SIZE];
        gameOver=false;
        nextPiece=randomPiece.nextInt(7);
        nextnextPiece=randomPiece.nextInt(7);
        currentColor=randomPiece.nextInt(colors.length);
        nextColor=randomPiece.nextInt(colors.length);
        for(int j=0;j<4;j++){
            for(int i=0;i<4;i++){
                currentPiece[j][i]=
                        tetramino[nextPiece][j][i];
            }
        }
        score=0;
        xPos=(ARRAY_X_SIZE-4)*SIZE/2;
        yPos=MIN_Y_BOUND;
        timer.restart();
        repaint();
    }
    
    public void rotate(){
        
        boolean canRotate=true;
        boolean[][] newArray=new boolean[4][4];
        for(int j=0;j<4;j++){
            for(int i=0;i<4;i++){
                newArray[j][i] = currentPiece[3-i][j];
            }
        }
        
        bookmark1:
        for(int j=0;j<4;j++){
            for(int i=0;i<4;i++){
                if(newArray[j][i]){
                    if(xPos+i*SIZE>MAX_X_BOUND||xPos+i*SIZE<MIN_X_BOUND||yPos+j*SIZE>MAX_Y_BOUND||rectArray[xPos/SIZE+i][yPos/SIZE+j]){
                        canRotate=false;
                        break bookmark1;
                    }
                }
            }
        }
        
        if(canRotate){
        currentPiece=newArray;
        }
    }
    
    public void moveLeft(){
        
        boolean canMove=true;
        
        bookmark1:
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if(currentPiece[j][i]&&xPos+i*SIZE-SIZE<MIN_X_BOUND){
                    canMove=false;
                    break bookmark1;
                }
            }
        }

        if(canMove){
            bookmark2:
            for(int i=0;i<4;i++){
                for(int j=0;j<4;j++){
                    if(currentPiece[j][i]&&rectArray[i-1+(xPos)/SIZE][j+(yPos)/SIZE]){
                        canMove=false;
                        break bookmark2;
                    }
                }
            }
        }
        
        
        if(canMove){
            xPos-=SIZE;
            repaint();
        }
    }
    
    public void moveRight(){
        boolean canMove=true;
        
        bookmark1:
        for(int i=3;i>=0;i--){
            for(int j=0;j<4;j++){
                if(currentPiece[j][i]&&xPos+i*SIZE+SIZE>MAX_X_BOUND){
                    canMove=false;
                    break bookmark1;
                }
            }
        }

        if(canMove){
            bookmark2:
            for(int i=3;i>=0;i--){
                for(int j=0;j<4;j++){
                    if(currentPiece[j][i]&&rectArray[i+1+(xPos)/SIZE][j+(yPos)/SIZE]){
                        canMove=false;
                        break bookmark2;
                    }
                }
            }
        }
        
        
        if(canMove){
            xPos+=SIZE;
            repaint();
        }
    }
    
    public boolean moveDown(){
        boolean canMove=true;
        
        bookmark1:
        for(int j=3;j>=0;j--){
            for(int i=0;i<4;i++){
                if(currentPiece[j][i]&&yPos+j*SIZE+SIZE>MAX_Y_BOUND){
                    canMove=false;
                    break bookmark1;
                }
            }
        }
        
        
        if(canMove){
            bookmark2:
            for(int j=3;j>=0;j--){
                for(int i=0;i<4;i++){
                    if(currentPiece[j][i]&&rectArray[i+(xPos)/SIZE][j+1+(yPos)/SIZE]){
                        canMove=false;
                        break bookmark2;
                    }
                }
            }
        }
        
        if(canMove){
            yPos+=SIZE;
        }
        
        else{
            for(int j=0;j<4;j++){
                for(int i=0;i<4;i++){
                    if(currentPiece[j][i]){
                        colorArray[i+(xPos)/SIZE][j+(yPos)/SIZE]=colors[currentColor];
                        rectArray[i+(xPos)/SIZE][j+(yPos)/SIZE]=true;
                    }
                }
            }
            checkLoss();
            clearLines();
            xPos=(ARRAY_X_SIZE-4)*SIZE/2;
            yPos=0;
            nextPiece=nextnextPiece;
            nextnextPiece=randomPiece.nextInt(7);
            currentColor=nextColor;
            nextColor=randomPiece.nextInt(colors.length);
            for(int j=0;j<4;j++){
                for(int i=0;i<4;i++){
                    currentPiece[j][i]=tetramino[nextPiece][j][i];
                }
            }
        }
        repaint();
        return canMove;
    }
    
    public void checkLoss(){
        if(rectArray[3][0]||rectArray[4][0]||rectArray[5][0]||rectArray[6][0]||
                rectArray[4][1]||rectArray[5][1])
            gameOver=true;
    }
    
    public void clearLines(){
        for(int j=ARRAY_Y_SIZE-1;j>=0;j--){
            boolean clearLineBoolean=true;
            for(int i=0;i<ARRAY_X_SIZE;i++){
                if(rectArray[i][j]==false){                    
                    clearLineBoolean=false;
                    break;
                }
            }
            if(clearLineBoolean==true){
                score++;
                for(int l=j-1;l>=0;l--){
                    for(int k=0;k<ARRAY_X_SIZE;k++){
                        colorArray[k][l+1]=colorArray[k][l];
                        rectArray[k][l+1]=rectArray[k][l];
                    }
                }
                for(int k=0;k<ARRAY_X_SIZE;k++){
                    colorArray[k][0]=null;
                    rectArray[k][0]=false;
                }
                j++;
            }
        }
        repaint();
    }
    
    public void render(Graphics g){       
       g.setColor(Color.WHITE);
       g.drawRect(MIN_X_BOUND, MIN_Y_BOUND, ARRAY_X_SIZE*SIZE, ARRAY_Y_SIZE*SIZE);
       if(paused){
           g.setColor(Color.WHITE);
           g.drawString("*PAUSED*",(ARRAY_X_SIZE-4)*SIZE/2, ARRAY_Y_SIZE*SIZE/2);
           
       }
       
       g.setColor(Color.WHITE);
       g.drawString("Next Piece", MAX_X_BOUND+SIZE+5, SIZE*3);  
       g.drawRect(MAX_X_BOUND+SIZE, SIZE*2, SIZE*7, SIZE*6);
       for(int i=0;i<4;i++){
           for(int j=0;j<4;j++){
               if(tetramino[nextnextPiece][j][i]==true){
                   g.setColor(colors[nextColor]);
                   g.fillRect(MAX_X_BOUND+i*SIZE+SIZE*3,j*SIZE+SIZE*4,SIZE,SIZE);
                   
                   g.setColor(Color.BLACK);
                   g.drawRect(MAX_X_BOUND+i*SIZE+SIZE*3,j*SIZE+SIZE*4,SIZE,SIZE);
               }   
           }
       }
       
       for(int i=0;i<4;i++){
           for(int j=0;j<4;j++){
               if(currentPiece[j][i]==true){
                   g.setColor(colors[currentColor]);
                   g.fillRect(xPos+i*SIZE,yPos+j*SIZE,SIZE,SIZE);
               }
                  
           }
       }

       g.setColor(Color.WHITE);
       g.drawString("SCORE: "+score, MAX_X_BOUND+SIZE+5, SIZE*2-5);
       
       for(int i=0;i<ARRAY_X_SIZE;i++){
           for(int j=0;j<ARRAY_Y_SIZE;j++){
               if(rectArray[i][j]==true){
                   g.setColor(colorArray[i][j]);
                   g.fillRect(i*SIZE,j*SIZE,SIZE,SIZE);
               }
               g.setColor(Color.BLACK);
               g.drawRect(i*SIZE, j*SIZE, SIZE, SIZE);
           }
       }
       g.setColor(Color.WHITE);
       g.drawRect(MIN_X_BOUND, MIN_Y_BOUND, ARRAY_X_SIZE*SIZE, ARRAY_Y_SIZE*SIZE);  
       
       //Add instructions
       g.drawString("Move Left: A", MAX_X_BOUND+SIZE+5, SIZE*10);
       g.drawString("Move Right: D", MAX_X_BOUND+SIZE+5, SIZE*11);
       g.drawString("Move Down: S", MAX_X_BOUND+SIZE+5, SIZE*12);
       g.drawString("Rotate: W", MAX_X_BOUND+SIZE+5, SIZE*13);
       g.drawString("Drop: Space", MAX_X_BOUND+SIZE+5, SIZE*14);
       g.drawString("Pause: P", MAX_X_BOUND+SIZE+5, SIZE*15);
       g.drawString("Restart: R", MAX_X_BOUND+SIZE+5, SIZE*16);
       g.drawString("Menu: M", MAX_X_BOUND+SIZE+5, SIZE*17);
       
       Font titleFont = new Font("TimesRoman", Font.BOLD, 25);
       g.setFont(titleFont);
       if(gameOver){
           g.setColor(Color.WHITE);
           g.drawString("GAME OVER", MIN_X_BOUND, ARRAY_Y_SIZE*SIZE/2);
           timer.stop();
           
       }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(STATE.State==STATE.GAME)
            this.render(g);
        else if(STATE.State==STATE.MENU)
            menu.render(g);
    } 
    
    private class KeyAction extends AbstractAction{
        private String keyPressed;
        public KeyAction(String key){
            keyPressed=key;
        }
        @Override
        public void actionPerformed(ActionEvent e){
            if(STATE.State==STATE.GAME){
                if(!paused&&!gameOver){
                    if(keyPressed.equals("A")){
                        moveLeft();
                    }
                    else if(keyPressed.equals("D")){
                        moveRight();
                    }
                    else if(keyPressed.equals("W")){
                        rotate();
                    }
                    else if(keyPressed.equals("S")){
                        boolean move;
                        move=moveDown();
                    }
                    else if(keyPressed.equals("SPACE")){
                        boolean moveable;
                        moveable=moveDown();
                        while(moveable)
                            moveable=moveDown();
                    }
                }
                if(keyPressed.equals("R")){
                    restartGame();
                }

                else if(keyPressed.equals("M")){
                    restartGame();
                    STATE.State=STATE.MENU;
                }
                else if(keyPressed.equals("P")){
                    if(paused)
                        timer.restart();
                    else
                        timer.stop();
                    paused =!paused;
                    repaint();
                }
            }
        }
    }
} 

