package maths;

/*
 * Interface permettant de représenter un objet disposant de 3 coordonnées tel que les points ou les vecteurs par exemple
 */
public interface CoordinateObject 
{
	public double getX();
	public double getY();
	public double getZ();
	
	public void setX(double x);
	public void setY(double y);
	public void setZ(double z);
}
