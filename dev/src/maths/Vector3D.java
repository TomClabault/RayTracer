package maths;

/*
 * Classe permettant de représenter un vecteur en coordonnées réelles dans l'espace
 */
public class Vector3D
{
	private double x, y, z;

	/*
	 * Contruit un vecteur à partir de ses trois composantes x, y et z
	 *
	 * @param x Composante x du vecteur
	 * @param y Composante y du vecteur
	 * @param z Composante z du vecteur
	 */
	public Vector3D(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	/*
	 * Construit le vecteur AB à partir des points a et b passés en paramètre
	 *
	 * @param a Vecteur A dont les coordonnées seront interprétées comme celle d'un point
	 * @param b Vecteur B dont les coordonnées seront interprétées comme celle d'un point
	 */
	public Vector3D(Vector3D originPoint, Vector3D directionPoint)
	{
		Vector3D vectorPoint = Vector3D.sub(directionPoint, originPoint);

		this.x = vectorPoint.getX();
		this.y = vectorPoint.getY();
		this.z = vectorPoint.getZ();
	}

	/*
	 * Créer un nouveau vecteur à partir d'un existant. i.e. fait une copie
	 */
	public Vector3D(Vector3D u)
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
	public static Vector3D add(Vector3D u, Vector3D v)
	{
		return new Vector3D(v.x + u.x, v.y + u.y, v.z + u.z);
	}

	/*
	 * Ajoute un vecteur au vecteur représenté par l'instance appelante
	 *
	 * @param u Deuxième terme de l'addition des deux vecteurs
	 *
	 * @return this + v
	 */
	public Vector3D add(Vector3D v)
	{
		return new Vector3D(this.x + v.x, this.y + v.y, this.z + v.z);
	}

	/*
	 * Vérifie si deux vecteurs sont colinéaires ou non
	 * *
	 * @param u Premier vecteur
	 * @param v Deuxième vecteur
	 *
	 * @return True si les deux vecteurs passés en argument sont coliénaires. False sinon
	 */
	public static boolean areColinear(Vector3D u, Vector3D v)
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
    public void copyIn(Vector3D vectorToCopy)
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
	public static Vector3D crossProduct(Vector3D u, Vector3D v)
	{
		return new Vector3D(u.y*v.z - u.z*v.y, u.z*v.x - u.x*v.z, u.x*v.y - u.y*v.x);
	}

	/*
	 * En interprétant les coordonnées des vecteurs comme celles de deux points, calcule la distance entre ces deux points
	 */
	public static double distance(Vector3D u, Vector3D v)
	{
		Vector3D vMinU = Vector3D.sub(v, u);
		
		return Math.sqrt(vMinU.x*vMinU.x + vMinU.y*vMinU.y + vMinU.z*vMinU.z);
	}
	
	/*
	 * Calcule le produit scalaire de deux vecteurs
	 *
	 * @param u Premier vecteur
	 * @param v Deuxième vecteur
	 *
	 * @return Produit scalaire de u et v
	 */
	public static double dotProduct(Vector3D u, Vector3D v)
	{
		return u.x*v.x + u.y*v.y + u.z*v.z;
	}

	@Override
	public boolean equals(Object object) {
		Vector3D vector = (Vector3D) object;
		if (this.x == vector.x && this.y == vector.y && this.z == vector.z) {
			return true;
		}
		return false;
	}
	
	/*
	 * @return Retourne la coordonnée X du vecteur
	 */
	public double getX()
	{
		return this.x;
	}

	/*
	 * @return Retourne la coordonnée Y du vecteur
	 */
	public double getY()
	{
		return this.y;
	}

	/*
	 * @return Retourne la coordonnée Z du vecteur
	 */
	public double getZ()
	{
		return this.z;
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
	 * Calcule et retourne l'opposé du vecteur de l'instance appelante. Ne modifie pas l'instance appelante
	 *
	 * @return Si le vecteur de l'instance appelante est de coordonnée (x, y, z), retourne un vecteur de coordoonnées (-x, -y, -z)
	 */
	public Vector3D negate()
	{
		return new Vector3D(-this.x, -this.y, -this.z);
	}

	/*
	 * Normalise le vecteur i.e. la longueur du vecteur est 1 après normalisation
	 */
	public void normalize()
	{
		double length = this.length();

		this.x /= length;
		this.y /= length;
		this.z /= length;
	}

	/*
	 * Normalise le vecteur donné en argument
	 * 
	 * @param toNormalize Le vecteur à normaliser
	 * 
	 * @return Retourne le vecteur passé en argument mais normalisé
	 */
	public static Vector3D normalize(Vector3D toNormalize)
	{
		Vector3D normalized = new Vector3D(toNormalize);
		normalized.normalize();

		return normalized;
	}

	/*
	 * Multiplie toutes les coordonnées du vecteur passé en argument par le scalaire passé en arument
	 * 
	 * @param u Le vecteur dont les coordonnées doivent être multipliées
	 * @param scalar Le scalaire par lequel les coordonnées vont être multipliées
	 * 
	 * @return Pour un vecteur u de coordonnées (x, y, z) et un scalaire k, retourne le vecteur v = (k*x, k*y, k*z)
	 */
	public static Vector3D scalarMul(Vector3D u, double scalar)
	{
		return new Vector3D(u.getX()*scalar, u.getY()*scalar, u.getZ()*scalar);
	}
	
	/*
	 * @param x Nouvelle coordonnée x du vecteur
	 */
	public void setX(double x)
	{
		this.x = x;
	}
	
	/*
	 * @param x Nouvelle coordonnée y du vecteur
	 */
	public void setY(double y)
	{
		this.y = y;
	}
	
	/*
	 * @param x Nouvelle coordonnée z du vecteur
	 */
	public void setZ(double z)
	{
		this.z = z;
	}
	
	/*
	 * Ajoute deux vecteurs et retourne le vecteur somme
	 *
	 * @param u Premier terme de la soustraction des deux vecteurs
	 * @param v Deuxième terme de la soustraction des deux vecteurs
	 *
	 * @return u - v
	 */
	public static Vector3D sub(Vector3D u, Vector3D v)
	{
		return new Vector3D(u.x - v.x, u.y - v.y, u.z - v.z);
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
}
