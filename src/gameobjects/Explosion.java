package gameobjects;

import gameinterface.Object;
import util.Resource;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Explosion implements Object {
    public float posX;
    public float posY;

    public BufferedImage image1;
    public BufferedImage image2;
    public BufferedImage image3;

    public int state;

    public static int timeout = 100;
    public long checkPointTime;

    public Explosion(float posX, float posY) {
        image1 = Resource.getResourceImage("data/explosion_1.png");
        image2 = Resource.getResourceImage("data/explosion_2.png");
        image3 = Resource.getResourceImage("data/explosion_3.png");

        this.posX = posX;
        this.posY = posY;
        state = 1;
        checkPointTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() - checkPointTime >= timeout) {
            if (state == 1) {
                state = 2;
                checkPointTime = System.currentTimeMillis();
            } else if (state == 2) {
                state = 3;
                checkPointTime = System.currentTimeMillis();
            } else if (state == 3) {
                state = 0;
            }
        }
    }

    @Override
    public void draw(Graphics g) {
        if (state == 1) {
            g.drawImage(image1, (int) posX, (int) posY, null);
        } else if (state == 2) {
            g.drawImage(image2, (int) posX, (int) posY, null);
        } else if (state == 3) {
            g.drawImage(image3, (int) posX, (int) posY, null);
        }
    }

    @Override
    public Rectangle getBound() {
        return null;
    }

    @Override
    public boolean isOutOfScreen() {
        return false;
    }
}
