package maths;

/*
 * Interface permettant de représenter un objet disposant de 3 coordonnées tel que les points ou les vecteurs par exemple
 */
public interface CoordinateObject 
{
	/**
	 * Constante utilisée pour déterminer à quelle niveau de précision de objet sont considérés égaux
	 */
	static final double EPSILON_EQUALS = 0.00000001;
	
	public double getX();
	public double getY();
	public double getZ();
	
	public void setX(double x);
	public void setY(double y);
	public void setZ(double z);
}
