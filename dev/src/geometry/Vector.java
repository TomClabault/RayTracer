package geometry;

/*
 * Classe permettant de représenter un vecteur en coordonnées réelles dans l'espace
 */
public class Vector 
{
	private double x, y, z;
	
	/*
	 * Contruit un vecteur à partir de ses trois composantes x, y et z
	 * 
	 * @param x Composante x du vecteur
	 * @param y Composante y du vecteur
	 * @param z Composante z du vecteur
	 */
	public Vector(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/*
	 * Construit un vecteur (x, y, z) avec un point (a, b, c) tel que x = a, y = b, z = c. Il s'agit simplement d'une recopie de coordonnées
	 * 
	 *  @param p Le point à partir du quel créer le vecteur
	 */
	public Vector(Point p)
	{
		this.x = p.getX();
		this.y = p.getY();
		this.z = p.getZ();
	}
	
	/*
	 * Construit le vecteur AB à partir des points a et b passés en paramètre
	 * 
	 * @param a Point A
	 * @param b Point B
	 */
	public Vector(Point originPoint, Point directionPoint)
	{
		Point vectorPoint = Point.sub(directionPoint, originPoint);
		
		this.x = vectorPoint.getX();
		this.y = vectorPoint.getY();
		this.z = vectorPoint.getZ();
	}
	
	/*
	 * Créer un nouveau vecteur à partir d'un existant. i.e. fait une copie
	 */
	public Vector(Vector u)
	{
		this.x = u.x;
		this.y = u.y;
		this.z = u.z;
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
	public static double dotProduct(Vector u, Vector v)
	{
		return u.x*v.x + u.y*v.y + u.z*v.z;
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
	 * Normalise le vecteur
	 */
	public void normalize()
	{
		double length = this.length();
		
		this.x /= length;
		this.y /= length;
		this.z /= length;
	}
	
	public static Vector normalize(Vector toNormalize)
	{
		Vector normalized = new Vector(toNormalize);
		normalized.normalize();
		
		return normalized; 
	}
	
	public static Vector scalarMul(Vector u, double scalar)
	{
		return new Vector(u.getX()*scalar, u.getY()*scalar, u.getZ()*scalar);
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
	
	/*
	 * Retourne la représentation mathématique du vecteur
	 * 
	 * @return Une chaîne de caractère de la forme: (x, y, z) avec x, y et z les coordonnées du vecteur
	 */
	public String toString()
	{
		return String.format("(%.3f, %.3f, %.3f)", this.x, this.y, this.z);
	}
	
	/*
	 * Utilise les coordoonées d'un vecteur pour définir un point
	 * 
	 * @param u Un vecteur de coordonnées (x, y, z)
	 * 
	 * @return Le point de coordonnées (x, y, z)
	 */
	public static Point v2p(Vector u)
	{
		return new Point(u.x, u.y, u.z);
	}

	public double getX() 
	{
		return this.x;
	}
	
	public double getY() 
	{
		return this.y;
	}
	
	public double getZ() 
	{
		return this.z;
	}
}
