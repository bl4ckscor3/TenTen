package bl4ckscor3.game.tenten;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import bl4ckscor3.game.tenten.game.Game;
import bl4ckscor3.game.tenten.game.GameThread;

public class TenTen
{
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static Game game;
	private static final JFrame FRAME = new JFrame();

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(() -> start());
	}

	/**
	 * Sets up and starts the game
	 */
	private static void start()
	{
		FRAME.setTitle("TenTen");
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setSize(WIDTH, HEIGHT);
		FRAME.getContentPane().setPreferredSize(new Dimension(WIDTH, HEIGHT));
		FRAME.pack();
		FRAME.setLocationRelativeTo(null);
		FRAME.setResizable(false);
		game = new Game(new GameThread());
		FRAME.add(game);
		FRAME.setVisible(true);
		game.getThread().start();
	}

	/**
	 * @return The Screen
	 */
	public static Game getGame()
	{
		return game;
	}
}
