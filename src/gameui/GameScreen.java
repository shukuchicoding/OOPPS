package gameui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import gamemanager.GameManager;
import gameobjects.*;
import util.Resource;
import gameinterface.GameSettings;

public class GameScreen extends JPanel implements Runnable, KeyListener, MouseListener {
	private static final int START_GAME_STATE = 0;
	private static final int GAME_PLAYING_STATE = 1;
	private static final int GAME_OVER_STATE = 2;

	private Land land;
	private MainCharacter mainCharacter;
	private GameManager enemyManager;
	private Cloud clouds;
	private Thread thread;
	private MaBu maBu;

	private boolean isKeyPressed;

	private int gameState = START_GAME_STATE;

	private BufferedImage startImage;
	private BufferedImage replayButtonImage;
	private BufferedImage gameOverButtonImage;
	private BufferedImage gameStartButtonImage;
	private BufferedImage bgGameImage;
	private Rectangle bounds;

	public GameScreen() {

		mainCharacter = new MainCharacter();
		// land = new Land(GameWindow.SCREEN_WIDTH, mainCharacter);
		mainCharacter.setSpeedX(5);
		replayButtonImage = Resource.getResourceImage("data/replay_button.png");
		gameOverButtonImage = Resource.getResourceImage("data/gameover_text.png");
		gameStartButtonImage = Resource.getResourceImage("data/gamestart_text.png");
		bgGameImage = Resource.getResourceImage("data/bg1.png");
		enemyManager = new GameManager(mainCharacter);
		clouds = new Cloud(GameWindow.SCREEN_WIDTH, mainCharacter);
		maBu = enemyManager.mabu;
	}

	public void startGame() {
		thread = new Thread(this);
		thread.start();
		this.addMouseListener(this);
	}

	public void gameUpdate() {
		if (gameState == GAME_PLAYING_STATE) {
			clouds.update();
			// land.update();
			mainCharacter.update();
			enemyManager.update();
			if (enemyManager.isCollision()) {
				mainCharacter.playDeadSound();
				gameState = GAME_OVER_STATE;
				mainCharacter.dead(true);
				maBu.win();
			}

		}
	}

	@Override
	public void paint(Graphics g) {
		g.setColor(Color.decode("#f7f7f7"));
		g.fillRect(0, 0, getWidth(), getHeight());
		g.drawImage(bgGameImage, 0, 0, null);
		g.setFont(new Font("TimesRoman", Font.BOLD, 15));

		Graphics2D g2d = (Graphics2D) g;

		switch (gameState) {
			case START_GAME_STATE:
				mainCharacter.draw(g);
				g.drawImage(gameStartButtonImage, 250, 30, null);
				bounds = new Rectangle(250, 30, 300, 49);
				// g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
				break;
			case GAME_PLAYING_STATE:
			case GAME_OVER_STATE:
				clouds.draw(g);
				// land.draw(g);

				ArrayList bullets = MainCharacter.getBullets();
				for (int w = 0; w < bullets.size(); w++) {

					GokuBullet m = (GokuBullet) bullets.get(w);
					g2d.drawImage(m.getImage(), m.getX(), m.getY(), null);
					if (enemyManager.isCollision2()) {
						bullets.remove(w);
					}
					if (enemyManager.isCollision3()) {
						bullets.remove(w);
					}
				}

				enemyManager.draw(g);
				mainCharacter.draw(g);

				g.setColor(Color.black);
				g.drawString("SCORE: " + mainCharacter.score, 500, 20);

				if (gameState == GAME_OVER_STATE) {
					bullets.clear();

					g.drawImage(gameOverButtonImage, 270, 20, null);

					mainCharacter.setSpeedX(5);

					g.drawImage(replayButtonImage, 378, 74, null);
					g.setColor(Color.WHITE);
					g.drawString("TOTAL SCORE: " + mainCharacter.score, 330, 130);
					bounds = new Rectangle(378, 74, 34, 30);
					// g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
				}
				break;
		}
	}

	@Override
	public void run() {
		int fps = 100;
		long msPerFrame = 1000 * 1000000 / fps;
		long lastTime = 0;
		long elapsed;

		int msSleep;
		int nanoSleep;

		long endProcessGame;
		long lag = 0;

		while (true) {
			gameUpdate();
			repaint();
			endProcessGame = System.nanoTime();
			elapsed = (lastTime + msPerFrame - System.nanoTime());
			msSleep = (int) (elapsed / 1000000);
			// nanoSleep = (int)(elapsed % 1000000);
			if (msSleep <= 0) {
				lastTime = System.nanoTime();
				continue;
			}
			try {
				Thread.sleep(msSleep);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lastTime = System.nanoTime();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!isKeyPressed) {
			isKeyPressed = true;
			// System.out.println("keyPressed "+e.getKeyCode());
			switch (gameState) {
				case START_GAME_STATE:
					// if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					// mainCharacter.jump();
					// }
					// else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					// mainCharacter.down(true);
					// }
					gameState = GAME_PLAYING_STATE;

					break;
				case GAME_PLAYING_STATE:
					if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP) {
						mainCharacter.jump();
					}
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						mainCharacter.down(true);
					}
					if (e.getKeyCode() == KeyEvent.VK_A) {
						mainCharacter.attack(true);
						mainCharacter.fire();
					}
					break;
				case GAME_OVER_STATE:
					if (e.getKeyCode() == KeyEvent.VK_SPACE) {
						gameState = GAME_PLAYING_STATE;
						resetGame();
					}

					break;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		isKeyPressed = false;
		if (gameState == GAME_PLAYING_STATE) {
			if (e.getKeyCode() == KeyEvent.VK_DOWN) {
				mainCharacter.down(false);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void resetGame() {
		enemyManager.reset();
		mainCharacter.dead(false);
		mainCharacter.reset();

	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Point clicked = e.getPoint();
		if (bounds != null) {
			if (bounds.contains(clicked) && gameState == GAME_OVER_STATE) {
				gameState = GAME_PLAYING_STATE;
				resetGame();
			} else if (bounds.contains(clicked) && gameState == START_GAME_STATE) {
				gameState = GAME_PLAYING_STATE;
				resetGame();
			}

			else
				return;
		}

	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
