package multithreading;

/**
 *	Représente une tâche de calcul pour le rendu de l'image.<br>
 *	Une tâche délimite une région de l'image. Les limites de la région peuvent être obtenues grâce aux getters appropriés.
 */
public class TileTask 
{
	private int startX, startY;
	private int endX, endY;
	
	/**
	 * Crée une tâche de calcul pour un thread. i.e. détermine la tuile de pixel à calculer en fonction des paramètres passés.<br>
	 * La validité des paramètres n'est pas vérifiée i.e. valeurs négatives, end &lt; à start, ...
	 * 
	 * @param startX Pixel de départ de la région représentée par la tâche en X
	 * @param startY Pixel de départ de la  région représentée par la tâche en Y
	 * @param endX Pixel de fin de la région représentée par la tâche en X
	 * @param endY Pixel de fin de la région représentée par la tâche en Y 
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
