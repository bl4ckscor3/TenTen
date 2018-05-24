package bl4ckscor3.game.tenten.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JPanel;

import bl4ckscor3.game.tenten.listener.MouseActionsListener;

public class Game extends JPanel
{
	private final ThreadLocalRandom r = ThreadLocalRandom.current();
	private final MouseActionsListener mouseListener;
	private final Font font = new Font("Calibri", 0, 55);
	private final GameThread thread;
	private final boolean[][] occupied = new boolean[10][10];
	private final Color[][] field = new Color[10][10];
	private int WIDTH;
	private int HEIGHT;
	public final int TILE_SIZE = 40;
	private int START_X;
	private int START_Y;
	private boolean shouldReset;
	private List<EnumTile> tilePool;
	public final EnumTile[] spawnedTiles = new EnumTile[3];
	private int score = 0;
	private int highscore = 0;
	private EnumTile pickedUp = null;
	private boolean ended = false;

	public Game(GameThread t)
	{
		thread = t;
		startReset();
		addMouseListener(mouseListener = new MouseActionsListener());
	}

	/**
	 * Draws everything on the screen
	 * @param g The Graphics to draw with
	 */
	@Override
	protected void paintComponent(Graphics g)
	{
		if(shouldReset)
		{
			reset();
			shouldReset = false;
		}

		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.ORANGE);
		g.setFont(font);
		g.drawString("Score:", START_X + TILE_SIZE * 11, START_Y + TILE_SIZE);
		g.drawString(score + "", START_X + TILE_SIZE * 11, START_Y + TILE_SIZE + font.getSize());
		g.drawString("Highscore:", START_X + TILE_SIZE * 11, START_Y + TILE_SIZE * 4);
		g.drawString(highscore + "", START_X + TILE_SIZE * 11, START_Y + TILE_SIZE * 4 + font.getSize());

		//draw play field and placed tiles
		for(int x = 0; x < 10; x++)
		{
			for(int y = 0; y < 10; y++)
			{
				g.setColor(occupied[x][y] ? field[x][y] : Color.LIGHT_GRAY);
				g.fillRect(x * TILE_SIZE + START_X, y * TILE_SIZE + START_Y, 32, 32);
			}
		}

		if(!ended)
			trySpawnNewTiles();
		else
			g.drawString("Game has ended! Rightclick to restart.", 10, 50);

		//draw spawn locations and the tiles on them
		for(int i = 0; i < spawnedTiles.length; i++)
		{
			int x = START_X - (int)(3.5D * TILE_SIZE) + i * TILE_SIZE * 6;
			int y = START_Y + (int)(TILE_SIZE * 10.5D);

			g.setColor(Color.GRAY);
			g.fillRect(x - 4, y - 4, TILE_SIZE * 5, TILE_SIZE * 5); //-4 for a slight border around the tiles

			if(spawnedTiles[i] != null)
				spawnedTiles[i].render(g, x, y, false);
		}

		//draw picked up tile on mouse
		if(pickedUp != null && getMousePosition() != null)
			pickedUp.render(g, getMousePosition().x, getMousePosition().y, true);

		//check for completed rows/columns
		boolean[] filledRows = {true, true, true, true, true, true, true, true, true, true};
		boolean[] filledColumns = {true, true, true, true, true, true, true, true, true, true};

		for(int x = 0; x < occupied.length; x++)
		{
			for(int y = 0; y < occupied[x].length; y++)
			{
				if(!occupied[x][y])
				{
					filledColumns[x] = false;
					filledRows[y] = false;
				}
			}
		}

		int pointsToAdd = 0;

		//actually remove completed rows/columns
		for(int x = 0; x < occupied.length; x++)
		{
			for(int y = 0; y < occupied[x].length; y++)
			{
				if(filledColumns[x])
				{
					occupied[x][y] = false;
					pointsToAdd++;
				}
				else if(filledRows[y])
				{
					occupied[x][y] = false;
					pointsToAdd++;
				}
			}
		}

		score += pointsToAdd;
		checkAndUpdateHighscore();

		if(canPickUp())
			checkPlaceability();
	}

	/**
	 * Picks up the tile at the given spawn position
	 * @param tile The spawn position to pick up the tile from. Has to be within the range of 0 and spawnedTiles.length
	 */
	public void pickUp(int tile)
	{
		if(!canPickUp())
			return;

		pickedUp = spawnedTiles[tile];
		spawnedTiles[tile] = null;
	}

	/**
	 * Places back the previously picked up tile onto the given spawn position
	 * @param tile The spawn position to place the tile down at. Has to be within the range of 0 and spawnedTiles.length
	 */
	public void placeBack(int tile)
	{
		if(canPickUp() || spawnedTiles[tile] != null)
			return;

		spawnedTiles[tile] = pickedUp;
		pickedUp = null;
	}

	/**
	 * Places the picked up tile onto the clicked position on the playing field, if possible
	 * @param spotX The x coordinate of the clicked field
	 * @param spotY The y coordinate of the clicked field
	 */
	public void tryPutDown(int spotX, int spotY)
	{
		if(!canPutDown(pickedUp, spotX, spotY))
			return;

		List<Point> parts = pickedUp.getParts();

		for(Point part : parts)
		{
			occupied[spotX + part.x][spotY + part.y] = true;
			field[spotX + part.x][spotY + part.y] = pickedUp.getColor();
		}

		score += pickedUp.getScoreValue();
		pickedUp = null;
	}

	/**
	 * Checks all tiles still remaining in the spawn positions wether they can be placed on the field or not
	 * If none can, the game will end
	 */
	public void checkPlaceability()
	{
		int placeable = spawnedTiles.length;
		int nullTiles = 0;

		for(EnumTile tile : spawnedTiles)
		{
			if(tile != null)
			{
				int possible = 100;

				for(int x = 0; x < occupied.length; x++)
				{
					for(int y = 0; y < occupied[x].length; y++)
					{
						if(!canPutDown(tile, x, y))
							possible--;
					}
				}

				if(possible == 0)
					placeable--;
			}
			else
			{
				placeable--;
				nullTiles++;
			}
		}

		if(placeable == 0 && nullTiles != 3)
			endGame();
	}

	/**
	 * Updates the highscore with the current score if score > highscore
	 */
	private void checkAndUpdateHighscore()
	{
		if(score > highscore)
			highscore = score;
	}

	/**
	 * @return true if the player has picked up a tile, false otherwise
	 */
	public boolean hasPickedUp()
	{
		return !canPickUp();
	}

	/**
	 * @return true if the place can pick up the tile (=has no tile picked up), false otherwise
	 */
	private boolean canPickUp()
	{
		return pickedUp == null;
	}

	/**
	 * Checks wether a tile can be put down at a specific position on the field
	 * @param tile The tile to check
	 * @param spotX The x coordinate of the clicked field
	 * @param spotY The y coordinate of the clicked field
	 * @return true if the given tile can be placed on the givne spot, false otherwise or if the given tile is null
	 */
	public boolean canPutDown(EnumTile tile, int spotX, int spotY)
	{
		if(tile == null)
			return false;

		for(Point p : tile.getParts())
		{
			int checkX = spotX + p.x;
			int checkY = spotY + p.y;

			if(checkY < 0 || checkX > 9 || checkY < 0 || checkY > 9 || occupied[checkX][checkY])
				return false;
		}

		return true;
	}

	/**
	 * @return true if the game has ended (=no spawned tiles can be placed down anymore), false otherwise
	 */
	public boolean hasEnded()
	{
		return ended;
	}

	/**
	 * Spawns new tiles at the spawn positions if necessary
	 */
	private void trySpawnNewTiles()
	{
		if(shouldReset || (spawnedTiles[0] == null && spawnedTiles[1] == null && spawnedTiles[2] == null && canPickUp()))
		{
			spawnedTiles[0] = tilePool.get(r.nextInt(tilePool.size()));
			spawnedTiles[1] = tilePool.get(r.nextInt(tilePool.size()));
			spawnedTiles[2] = tilePool.get(r.nextInt(tilePool.size()));
		}
	}

	/**
	 * Ends the game
	 */
	private void endGame()
	{
		ended = true;
	}

	/**
	 * Queues a game reset
	 */
	public void startReset()
	{
		shouldReset = true;
	}

	/**
	 * Resets the game. Highscore will be kept
	 */
	private void reset()
	{
		WIDTH = getWidth();
		HEIGHT = getHeight();
		START_X = WIDTH / 2 - (10 * TILE_SIZE) / 2;
		START_Y = HEIGHT / 4 - (10 * TILE_SIZE) / 4;
		mouseListener.getSpawnAreas().clear();
		mouseListener.getPlayAreas().clear();
		checkAndUpdateHighscore();
		score = 0;
		pickedUp = null;
		ended = false;

		for(int i = 0; i < spawnedTiles.length; i++)
		{
			int x = START_X - (int)(3.5D * TILE_SIZE) + i * TILE_SIZE * 6;
			int y = START_Y + (int)(TILE_SIZE * 10.5D);

			mouseListener.addSpawnArea(x - 4, y - 4, TILE_SIZE * 5, TILE_SIZE * 5);
		}

		for(int x = 0; x < 10; x++)
		{
			for(int y = 0; y < 10; y++)
			{
				mouseListener.addPlayArea(x * TILE_SIZE + START_X, y * TILE_SIZE + START_Y, 32, 32);
			}
		}

		for(int x = 0; x < 10; x++)
		{
			for(int y = 0; y < 10; y++)
			{
				occupied[x][y] = false;
			}
		}

		tilePool = Arrays.asList(EnumTile.values());
		Collections.shuffle(tilePool);
		trySpawnNewTiles();
		shouldReset = false;
	}

	/**
	 * @return The thread this game is running on
	 */
	public GameThread getThread()
	{
		return thread;
	}
}
