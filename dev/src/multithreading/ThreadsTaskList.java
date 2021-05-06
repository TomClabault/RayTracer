package multithreading;

import java.util.ArrayList;

/**
 * Definit une liste de tâche de calcul qui pourra etre utilisee par les multiples threads lors du rendu d'une scene. Voir {@link multithreading.TileTask} pour la definition d'une tâche.<br>
 * Chaque thread pourra alors recuperer une tâche de la liste au moyen de la methode getTask.<br>
 * Le thread ayant recupere une tâche, le nombre de tâche donnee doit etre incremente grâce a la methode incrementTaskGiven.<br>
 * La gestion du nombre de tâche finie est laissee a l'utilisateur.  
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
	 * Permet d'initialiser la liste des tâches pour une largeur et hauteur de rendu donnee
	 * 
	 * @param renderWidth Largeur en pixel de l'image rendue
	 * @param renderHeight Hauteur en pixel de l'image rendue
	 */
	public void initTaskList(int renderWidth, int renderHeight)
	{
		this.taskList = new ArrayList<>();
		
		//Des tuiles de 64*64 pixels semblent etre un bon choix arbitraire en terme de performances
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
	 * Permet de remettre a zero l'avancement de la liste des tâches. i.e. la liste garde les memes tâches mais est prete a etre reutilisee.
	 * Cette methode doit toujours etre appelee avant une nouvelle utilisation des tâches de cette liste. 
	 */
	public void resetTasksProgression()
	{
		this.totalTaskCount = 0;
		this.totalTaskFinished = 0;
		this.totalTaskGiven = 0;
	}
}
