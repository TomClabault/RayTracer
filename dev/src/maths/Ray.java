package maths;

public class Ray
{
	private Vector direction;
	private Point origin;
	
	/**
	 * Construit un rayon a partir de son point d'origine ainsi que d'un vecteur indiquant sa direction
	 * 
	 * @param origin Origine du rayon
	 * @param direction Vecteur directeur du rayon
	 */
	public Ray(Point origin, Vector direction)
	{
		this.origin = origin;
		this.direction = direction;
	}
	
	public Point determinePoint(double coefficient)
	{
		return Point.translateMul(this.origin, this.direction, coefficient);
	}
	
	/**
	 * Permet d'obtenir la direction du rayon	
	 * 
	 * @return Vecteur representant la direction du rayon
	 */
	public Vector getDirection()
	{
		return this.direction;
	}
	
	/**
	 * Permet d'obtenir l'origine du rayon
	 * 
	 * @return Un point representant l'origin du rayon
	 */
	public Point getOrigin()
	{
		return this.origin;
	}
	
	/**
	 * Retourne l'oppose du vecteur de direction du rayon. Ne modifie pas l'instance appelante 
	 * 
	 * @return Si d = (x, y, z) le vecteur de direction du rayon, retourne v = (-x, -y, -z)
	 */
	public Vector getNegatedDirection()
	{
		return this.direction.getNegated();
	}
	
	/**
	 * Normalise la direction du rayon
	 */
	public void normalize()
	{
		this.direction.normalize();
	}
	
	/**
	 * Retourne une chaîne de caractere representant le rayon.
	 * 
	 * @return Une chaîne de caractere de la forme:
	 * 
	 * Origine: (x, y, z)
	 * Direciton: (x, y, z)
	 */
	public String toString()
	{
		return String.format("Origine: %s\nDirection: %s\n\n", this.getOrigin().toString(), this.getDirection().toString());
	}
}
