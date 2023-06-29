package gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gameinterface.GameSettings;
import gameinterface.Object;
import util.Resource;

public class MabuBullet implements Object {
	private float posX;
	private float posY;
	private float speedX;

	private BufferedImage image;
	private Rectangle rectBound;

	public MabuBullet(float posX, float posY, float speedX) {
		image = Resource.getResourceImage("data/bullet_Buu.png");
		this.posX = posX;
		this.posY = posY;
		this.speedX = speedX;
	}

	@Override
	public void update() {
		posX -= speedX;
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
		if (posX < -image.getWidth()) {
			return true;
		}
		return false;
	}

}
