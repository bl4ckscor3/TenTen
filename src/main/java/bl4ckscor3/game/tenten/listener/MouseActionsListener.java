package bl4ckscor3.game.tenten.listener;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import bl4ckscor3.game.tenten.TenTen;
import bl4ckscor3.game.tenten.game.Area;

public class MouseActionsListener implements MouseListener
{
	private List<Area> spawnAreas = new ArrayList<Area>();
	private List<Area> playAreas = new ArrayList<Area>();

	@Override
	public void mouseReleased(MouseEvent e)
	{
		//return if the mosue button pressed is not lmb, also disable presses when the game has ended
		if(e.getButton() != 1 || TenTen.getGame().hasEnded())
		{
			//queue a reset when the game has ended and rmb has been clicked
			if(e.getButton() == 3 && TenTen.getGame().hasEnded())
				TenTen.getGame().startReset();
			return;
		}

		//if: check if a spawn area has been clicked if the player has not picked up anything
		if(!TenTen.getGame().hasPickedUp())
		{
			for(int i = 0; i < spawnAreas.size(); i++)
			{
				if(spawnAreas.get(i).isIn(e.getX(), e.getY()))
				{
					TenTen.getGame().pickUp(i);
					return;
				}
			}
		}
		else
		{
			//check if a spawn area has been clicked if the player has picked up something
			for(int i = 0; i < spawnAreas.size(); i++)
			{
				if(spawnAreas.get(i).isIn(e.getX(), e.getY()))
				{
					TenTen.getGame().placeBack(i);
					return;
				}
			}

			//check if a field has been clicked
			for(int i = 0; i < playAreas.size(); i++)
			{
				if(playAreas.get(i).isIn(e.getX(), e.getY()))
					TenTen.getGame().tryPutDown(i / 10, i % 10);
			}
		}
	}

	/**
	 * Adds a listener for an area where tiles can spawn in
	 * @param x The top left x position of the area
	 * @param y The top left y position of the area
	 * @param width The width of the area
	 * @param height The height of the area
	 */
	public void addSpawnArea(int x, int y, int width, int height)
	{
		spawnAreas.add(new Area(x, x + width, y, y + height));
	}

	/**
	 * Adds a listener for an area where tiles can be placed in (field)
	 * @param x The top left x position of the area
	 * @param y The top left y position of the area
	 * @param width The width of the area
	 * @param height The height of the area
	 */
	public void addPlayArea(int x, int y, int width, int height)
	{
		playAreas.add(new Area(x, x + width, y, y + height));
	}

	/**
	 * @return The Areas that define the positions of all spawn areas as a list
	 */
	public List<Area> getSpawnAreas()
	{
		return spawnAreas;
	}

	/**
	 * @return The Areas that define the positions of all field areas as a list
	 */
	public List<Area> getPlayAreas()
	{
		return playAreas;
	}

	@Override
	public void mouseClicked(MouseEvent e){}

	@Override
	public void mousePressed(MouseEvent e){}

	@Override
	public void mouseEntered(MouseEvent e){}

	@Override
	public void mouseExited(MouseEvent e){}
}
