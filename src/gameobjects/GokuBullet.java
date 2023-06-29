package gameobjects;

import gameinterface.Object;
import util.Resource;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GokuBullet {
    int x, y, speed;
    BufferedImage image;

    boolean visible;

    private Rectangle rectBound;

    public GokuBullet(int startX, int startY){
        x = startX;
        y = startY;
        speed = 10;
        image = Resource.getResourceImage("data/bullet_Goku.png");
        visible = true;
    }

    public void move(){
        x += speed;
        if ( x > 700 ){
            visible = false;
        }
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public boolean getVisible() {
        return visible;
    }

    public BufferedImage getImage(){
        return image;
    }

    public Rectangle getBound() {
        rectBound = new Rectangle();
        rectBound.x = (int) x + 5;
        rectBound.y = (int) y + 5;
        rectBound.width = image.getWidth() - 10;
        rectBound.height = image.getHeight() - 10;
        return rectBound;
    }
}
