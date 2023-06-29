package gamemanager;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import gameinterface.Object;
import gameobjects.*;
import gameinterface.GameSettings;
import util.Resource;

public class GameManager extends GameSettings {
	private MainCharacter mainCharacter;
	private MaBu mabu;

	private BufferedImage tree;
	private BufferedImage stone;

	private List<Object> obstacles;
	private List<MabuBullet> mabuBullet;

	private Random rand;

	private long mabuPreviousShoot; // milisecond

	public GameManager(MainCharacter mainCharacter) {
		rand = new Random();
		tree = Resource.getResourceImage("data/tree.png");
		stone = Resource.getResourceImage("data/stone.png");

		this.mainCharacter = mainCharacter;
		mabu = new MaBu(mabuPosX, mabuPosY, mabuSpeedY, mabuDirectionY);

		obstacles = new ArrayList<>();
		obstacles.add(createObstacle());

		mabuBullet = new ArrayList<>();
		mabuPreviousShoot = System.currentTimeMillis();
	}

	public void update() {
		mabu.update();
		mabuBulletUpdate();
		for (Object o : obstacles) {
			o.update();
		}

		if ((System.currentTimeMillis() - mabuPreviousShoot) >= mabuShootingPeriod) {
			mabuFire();
			mabuPreviousShoot = System.currentTimeMillis();
		}

		if (this.isCollision1()) {
			mabu.addBeAttacked();
			mainCharacter.upScore(2);

			if (mabu.getBeAttacked() == mabu.getHitPoint()) {
				mabu.dead(true);
			}
		}
	}

	public void mabuBulletUpdate() {
		for (MabuBullet bullet : mabuBullet) {
			bullet.update();
		}

		if (mabuBullet.size() > 0) {
			MabuBullet bullet = mabuBullet.get(0);
			if (bullet.isOutOfScreen()) {
				mabuBullet.remove(0);
			}
		}

		if ( mainCharacter.score >= 200 * mainCharacter.getLevel() + 50){
			mabuBulletSpeed++;
		}
	}

	public void mabuFire() {
		if (!mabu.isDead) {
			Rectangle mabuBound = mabu.getBound();
			mabuBullet.add(new MabuBullet(mabuBound.x, mabuBound.y, mabuBulletSpeed));
		}
	}

	public void draw(Graphics g) {
		mabu.draw(g);

		for (Object o : obstacles) {
			o.draw(g);
		}

		for (MabuBullet bullet : mabuBullet) {
			bullet.draw(g);
		}

		Object obstacle = obstacles.get(0);
		if (obstacle.isOutOfScreen()) {
			mainCharacter.upScore(1);
			obstacles.remove(0);
			obstacles.add(createObstacle());
		}
	}

	private Object createObstacle() {
		int type = rand.nextInt(2);
		if (type == 0) {
			return new TreeAndStone(mainCharacter, 800, tree.getWidth() - 10, tree.getHeight() - 10, tree);
		} else {
			return new TreeAndStone(mainCharacter, 800, stone.getWidth() - 10, stone.getHeight() - 10, stone);
		}
	}

	public boolean isCollision() {
		for (Object o : obstacles) {
			if (mainCharacter.getBound().intersects(o.getBound())) {
				return true;
			}
		}
		for (MabuBullet bullet : mabuBullet) {
			if (mainCharacter.getBound().intersects(bullet.getBound())) {
				return true;
			}
		}

		return false;
	}

	public boolean isCollision1() {
		ArrayList bullets = MainCharacter.getBullets();
		for (int w = 0; w < bullets.size(); w++) {
			GokuBullet m = (GokuBullet) bullets.get(w);
			if (mabu.getBound().intersects(m.getBound())) {
				bullets.remove(w);
				return true;
			}
		}
		return false;
	}

	public boolean isCollision2() { // Dan goku khong xuyen qua tree va stone
		ArrayList bullets = MainCharacter.getBullets();
		for (Object o : obstacles) {
			for (int w = 0; w < bullets.size(); w++) {
				GokuBullet m = (GokuBullet) bullets.get(w);
				if (m.getBound().intersects(o.getBound())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isCollision3() {
		ArrayList bullets = MainCharacter.getBullets();
		for (MabuBullet bullet : mabuBullet) {
			for (int w = 0; w < bullets.size(); w++) {
				GokuBullet m = (GokuBullet) bullets.get(w);
				if (m.getBound().intersects(bullet.getBound())) {
					mabuBullet.remove(bullet);
					return true;
				}
			}
		}
		return false;
	}

	public void reset() {
		obstacles.clear();
		obstacles.add(createObstacle());
		mabuBullet.clear();
		mabu.resetBeAttacked();
	}
}
