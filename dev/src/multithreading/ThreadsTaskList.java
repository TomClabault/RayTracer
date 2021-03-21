package multithreading;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadsTaskList
{
	private ArrayList<TileTask> taskList;
	
	private AtomicInteger totalTaskCount;
	private AtomicInteger totalTaskGiven;
	private AtomicInteger totalTaskFinished;
	
	public ThreadsTaskList()
	{
		this.taskList = new ArrayList<>();
		
		this.totalTaskCount = new AtomicInteger(0);
		this.totalTaskFinished = new AtomicInteger(0);
		this.totalTaskGiven = new AtomicInteger(0);;
	}
	
	public boolean compareAndSetTaskGiven(int expectedValue, int newValue)
	{
		return this.totalTaskGiven.compareAndSet(expectedValue, newValue);
	}
	
	public int getAndIncrementTaskGiven()
	{
		return this.totalTaskGiven.getAndIncrement();
	}
	
	public TileTask getTask(int index)
	{
		return this.taskList.get(index);
	}
	
	public int getTotalTaskCount()
	{
		return this.totalTaskCount.get();
	}
	
	public int getTotalTaskFinished()
	{
		return this.totalTaskFinished.get();
	}
	
	public int getTotalTaskGiven()
	{
		return this.totalTaskGiven.get();
	}
	
	public void incrementTaskFinished()
	{
		this.totalTaskFinished.addAndGet(1);
	}
	
	public void incrementTaskGiven()
	{
		this.totalTaskGiven.addAndGet(1);
	}
	
	/*
	 * Permet d'initialiser la liste des tâches
	 * 
	 * @param nbCore Combien de processeur de calcul va utiliser le rendu
	 * @param renderWidth Largeur en pixel de l'image rendue
	 * @param renderHieght Hauteur en pixel de l'image rendue
	 */
	public void initTaskList(int nbCore, int renderWidth, int renderHeight)
	{
		int tilesWidth = renderWidth / nbCore;
		int tilesHeight = renderHeight / nbCore;
		
		int tilesCountX = renderWidth / tilesWidth; tilesCountX = (tilesCountX * tilesWidth < renderWidth) ? tilesCountX + 1 : tilesCountX; 
		int tilesCountY = renderHeight / tilesHeight; tilesCountY = (tilesCountY * tilesHeight < renderHeight) ? tilesCountY + 1 : tilesCountY;
		
		for(int y = 0; y < tilesCountY; y++)
		{
			int startY = y*tilesHeight;
			int endY = y*tilesHeight + tilesHeight; endY = endY > renderHeight ? renderHeight : endY;
			for(int x = 0; x < tilesCountX; x++)
			{
				int startX = x*tilesWidth;
				int endX = x*tilesWidth + tilesWidth; endX = endX > renderWidth ? renderWidth : endX;
			
				this.taskList.add(new TileTask(startX, startY, endX, endY));
				this.totalTaskCount.addAndGet(1);
			}
		}
	}
}
