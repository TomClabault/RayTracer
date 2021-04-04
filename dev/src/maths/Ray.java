package maths;

public class Ray
{
	private Vector3D direction;
	private Vector3D origin;
	
	/*
	 * Construit un rayon à partir de son point d'origine ainsi que d'un vecteur indiquant sa direction
	 * 
	 * @param origin Origine du rayon
	 * @param direction Vecteur directeur du rayon
	 */
	public Ray(Vector3D origin, Vector3D direction)
	{
		this.origin = origin;
		this.direction = direction;
	}
	
	public Vector3D determinePoint(double coefficient)
	{
		return Vector3D.add(Vector3D.scalarMul(this.direction, coefficient), this.getOrigin());
	}
	
	/*
	 * Permet d'obtenir la direction du rayon	
	 * 
	 * @return Vecteur représentant la direction du rayon
	 */
	public Vector3D getDirection()
	{
		return this.direction;
	}
	
	/*
	 * Permet d'obtenir l'origine du rayon
	 * 
	 * @return Un point représentant l'origin du rayon
	 */
	public Vector3D getOrigin()
	{
		return this.origin;
	}
	
	/*
	 * Retourne l'origine du rayon sous la forme d'un vecteur
	 * 
	 * @return Vecteur de même coordonnées que le point d'origine du rayon
	 */
	public Vector3D getOriginV()
	{
		return new Vector3D(this.origin);
	}
	
	/*
	 * Retourne l'opposé du vecteur de direction du rayon. Ne modifie pas l'instance appelante 
	 * 
	 * @return Si d = (x, y, z) le vecteur de direction du rayon, retourne v = (-x, -y, -z)
	 */
	public Vector3D negate()
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
