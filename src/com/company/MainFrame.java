package com.company;


import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Adi on 1/10/2016.
 */
public class MainFrame extends JFrame{

    static int widthScreen ;
    static int heightScreen ;
    static int widthFrame ;
    static int heightFrame ;

    static ArrayList<Boid> boids = new ArrayList<>();

    public MainFrame() throws HeadlessException {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        widthScreen = (int) screenSize.getWidth();
        heightScreen = (int) screenSize.getHeight();
        widthFrame = 9 * widthScreen / 10;
        heightFrame = 9 * heightScreen / 10;

        setSize(widthFrame,heightFrame);
        setLocation((widthScreen-widthFrame)/2,(heightScreen-heightFrame)/2);
        setTitle("Master Boids");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    //        Border border = BorderFactory.createLineBorder(Color.red,4);
    //        getRootPane().setBorder(border);

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_B){
                    addBoid();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

    }

    private void addBoid(){
        Boid boid = null;
        while(boid == null){
            int boidX = ThreadLocalRandom.current().nextInt((2*Boid.centerR),widthFrame+1-(2*Boid.centerR));
            int boidY = ThreadLocalRandom.current().nextInt((2*Boid.centerR),heightFrame+1-(2*Boid.centerR));
            int boidAngle = ThreadLocalRandom.current().nextInt(0,361);
            boid = new Boid(boidX,boidY,boidAngle);
            for(Boid aux : boids){
                if(aux.intersect(boid)){
                    boid = null;
                    break;
                }
            }
        }
        boids.add(boid);
        add(boid);
        Thread newBoid = new Thread(boid);
        newBoid.start();
        validate();
        repaint();
    }
}
