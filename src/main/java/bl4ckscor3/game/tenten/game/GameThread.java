package bl4ckscor3.game.tenten.game;

import bl4ckscor3.game.tenten.TenTen;

public class GameThread extends Thread implements Runnable
{
	@Override
	public void run()
	{
		int targetTps = 60;
		double secondsPerTick = 1.0D / targetTps; //how long to wait between each update
		double nanosecondsPerTick = secondsPerTick * 1_000_000_000.0D;
		double then = System.nanoTime();
		double now = System.nanoTime();
		double unprocessed = 0;

		while(true)
		{
			now = System.nanoTime();
			unprocessed += (now - then) / nanosecondsPerTick;
			then = now;

			while(unprocessed >= 1)
			{
				unprocessed--;
			}

			try
			{
				Thread.sleep(1);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			TenTen.getGame().repaint();
		}
	}
}
