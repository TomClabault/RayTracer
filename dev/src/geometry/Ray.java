package geometry;

public class Ray
{
	private Vector direction;
	
	private Point origin;
	
	public Ray(Vector direction, Point origin)
	{
		this.direction = direction;
		this.origin = origin;
	}
	
	/*
	 * Permet d'obtenir la direction du rayon
	 * 
	 * @return Vecteur représentant la direction du rayon
	 */
	public Vector getDirection()
	{
		return this.direction;
	}
	
	/*
	 * Permet d'obtenir l'origine du rayon
	 * 
	 * @return Un point représentant l'origin du rayon
	 */
	public Point getOrigin()
	{
		return this.origin;
	}
}
