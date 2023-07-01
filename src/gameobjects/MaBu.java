package gameobjects;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import gameinterface.Object;
import util.Animation;
import util.Resource;
//import util.TimeInterval;

import javax.xml.transform.Source;

public class MaBu implements Object {

	public static final int NORMAL = 0;
	public static final int PRE_ATTACK = 1;
	public static final int ATTACK = 3;
	public static final int BE_HIT = 4;
	public static final int WIN = 5;
	public static final int DEAD = 6;

	public int state;

	public float posX;
	public float posY;
	public float speedY;
	public int directionY;

	public int hitPoint = 5;
	public int beAttacked;
	public long deadTime;

	public long previousShoot; // milisecond
	public long prepareTime;
	public long previousBeHit;
	public int shootingTimeout = 500;
	public int beHitTimeout = 200;

	public Rectangle rectBound;
	public BufferedImage normalImage;
	public BufferedImage prepareAtkImage;
	public BufferedImage attackImage;
	public BufferedImage beHitImage;
	public BufferedImage winImage;

	public MaBu(float posX, float posY, float speedY, int directionY) {
		this.posX = posX;
		this.posY = posY;
		this.speedY = speedY;
		this.directionY = directionY;
		beAttacked = 0;
		deadTime = 0;

		state = NORMAL;

		normalImage = Resource.getResourceImage("data/Buu_0.png");
		prepareAtkImage = Resource.getResourceImage("data/Buu_1.png");
		attackImage = Resource.getResourceImage("data/Buu_2.png");
		beHitImage = Resource.getResourceImage("data/Buu_3.png");
		winImage = Resource.getResourceImage("data/Buu_4.png");
	}

	@Override
	public void update() {
		if (posY <= 0 || posY >= 45) {
			directionY *= -1;
		}
		posY += directionY * speedY;

		if ((System.currentTimeMillis() - deadTime > 4000) && isDead()) {
			resetBeAttacked();
		}

		if (state == ATTACK &&
				System.currentTimeMillis() - prepareTime >= shootingTimeout * 2) {
			state = NORMAL;
		}

		if (state == BE_HIT &&
				System.currentTimeMillis() - previousBeHit >= beHitTimeout) {
			state = NORMAL;
		}
	}

	@Override
	public void draw(Graphics g) {
		if (!isDead()) {
			switch (state) {
				case NORMAL:
					g.drawImage(normalImage,
							(int) posX, (int) posY, null);
					break;
				case PRE_ATTACK:
					g.drawImage(prepareAtkImage,
							(int) posX, (int) posY, null);
					break;
				case ATTACK:
					g.drawImage(attackImage,
							(int) posX, (int) posY, null);
					break;
				case BE_HIT:
					g.drawImage(beHitImage,
							(int) posX, (int) posY, null);
					g.drawString(beAttacked + "/ " + hitPoint, 0, 10); 
					break;
				case WIN:
					g.drawImage(winImage,
							(int) posX, (int) posY, null);
					break;
			}
		}
	}

	@Override
	public Rectangle getBound() {
		rectBound = new Rectangle();
		rectBound.x = (int) posX + 5;
		rectBound.y = (int) posY + 5;
		rectBound.width = normalImage.getWidth() - 10;
		rectBound.height = normalImage.getHeight() - 10;
		return rectBound;
	}

	@Override
	public boolean isOutOfScreen() {
		// TODO Auto-generated method stub
		return false;
	}

	public void dead(boolean isDeath) {
		state = DEAD;
		deadTime = System.currentTimeMillis();
	}

	public boolean isDead() {
		if (state == DEAD) {
			return true;
		}
		return false;
	}

	public void revive(boolean isRevive) {
		// isDead() = false;
	}

	public int getHitPoint() {
		return hitPoint;
	}

	public int getBeAttacked() {
		return beAttacked;
	}

	public void addBeAttacked() {
		beAttacked++;
	}

	public void resetBeAttacked() {
		previousShoot = System.currentTimeMillis();
		beAttacked = 0;
		// isDead = false;
		state = NORMAL;
	}

	public void preAttack() {
		state = PRE_ATTACK;
		prepareTime = System.currentTimeMillis();
	}

	public void attack() {
		state = ATTACK;
	}

	public void beHit() {
		state = BE_HIT;
		previousBeHit = System.currentTimeMillis();
	}

	public void win() {
		state = WIN;
	}

	public void normal() {
		state = NORMAL;
	}

	public boolean isPrepare() {
		if (state == PRE_ATTACK) {
			return true;
		}
		return false;
	}
}
