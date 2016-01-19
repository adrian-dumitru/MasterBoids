package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Random;

/**
 * Created by Adi on 1/10/2016.
 */
public class Boid extends JComponent implements Runnable{

    public int age = 0;
    public int leaderAge = 0;

    private int centerX;
    private int centerY;
    public static int centerR = 10;
    private int angle;  // in degrees
    private static int neighboursRadius = 40;
    private static int visualRadius = 100;

    private int headX;
    private int headY;

    public Color color;

    public Boid(int centerX, int centerY, int angle){
        this.angle = angle;
        setCenterX(centerX);
        setCenterY(centerY);
        Random random = new Random();
        color = new Color(random.nextFloat(),random.nextFloat(),random.nextFloat());
    }

    @Override
    public void paintComponent(Graphics g){
        Polygon boid = createPolygon();
        g.drawPolygon(boid);
        g.setColor(color);
        g.fillPolygon(boid);
    }

    private Polygon createPolygon(){
        Polygon polygon = new Polygon();

        double angle150 = Math.toRadians((360 + 150 + angle)%360);
        double angle210 = Math.toRadians((360 + 210 + angle)%360);

        int x1 = headX;
        int y1 = headY;

        int x2 = (int) Math.ceil(centerX + centerR * Math.cos(angle150));
        int y2 = (int) Math.ceil(centerY + centerR * Math.sin(angle150));

        int x3 = (int) Math.ceil(centerX + centerR * Math.cos(angle210));
        int y3 = (int) Math.ceil(centerY + centerR * Math.sin(angle210));

        polygon.addPoint(x1,y1);
        polygon.addPoint(x2,y2);
        polygon.addPoint(x3,y3);

        return polygon;
    }

    public void move(int step){
//        avoidBorder(step);
        setCenterX((int) Math.ceil(centerX + step * Math.cos(Math.toRadians(angle))));
        setCenterY((int) Math.ceil(centerY + step * Math.sin(Math.toRadians(angle))));
        validate();
        repaint();
        checkForLeader();
        age++;
        leaderAge++;
        if(age == Integer.MAX_VALUE){
            System.out.println();
        }
    }

    private void checkForLeader(){
        for(Boid boid : MainFrame.boids){
            if(inVisualRadius(boid) && (boid.age > this.age || boid.leaderAge > this.leaderAge)){
                angle = boid.angle;
                leaderAge = boid.leaderAge;
                color = boid.color;
            }
        }
    }

    private boolean inVisualRadius(Boid boid){
        int distance = distanceBetweenBoids(this,boid);
        if(distance <= visualRadius){
            return true;
        }else{
            return false;
        }
    }

    private static int distanceBetweenBoids(Boid boid1, Boid boid2){
        int distance = (int) Math.sqrt( (boid1.centerX - boid2.centerX) * (boid1.centerX - boid2.centerX) +
                (boid1.centerY - boid2.centerY) * (boid1.centerY - boid2.centerY));
        return distance;
    }

    public boolean intersect(Boid boid){
        int distance = distanceBetweenBoids(this,boid);
        if(distance <= 2 * centerR){
            return true;
        }else{
            return false;
        }
    }

    public void setCenterX(int centerX){
        this.centerX = centerX;
        if(centerX >= MainFrame.widthFrame){
            this.centerX = 0;
        }else if(centerX <= 0){
            this.centerX = MainFrame.widthFrame;
        }
        headX = (int) Math.ceil(this.centerX + centerR * Math.cos(Math.toRadians(angle)));
    }

    public void setCenterY(int centerY){
        this.centerY = centerY;
        if(centerY >= MainFrame.heightFrame){
            this.centerY = 0;
        }else if(centerY <= 0){
            this.centerY = MainFrame.heightFrame;
        }
        headY = (int) Math.ceil(this.centerY + centerR * Math.sin(Math.toRadians(angle)));
    }

    public void avoidNeighbours(){
        for(Boid boid : MainFrame.boids){
            if(boid == this){
                continue;
            }else{

            }
        }
    }

    @Override
    public void run() {
        while(true){
            move(2);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
