package geometry;

/*
 * Classe permettant de représenter un point de l'espace en coordonnées réelles
 */
public class Point 
{
    private double x, y, z;
    
    public Point(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public void setX(double newX)
    {
        this.x = newX;
    }

    public void setY(double newY)
    {
        this.y = newY;
    }

    public void setZ(double newZ)
    {
        this.z = newZ;
    }

    /*
     * Additionne deux points entre eux et retourne le point résultant
     * 
     * @param a Le premier point de coordonnées (a1, a2, a3)
     * @param b Le deuxième point de coordonnées (b1, b2, b3)
     * 
     * @return a + b = (a1 + b1, a2 + b2, a3 + b3)
     */
    public static Point add(Point a, Point b)
    {
    	return new Point(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
    }
    
    public void copyIn(Point pointToCopy)
    {
    	this.x = pointToCopy.getX();
    	this.y = pointToCopy.getY();
    	this.z = pointToCopy.getZ();
    }
    
    /*
     * Calcule et retourne la distance entre deux points
     * 
     * @param p1 Le premier point
     * @param p2 Le deuxième point
     * 
     * @return La distance entre les deux points
     */
    public static double distance(Point p1, Point p2)
    {
    	Point pSub = Point.sub(p2,  p1);
    	return Math.sqrt(pSub.getX()*pSub.getX() + pSub.getY()*pSub.getY() + pSub.getZ()*pSub.getZ());
    }
    
    /*
     * Permet de comparer deux instances de la classe Point entre elles
     * 
     * @param o Un point de coordonnées (x, y, z)
     * 
     * @return Avec le point de l'instance appelante de coordonnées (x1, y1, z1), retourne true si x1 == x && y1 == y && z1 == 1. False sinon
     */
    @Override
    public boolean equals(Object o)
    {
    	if(! (o instanceof Point))
    		return false;
    	else
    	{
    		Point oPoint = (Point)o;
    		if(oPoint.getX() != this.getX() || oPoint.getY() != this.getY() || oPoint.getZ() != this.getZ())
    			return false;
    	}
    	
    	return true;
    }
    
    /*
     * Redéfinition de la méthode hashCode pour qu'elle retourne le hash du point en fonction de ses coordonnées
     * 
     * @return Un hash basé sur les coordonnées du point
     */
    @Override
    public int hashCode() 
    {
    	return java.util.Objects.hash(this.getX(), this.getY(), this.getZ());
    }
    
    /*
     * Crée et retourne un vecteur ayant les même coordonnées que le point passé en paramètre
     * 
     * @param point Un point de coordonnées (x, y, z)
     * 
     * @return Le vecteur de coordonnées (x, y, z)
     */
    public static Vector p2v(Point point)
    {
    	return new Vector(point);
    }
    
    /*
     * Multiplie les coordonnées d'un point par un scalaire et retourne le point résultant
     * 
     * @param scalar Un scalaire
     * @param Un point de coordonnées (a, b , c)
     * 
     * @return Le point de coordonnées (a*scalar, b*scalar, c*scalar)
     */
    public static Point scalarMul(double scalar, Point a)
    {
    	return new Point(a.getX()*scalar, a.getY()*scalar, a.getZ()*scalar);
    }
    
    /*
     * Soustrait deux points coordonnées à coordonnées et retourne le point résultant
     * 
     * @param a Le premier point de coordoonées (a1, a2, a3)
     * @param b Le deuxième point de coordonnées (b1, b2, b3)
     * 
     * @return Le point de coordonnées (a1 - b1, a2 - b2, a3 - b3)
     */
    public static Point sub(Point a, Point b)
    {
    	return new Point(a.x - b.x, a.y - b.y, a.z - b.z);
    }
    
    /*
     * Redéfinition de toString() pour afficher les points sous la forme "(x, y, z)"
     * 
     * @return Retourne une chaîne de caractère de la forme "(x, y, z)" avec x, y et z les coordoonées du point 
     */
    @Override
    public String toString()
    {
    	return String.format("(%.03f, %.03f, %.03f)", this.x, this.y, this.z);
    }
}
