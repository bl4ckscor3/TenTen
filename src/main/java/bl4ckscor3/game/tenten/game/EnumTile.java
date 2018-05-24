package bl4ckscor3.game.tenten.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Arrays;
import java.util.List;

import bl4ckscor3.game.tenten.TenTen;

public enum EnumTile
{
	ONE					(1, new Color(0xA800BA), new Point()),
	TWO_VERTICAL		(2, new Color(0xFFC700), new Point(), new Point(0, 1)),
	TWO_HORIZONTAL		(2, new Color(0xFFC700), new Point(), new Point(1, 0)),
	THREE_VERTICAL		(3, new Color(0xFF6100), new Point(), new Point(0, 1), new Point(0, 2)),
	THREE_HORIZONTAL	(3, new Color(0xFF6100), new Point(), new Point(1, 0), new Point(2, 0)),
	THREE_TOP_LEFT		(3, new Color(0x00FF61), new Point(), new Point(0, 1), new Point(1, 0)),
	THREE_TOP_RIGHT		(3, new Color(0x00FF61), new Point(), new Point(1, 0), new Point(1, 1)),
	THREE_BOTTOM_LEFT	(3, new Color(0x00FF61), new Point(), new Point(0, 1), new Point(1, 1)),
	THREE_BOTTOM_RIGHT	(3, new Color(0x00FF61), new Point(1, 1), new Point(1, 0), new Point(0, 1)),
	TWO_BY_TWO			(4, new Color(0xFF0000), new Point(), new Point(0, 1), new Point(1, 0), new Point(1, 1)),
	FOUR_HORIZONTAL		(4, new Color(0x0000FF), new Point(), new Point(1, 0), new Point(2, 0), new Point(3, 0)),
	FOUR_VERTICAL		(4, new Color(0x0000FF), new Point(), new Point(0, 1), new Point(0, 2), new Point(0, 3)),
	FIVE_TOP_LEFT		(5, new Color(0x0094FF), new Point(), new Point(0, 1), new Point(0, 2), new Point(1, 0), new Point(2, 0)),
	FIVE_TOP_RIGHT		(5, new Color(0x0094FF), new Point(), new Point(1, 0), new Point(2, 0), new Point(2, 1), new Point(2, 2)),
	FIVE_BOTTOM_LEFT	(5, new Color(0x0094FF), new Point(), new Point(0, 1), new Point(0, 2), new Point(1, 2), new Point(2, 2)),
	FIVE_BOTTOM_RIGHT	(5, new Color(0x0094FF), new Point(2, 2), new Point(1, 2), new Point(0, 2), new Point(2, 1), new Point(2, 0)),
	FIVE_HORIZONTAL		(5, new Color(0x880012), new Point(), new Point(1, 0), new Point(2, 0), new Point(3, 0), new Point(4, 0)),
	FIVE_VERTICAL		(5, new Color(0x880012), new Point(), new Point(0, 1), new Point(0, 2), new Point(0, 3), new Point(0, 4)),
	THREE_BY_THREE		(9, new Color(0x009100), new Point(), new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(0, 2), new Point(1, 2), new Point(2, 2));

	private int scoreValue;
	private Color color;
	private List<Point> parts;

	/**
	 * Defines a placeable tile
	 * @param value The score value that this tile gives when placed down
	 * @param c The Color this tile should be rendered in
	 * @param parts The tile's parts. Each Point is relative to the top left position of the tile (top left would be new Point(0, 0);)
	 */
	private EnumTile(int value, Color c, Point... parts)
	{
		scoreValue = value;
		color = c;
		this.parts = Arrays.asList(parts);
	}

	/**
	 * Renders this tile
	 * @param g The Graphics to render with
	 * @param x The x position to render the tile at
	 * @param y The y position to render the tile at
	 * @param withOffset Wether or not to render the tile with half a tile size offset (used when a tile is picked up and rendered under the mouse)
	 */
	public void render(Graphics g, int x, int y, boolean withOffset)
	{
		int TILE_SIZE = TenTen.getGame().TILE_SIZE;

		g.setColor(color);

		for(int i = 0; i < parts.size(); i++)
		{
			g.fillRect(x + parts.get(i).x * TILE_SIZE - (withOffset ? TILE_SIZE / 2 : 0),
					y + parts.get(i).y * TILE_SIZE - (withOffset ? TILE_SIZE / 2 : 0), 32, 32);
		}
	}

	/**
	 * @return The score value that this tile gives when placed down
	 */
	public int getScoreValue()
	{
		return scoreValue;
	}

	/**
	 * @return The Color this tile should be rendered in
	 */
	public Color getColor()
	{
		return color;
	}

	/**
	 * @return The tile's parts
	 */
	public List<Point> getParts()
	{
		return parts;
	}
}
