package gameui;

import javax.swing.*;

public class GameWindow extends JFrame {
	public static final int SCREEN_WIDTH = 800;
	private GameScreen gameScreen;

	public GameWindow() {
		super("Ác mộng của Goku");
		setSize(SCREEN_WIDTH, 235);
		setLocation(400, 200);
		ImageIcon img = new ImageIcon("data/icon.png");
		setIconImage(img.getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		gameScreen = new GameScreen();
		addKeyListener(gameScreen);
		add(gameScreen);
	}

	public void startGame() {
		setVisible(true);
		gameScreen.startGame();
	}

	public static void main(String args[]) {
		(new GameWindow()).startGame();
	}
}
