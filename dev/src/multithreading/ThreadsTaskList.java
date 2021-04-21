package multithreading;

import java.util.ArrayList;

/**
 * Définit une liste de tâche de calcul qui pourra être utilisée par les multiples threads lors du rendu d'une scène. Voir {@link multithreading.TileTask} pour la définition d'une tâche.<br>
 * Chaque thread pourra alors récupérer une tâche de la liste au moyen de la méthode getTask.<br>
 * Le thread ayant récupéré une tâche, le nombre de tâche donnée doit être incrémenté grâce à la méthode incrementTaskGiven.<br>
 * La gestion du nombre de tâche finie est laissée à l'utilisateur.  
 */
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
	
	synchronized public TileTask getTask(int index)
	{
		return this.taskList.get(index);
	}
	
	synchronized public int getTotalTaskCount()
	{
		return this.totalTaskCount;
	}
	
	synchronized public int getTotalTaskFinished()
	{
		return this.totalTaskFinished;
	}
	
	synchronized public int getTotalTaskGiven()
	{
		return this.totalTaskGiven;
	}
	
	synchronized public void incrementTaskFinished()
	{
		this.totalTaskFinished++;
	}
	
	synchronized public void incrementTaskGiven()
	{
		this.totalTaskGiven++;
	}
	
	/**
	 * Permet d'initialiser la liste des tâches pour une largeur et hauteur de rendu donnée
	 * 
	 * @param renderWidth Largeur en pixel de l'image rendue
	 * @param renderHeight Hauteur en pixel de l'image rendue
	 */
	public void initTaskList(int renderWidth, int renderHeight)
	{
		this.taskList = new ArrayList<>();
		
		//Des tuiles de 64*64 pixels semblent être un bon choix arbitraire en terme de performances
		int tilesWidth = 64;//renderWidth / 64;
		int tilesHeight = 64;//renderHeight / 64;
		
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
	
	/**
	 * Permet de remettre à zéro l'avancement de la liste des tâches. i.e. la liste garde les mêmes tâches mais est prête à être réutilisée.
	 * Cette méthode doit toujours être appelée avant une nouvelle utilisation des tâches de cette liste. 
	 */
	public void resetTasksProgression()
	{
		this.totalTaskCount = 0;
		this.totalTaskFinished = 0;
		this.totalTaskGiven = 0;
	}
}
