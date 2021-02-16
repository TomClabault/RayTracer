package geometry;

/*
 * Classe permettant de représenter un point de l'espace en coordonnées réelles
 */
public class Point 
{
    private float x,y,z;

    public Point(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX(){
        return this.x;
    }

    public float getY(){
        return this.y;
    }

    public float getZ(){
        return this.z;
    }

    public void setX(float newX){
        this.x = newX;
    }

    public void setY(float newY){
        this.y = newY;
    }

    public void setZ(float newZ){
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
     * Multiplie les coordonnées d'un point par un scalaire et retourne le point résultant
     * 
     * @param scalar Un scalaire
     * @param Un point de coordonnées (a, b , c)
     * 
     * @return Le point de coordonnées (a*scalar, b*scalar, c*scalar)
     */
    public static Point scalarMul(float scalar, Point a)
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
