package bl4ckscor3.game.tenten.game;

public class Area
{
	private int x1;
	private int x2;
	private int y1;
	private int y2;

	/**
	 * A simple rectangular area
	 * @param x1 The first x position of the area
	 * @param x2 The second x position of the area
	 * @param y1 The first y position of the area
	 * @param y2 The second y position of the area
	 */
	public Area(int x1, int x2, int y1, int y2)
	{
		this.x1 = Math.min(x1, x2);
		this.x2 = Math.max(x1, x2);
		this.y1 = Math.min(y1, y2);
		this.y2 = Math.max(y1, y2);
	}

	/**
	 * Checks wether a given point is within this area
	 * @param x The x coordinate of the point to check
	 * @param y The y coordinate of the point to check
	 * @return true if the given point is within the bounds of this area, false otherwise
	 */
	public boolean isIn(int x, int y)
	{
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}

	/**
	 * @return The start position of this area (minimum x)
	 */
	public int getX1()
	{
		return x1;
	}

	/**
	 * @return The start position of this area (maximum x)
	 */
	public int getX2()
	{
		return x2;
	}

	/**
	 * @return The end position of this area (minimum y)
	 */
	public int getY1()
	{
		return y1;
	}

	/**
	 * @return The end position of this area (maximum y)
	 */
	public int getY2()
	{
		return y2;
	}

	@Override
	public String toString()
	{
		return String.format("%s->%s | %s->%s", x1, x2, y1, y2);
	}
}
