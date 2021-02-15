package geometry;

public class Vector 
{
	private float x;
	private float y;
	private float z;
	
	public Vector(float x, float y, float z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/*
	 * Construit un vecteur à partir de deux points
	 * 
	 * @param a 
	 */
	public Vector(Point a, Point b)
	{
		Point vectorPoint = Point.sub(b, a);
		
		this.x = vectorPoint.getX();
		this.y = vectorPoint.getY();
		this.z = vectorPoint.getZ();
	}
	
	/*
	 * Ajoute deux vecteurs et retourne le vecteur somme
	 * 
	 * @param u Premier terme de l'addition des deux vecteurs
	 * @param v Deuxième terme de l'addition des deux vecteurs
	 * 
	 * @return u + v
	 */
	public static Vector add(Vector u, Vector v)
	{
		return new Vector(v.x + u.x, v.y + u.y, v.z + u.z);
	}
	
	/*
	 * Ajoute un vecteur au vecteur représenté par l'instance appelante
	 * 
	 * @param u Deuxième terme de l'addition des deux vecteurs
	 * 
	 * @return this + v
	 */
	public Vector add(Vector v)
	{
		return new Vector(this.x + v.x, this.y + v.y, this.z + v.z);
	}
	
	/*
	 * @param u Premier vecteur
	 * @param v Deuxième vecteur
	 * 
	 * @return Le produit vectoriel de u et v
	 */
	public static Vector crossProduct(Vector u, Vector v)
	{
		return new Vector(u.y*v.z - u.z*v.y, u.z*v.x - u.x*v.z, u.x*v.y - u.y*v.x);
	}
	
	/*
	 * Calcule le produit scalaire de deux vecteurs
	 * 
	 * @param u Premier vecteur
	 * @param v Deuxième vecteur
	 * 
	 * @return Produit scalaire de u et v
	 */
	public static float dotProduct(Vector u, Vector v)
	{
		return u.x*u.x + u.y*v.y + u.z*v.z;
	}
	
	/*
	 * Calcule la longueur du vecteur
	 * 
	 * @return Longueur du vecteur
	 */
	public double length()
	{
		return Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
	}
	
	/*
	 * Ajoute deux vecteurs et retourne le vecteur somme
	 * 
	 * @param u Premier terme de la soustraction des deux vecteurs
	 * @param v Deuxième terme de la soustraction des deux vecteurs
	 * 
	 * @return u - v
	 */
	public static Vector sub(Vector u, Vector v)
	{
		return new Vector(u.x - v.x, u.y - v.y, u.z - v.z);
	}
}
