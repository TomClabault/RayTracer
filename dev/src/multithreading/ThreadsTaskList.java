package multithreading;

import java.util.ArrayList;

public class ThreadsTaskList
{
	private ArrayList<TileTask> taskList;
	
	private int totalTaskCount;
	private int totalTaskGiven;
	private int totalTaskFinished;
	
	public ThreadsTaskList()
	{
		this.taskList = new ArrayList<>();
		
		this.totalTaskCount = 0;
		this.totalTaskFinished = 0;
		this.totalTaskGiven = 0;
	}
	
	public TileTask getTask(int index)
	{
		return this.taskList.get(index);
	}
	
	public int getTotalTaskCount()
	{
		return this.totalTaskCount;
	}
	
	public int getTotalTaskFinished()
	{
		return this.totalTaskFinished;
	}
	
	public int getTotalTaskGiven()
	{
		return this.totalTaskGiven;
	}
	
	public void incrementTaskFinished()
	{
		this.totalTaskFinished++;
	}
	
	public void incrementTaskGiven()
	{
		this.totalTaskGiven++;
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
				this.totalTaskCount++;
			}
		}
	}
}
