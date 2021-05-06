package maths;

/*
 * Interface permettant de representer un objet disposant de 3 coordonnees tel que les points ou les vecteurs par exemple
 */
public interface CoordinateObject 
{
	/**
	 * Constante utilisee pour determiner a quelle niveau de precision de objet sont consideres egaux
	 */
	static final double EPSILON_EQUALS = 0.00000001;
	
	public double getX();
	public double getY();
	public double getZ();
	
	public void setX(double x);
	public void setY(double y);
	public void setZ(double z);
}
