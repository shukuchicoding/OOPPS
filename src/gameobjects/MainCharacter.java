package gameobjects;

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import util.Animation;
import util.Resource;

public class MainCharacter {
	public static final int LAND_POSY = 95; 
	public static final float GRAVITY = 0.4f;

	private static final int NORMAL_RUN = 0;
	private static final int JUMPING = 1;
	private static final int DOWN_RUN = 2;
	private static final int ATTACK = 3;
	private static final int DEATH = 4;
	private static final int START = 5; // update them trang thai start

	private float posX;
	private float posY;
	private float speedX;
	private float speedY;
	private Rectangle rectBound;

	public int score = 0;

	private int state = START;
	private int level;

	private BufferedImage startImage;
	private Animation normalRunAnim;
	private BufferedImage jumping;
	private Animation downRunAnim;
	private BufferedImage deathImage;
	private Animation attackAnim;

	private AudioClip jumpSound;
	private AudioClip deadSound;
	private AudioClip scoreUpSound;

	static ArrayList bullets;

	private boolean attackCheck = false;

	public MainCharacter() {
		posX = 50;
		posY = LAND_POSY;
		rectBound = new Rectangle();
		startImage = Resource.getResourceImage("data/Goku_start.png");
		normalRunAnim = new Animation(100);

//		normalRunAnim.addFrame(Resource.getResourceImage("data/normal_run_1.png"));
//		normalRunAnim.addFrame(Resource.getResourceImage("data/normal_run_2.png"));
//
//		
//		jumping = Resource.getResourceImage("data/jumping.png");

		normalRunAnim.addFrame(Resource.getResourceImage("data/Goku_run_1.png"));
		normalRunAnim.addFrame(Resource.getResourceImage("data/Goku_run_2.png"));
		normalRunAnim.addFrame(Resource.getResourceImage("data/Goku_run_3.png"));
		normalRunAnim.addFrame(Resource.getResourceImage("data/Goku_run_4.png"));
		normalRunAnim.addFrame(Resource.getResourceImage("data/Goku_run_5.png"));
		normalRunAnim.addFrame(Resource.getResourceImage("data/Goku_run_6.png"));

		jumping = Resource.getResourceImage("data/Goku_jump.png");

		
		downRunAnim = new Animation(60);
		downRunAnim.addFrame(Resource.getResourceImage("data/Goku_down_run.png"));
//		downRunAnim.addFrame(Resource.getResourceImage("data/main-character6.png"));

		deathImage = Resource.getResourceImage("data/Goku_death.png");
		attackAnim = new Animation(100);
		attackAnim.addFrame(Resource.getResourceImage("data/Goku_attack_1.png"));
		attackAnim.addFrame(Resource.getResourceImage("data/Goku_attack_2.png"));

		bullets = new ArrayList<>();

		try {
			jumpSound = Applet.newAudioClip(new URL("file", "", "data/jump.wav"));
			deadSound = Applet.newAudioClip(new URL("file", "", "data/dead.wav"));
			scoreUpSound = Applet.newAudioClip(new URL("file", "", "data/scoreup.wav"));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.level = 1;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public void draw(Graphics g) {
		switch (state) {
		case NORMAL_RUN:
			if (getAttackCheck() == false){
				g.drawImage(normalRunAnim.getFrame(), (int) posX, (int) posY, null);
			}else {
				g.drawImage(Resource.getResourceImage("data/Goku_attack_2.png"), (int) posX, (int) posY, null);
				attackCheck = false;
			}
			break;
		case JUMPING:
			g.drawImage(jumping, (int) posX, (int) posY, null);
			break;
		case DOWN_RUN:
			g.drawImage(downRunAnim.getFrame(), (int) posX, (int) (posY + 20), null);
			break;
		case DEATH:
			g.drawImage(deathImage, (int) posX, (int) posY, null);
			break;
		case START:
			g.drawImage(startImage, (int) posX, (int) posY - 5, null);
			break;
		case ATTACK:
			g.drawImage(Resource.getResourceImage("data/Goku_attack_2.png"), (int) posX, (int) posY, null);
			//g.drawImage(attackAnim.getFrame(), (int) posX, (int) posY, null);
			break;
		}
	}
	
	public void update() {
		normalRunAnim.updateFrame();
		downRunAnim.updateFrame();
		attackAnim.updateFrame();
		ArrayList bullets = MainCharacter.getBullets();
		for(int w = 0; w < bullets.size(); w++){
			attackCheck = true;
			GokuBullet m = (GokuBullet) bullets.get(w);
			if (m.getVisible() == true){
				m.move();
			}else{
				bullets.remove(w);
			}
		}

		if ( this.score > 150 * level){
			this.setSpeedX(speedX);
			level++;
		}
		if (posY >= LAND_POSY) {
			posY = LAND_POSY;
			if (state != DOWN_RUN) {
				state = NORMAL_RUN;
			}
		}
		
		else {
			speedY += GRAVITY;
			posY += speedY;
		}
	}
	
	public void jump() {
		if (posY >= LAND_POSY) {
			if (jumpSound != null) {
				jumpSound.play();
			}
			speedY = -9.2f; //9->9.25 
			posY += speedY * 0.0675;
			state = JUMPING;
		}
	}
	
	public void down(boolean isDown) {
		if (state == JUMPING) {
			return;
		}
		else if (isDown) {
			state = DOWN_RUN;
		}
		else {
			state = NORMAL_RUN;
		}
	}
	
	public void attack(boolean isAttack) {
//		if (state == NORMAL_RUN) {
//			return;
//		}
		if (isAttack) {
			state = ATTACK;
			attackCheck = true;
		}
//		else {
//			state = NORMAL_RUN;
//		}
	}
	
	public void dead(boolean isDeath) {
		if (isDeath) {
			state = DEATH;
		} else {
			state = NORMAL_RUN;
		}
	}
	
	public void reset() {
		posY = LAND_POSY;
		score = 0;
	}
	
	public void playDeadSound() {
		deadSound.play();
	}
	
	public void upScore() {
		score += 20;
		if (score % 100 == 0) {
			scoreUpSound.play();
		}
	}
	
	public Rectangle getBound() {
		rectBound = new Rectangle();
		if (state == DOWN_RUN) {
			rectBound.x = (int) posX + 5;
			rectBound.y = (int) posY + 20;
			rectBound.width = downRunAnim.getFrame().getWidth() - 10;
			rectBound.height = downRunAnim.getFrame().getHeight();
		} else if (state == NORMAL_RUN) {
			rectBound.x = (int) posX + 5;
			rectBound.y = (int) posY;
			rectBound.width = normalRunAnim.getFrame().getWidth() - 10;
			rectBound.height = normalRunAnim.getFrame().getHeight();
		} else if (state == ATTACK) {
			rectBound.x = (int) posX + 5;
			rectBound.y = (int) posY;
			rectBound.width = attackAnim.getFrame().getWidth() - 10;
			rectBound.height = attackAnim.getFrame().getHeight();
		} else if (state == JUMPING) {
			rectBound.x = (int) posX + 5;
			rectBound.y = (int) posY;
			rectBound.width = jumping.getWidth() - 10;
			rectBound.height = jumping.getHeight();
		}
		return rectBound;
	}

	public void fire(){
		GokuBullet z = new GokuBullet((int) posX, (int) posY);
		bullets.add(z);
	}

	public static ArrayList getBullets(){
		return bullets;
	}

	public boolean getAttackCheck(){
		return attackCheck;
	}
}
