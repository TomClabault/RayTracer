package geometry;

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
