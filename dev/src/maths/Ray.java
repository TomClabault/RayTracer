package maths;

public class Ray
{
	private Vector direction;
	
	private Point origin;
	
	public Ray(Point origin, Vector direction)
	{
		this.origin = origin;
		this.direction = new Vector(direction);
	}
	
	public Ray(Point origin, Point direction)
	{
		this.origin = origin;
		this.direction = new Vector(origin, direction);
	}
	
	public Point determinePoint(double coefficient)
	{
		return Point.add(Point.scalarMul(coefficient, this.getDirectionP()), this.getOrigin());
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
	
	/*
	 * Retourne l'opposé du vecteur de direction du rayon. Ne modifie pas l'instance appelante 
	 * 
	 * @return Si d = (x, y, z) le vecteur de direction du rayon, retourne v = (-x, -y, -z)
	 */
	public Vector negate()
	{
		return this.direction.negate();
	}
	
	/*
	 * Normalise la direction du rayon
	 */
	public void normalize()
	{
		this.direction.normalize();
	}
	
	/*
	 * Retourne une chaîne de caractère représentant le rayon.
	 * 
	 * @return Une chaîne de caractère de la forme:
	 * 
	 * Origine: (x, y, z)
	 * Direciton: (x, y, z)
	 */
	public String toString()
	{
		return String.format("Origine: %s\nDirection: %s\n\n", this.getOrigin().toString(), this.getDirection().toString());
	}
}
