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
	
	public Point determinePoint(double coefficient)
	{
		return Point.add(this.getOrigin(), Point.scalarMul(coefficient, this.getDirectionP()));
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
	 * Retourne la direction du rayon sous la forme d'un point
	 * 
	 * @return Pour un rayon de direction v(x, y, z), retourne le point p(x, y, z)
	 */
	public Point getDirectionP()
	{
		return Vector.v2p(this.direction);
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
	
	/*
	 * Retourne l'origine du rayon sous la forme d'un vecteur
	 * 
	 * @return Vecteur de même coordonnées que le point d'origine du rayon
	 */
	public Vector getOriginV()
	{
		return new Vector(this.origin);
	}
}
