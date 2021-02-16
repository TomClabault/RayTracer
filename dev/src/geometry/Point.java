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

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public double getZ(){
        return this.z;
    }

    public void setX(double newX){
        this.x = newX;
    }

    public void setY(double newY){
        this.y = newY;
    }

    public void setZ(double newZ){
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
}
