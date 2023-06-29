package gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gameinterface.Object;
import util.Animation;
import util.Resource;

import javax.xml.transform.Source;

public class MaBu implements Object {

	private float posX;
	private float posY;
	private float speedY;
	private int directionY;

	private int hitPoint = 3;
	private int beAttacked;
	private BufferedImage image;
	private Rectangle rectBound;

	public MaBu(float posX, float posY, float speedY, int directionY) {
		image = Resource.getResourceImage("data/Buu_0.png");
		this.posX = posX;
		this.posY = posY;
		this.speedY = speedY;
		this.directionY = directionY;
		beAttacked = 0;
	}

	@Override
	public void update() {
		if (posY <= 0 || posY >= 45) {
			directionY *= -1;
		}
		posY += directionY * speedY;
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(image, (int) posX, (int) posY, null);
	}

	@Override
	public Rectangle getBound() {
		rectBound = new Rectangle();
		rectBound.x = (int) posX + 5;
		rectBound.y = (int) posY + 5;
		rectBound.width = image.getWidth() - 10;
		rectBound.height = image.getHeight() - 10;
		return rectBound;
	}

	@Override
	public boolean isOutOfScreen() {
		// TODO Auto-generated method stub
		return false;
	}


	public void dead (boolean isDeath) {
		System.out.println("Buu is dead");
		beAttacked = 0;
	}
	
	public void revive (boolean isRevive) {
		
	}

	public int getHitPoint(){
		return hitPoint;
	}

	public int getBeAttacked(){
		return beAttacked;
	}

	public void addBeAttacked(){
		beAttacked++;
	}

	public void resetBeAttacked(){
		beAttacked = 0;
	}

}
