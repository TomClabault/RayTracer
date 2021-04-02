package multithreading;

public class TileTask 
{
	private int startX, startY;
	private int endX, endY;
	
	/*
	 * Crée une tâche de calcul pour un thread. i.e. détermine la tuile de pixel à calculer en fonction des paramètres passés
	 * 
	 * @param startX Pixel de départ de la tâche en X
	 * @param startY Pixel de départ de la tâche en Y
	 * @param endX Pixel de fin de la tâche en X
	 * @param endY Pixel de fin de la tâche en Y 
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
