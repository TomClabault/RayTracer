package multithreading;

/**
 *	Represente une tâche de calcul pour le rendu de l'image.<br>
 *	Une tâche delimite une region de l'image. Les limites de la region peuvent etre obtenues grâce aux getters appropries.
 */
public class TileTask 
{
	private int startX, startY;
	private int endX, endY;
	
	/**
	 * Cree une tâche de calcul pour un thread. i.e. determine la tuile de pixel a calculer en fonction des parametres passes.<br>
	 * La validite des parametres n'est pas verifiee i.e. valeurs negatives, end &lt; a start, ...
	 * 
	 * @param startX Pixel de depart de la region representee par la tâche en X
	 * @param startY Pixel de depart de la  region representee par la tâche en Y
	 * @param endX Pixel de fin de la region representee par la tâche en X
	 * @param endY Pixel de fin de la region representee par la tâche en Y 
	 */
	public TileTask(int startX, int startY, int endX, int endY)
	{
		this.startX = startX;
		this.startY = startY;
		
		this.endX = endX;
		this.endY = endY;
	}
	
	public int getEndX()
	{
		return this.endX;
	}
	
	public int getEndY()
	{
		return this.endY;
	}
	
	public int getStartX()
	{
		return this.startX;
	}
	
	public int getStartY()
	{
		return this.startY;
	}
}
