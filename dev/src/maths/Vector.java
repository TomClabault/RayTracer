package maths;

/*
 * Classe permettant de représenter un vecteur en coordonnées réelles dans l'espace
 */
public class Vector implements CoordinateObject
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
	 * Vérifie si deux vecteurs sont colinéaires ou non
	 * *
	 * @param u Premier vecteur
	 * @param v Deuxième vecteur
	 *
	 * @return True si les deux vecteurs passés en argument sont coliénaires. False sinon
	 */
	public static boolean areColinear(Vector u, Vector v)
	{
		double xy = u.x*v.y - u.y*v.x;
		double xz = u.x*v.z - u.z*v.x;
		double yz = u.y*v.z - u.z*v.y;

		if(Math.abs(xy + xz) < 0.00001d && Math.abs(xy + yz) < 0.00001d && Math.abs(xz + yz) < 0.0001d)
			return true;
		else
			return false;
	}

	/*
     * Permet de copier un vecteur passé en argument dans l'instance du vecteur appelante
     *
     * @param vectorToCopy Vecteur dont les coordonnées vont être copiées dans l'instance actuelle
     */
    public void copyIn(Vector vectorToCopy)
    {
    	this.x = vectorToCopy.x;
    	this.y = vectorToCopy.y;
    	this.z = vectorToCopy.z;
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

	@Override
	public boolean equals(Object v)
	{
		Vector vVec = (Vector)v;
		
		double diffX = Math.abs(this.x - vVec.getX());
		if(diffX > EPSILON_EQUALS)
			return false;
		
		double diffY = Math.abs(this.y - vVec.getY());
		if(diffY > EPSILON_EQUALS)
			return false;
		
		double diffZ = Math.abs(this.z - vVec.getZ());
		if(diffZ > EPSILON_EQUALS)
			return false;
		
		return true;
	}
	
	/*
	 * Calcule et retourne un nouveau vecteur de direction opposée au vetceur représenté par l'instance appelante
	 * 
	 * @return Si le vecteur de l'instance appelante est de coordonnées (x, y, z), retourne le vecteur de coordonnées (-x, -y , -z) 
	 */
	public Vector getNegated()
	{
		return new Vector(-this.x, -this.y, -this.z);
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
	
	/*
	 * Permet d'interpoler linéairement un vecteur entre deux vecteurs donnés
	 * 
	 * @param u Le premier vecteur qui servira pour l'interpolation
	 * @param v Le deuxième vecteur qui servira pour l'interpolation
	 * @param coeff La coefficient d'interpolation. 1 donnera 'u' comme résultat d'interpolation. 0 donnera v. Un réel entre les deux donnera un vecteur entre u et v  
	 *
	 * @return Retourne le vecteur interpolé des deux vecteurs passé en arguments avec le coefficient donné 
	 */
	public static Vector interpolate(Vector u, Vector v, double coeff)
	{
		return new Vector(Vector.add(Vector.scalarMul(u, coeff), Vector.scalarMul(v, 1 - coeff)));
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

	protected static Point normalize(CoordinateObject object)
	{
		double x, y, z;
		x = object.getX();
		y = object.getY();
		z = object.getZ();
		
		Point normalized = new Point(x, y, z);
		
		double length = Math.sqrt(x*x + y*y + z*z);
		
		normalized.setX(x / length);
		normalized.setY(y / length);
		normalized.setZ(z / length);

		return normalized;
	}
	
	/*
	 * Calcule les coordonnées normalisée du CoordinateObject passé en paramètre et renvoie un point ayant ces coordonnées normalisées
	 * 
	 *  @param toNormalize L'objet dont on veut les coordonnées normalisées
	 *  
	 *  @return Retourne un point de coordonnées (x, y, z) tel que sqrt(x^2 + y^2 + z^2) = 1 
	 */
	public static Point normalizeP(CoordinateObject toNormalize)
	{
		return normalize(toNormalize);
	}
	
	/*
	 * Calcule les coordonnées normalisée du CoordinateObject passé en paramètre et renvoie un vecteur ayant ces coordonnées normalisées
	 * 
	 *  @param toNormalize L'objet dont on veut les coordonnées normalisées
	 *  
	 *  @return Retourne un vecteur de norme 1 à partir des coordonnées de l'objet 'toNormalize'    
	 */
	public static Vector normalizeV(CoordinateObject toNormalize)
	{
		Point normalized = normalize(toNormalize);
		
		return new Vector(normalized.getX(), normalized.getY(), normalized.getZ());
	}

	public void setX(double x)
	{
		this.x = x;
	}
	
	public void setY(double y)
	{
		this.y = y;
	}
	
	public void setZ(double z)
	{
		this.z = z;
	}
	
	/*
	 * Multiple chaque coordonnée d'un vecteur par un scalaire
	 * 
	 * @param u Un vecteur de coordonnées (x, y, z)
	 * @param scalar Le scalaire qui sera utilisé pour la multiplication des coordonnées du vecteur
	 * 
	 * @return Un nouveau vecteur de coordonnées (scalar*x, scalar*y, scalar*z)
	 */
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
	@Override
    public String toString()
    {
    	String output = String.format("[%.3f, %.3f, %.3f]", this.x, this.y, this.z);
    	
    	return String.format("%-25s", output);
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
}
