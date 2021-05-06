package maths;

public class Point implements CoordinateObject
{
	private double x, y, z;

	public Point(double x, double y, double z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/*
     * Additionne deux points entre eux et retourne le point resultant
     * 
     * @param a Le premier point de coordonnees (a1, a2, a3)
     * @param b Le deuxieme point de coordonnees (b1, b2, b3)
     * 
     * @return a + b = (a1 + b1, a2 + b2, a3 + b3)
     */
    public static Point add(Point a, Point b)
    {
    	return new Point(a.getX() + b.getX(), a.getY() + b.getY(), a.getZ() + b.getZ());
    }
    
    /*
     * Permet de copier un point passe en argument dans l'instance du point appelante
     * 
     * @param pointToCopy Point dont les coordonnees vont etre copiees dans l'instance actuelle
     */
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
     * @param p2 Le deuxieme point
     * 
     * @return La distance entre les deux points
     */
    public static double distance(Point p1, Point p2)
    {
    	Point pSub = Point.sub(p2,  p1);
    	return Math.sqrt(pSub.getX()*pSub.getX() + pSub.getY()*pSub.getY() + pSub.getZ()*pSub.getZ());
    }
    
    @Override
    public boolean equals(Object otherPoint)
    {
    	Point otherPointP = (Point)otherPoint;
    	
    	double diffX = otherPointP.getX() - this.getX();
    	if(Math.abs(diffX) > EPSILON_EQUALS)
    		return false;
    	
    	double diffY = otherPointP.getY() - this.getY();
    	if(Math.abs(diffY) > EPSILON_EQUALS)
    		return false;
    	
    	double diffZ = otherPointP.getZ() - this.getZ();
    	if(Math.abs(diffZ) > EPSILON_EQUALS)
    		return false;
    	
    	return true;
    }
	
	@Override
	public double getX() 
	{
		return this.x;
	}

	@Override
	public double getY() 
	{
		return this.y;
	}

	@Override
	public double getZ() 
	{
		return this.z;
	}
	
	/**
	 * Permet de determiner le point representant le milieu du segment forme par les points a et b passes en argument
	 * 
	 *  @param a Le premier point du segment dont on veut determiner le milieu
	 *  @param b Le deuxieme point du segment dont on veut determiner le milieu
	 *  
	 *  @return Le point au milieu du segement [ab] 
	 */
	public static Point midPoint(Point a, Point b)
	{
		return new Point((a.getX() + b.getX())/2, (a.getY() + b.getY())/2, (a.getZ() + b.getZ())/2);
	}
	
	/**
     * Multiplie les coordonnees d'un point par un scalaire et retourne le point resultant
     * 
     * @param scalar 	Un scalaire
     * @param a 		Un point de coordonnees (a, b , c)
     * 
     * @return Le point de coordonnees (a*scalar, b*scalar, c*scalar)
     */
    public static Point scalarMul(double scalar, Point a)
    {
    	return new Point(a.getX()*scalar, a.getY()*scalar, a.getZ()*scalar);
    }
    
    /**
     * {@link maths.Point#scalarMul}
     */
    public static Point scalarMul(Point a, double scalar)
    {
    	return new Point(a.getX()*scalar, a.getY()*scalar, a.getZ()*scalar);
    }
	
	@Override
	public void setX(double x) 
	{
		this.x = x;
	}

	@Override
	public void setY(double y) 
	{
		this.y = y;
	}

	@Override
	public void setZ(double z) 
	{
		this.z = z;
	}
    
    /**
     * Soustrait deux points coordonnees a coordonnees et retourne le point resultant
     * 
     * @param a Le premier point de coordoonees (a1, a2, a3)
     * @param b Le deuxieme point de coordonnees (b1, b2, b3)
     * 
     * @return Le point de coordonnees (a1 - b1, a2 - b2, a3 - b3)
     */
    public static Point sub(Point a, Point b)
    {
    	return new Point(a.x - b.x, a.y - b.y, a.z - b.z);
    }
    
    @Override
    public String toString()
    {
    	String output = String.format("[%.3f, %.3f, %.3f]", this.x, this.y, this.z);
    	
    	return String.format("%-25s", output);
    }
    
    /**
     * @return Pour this, le point de coordonnees (x, y, z), retourne le vecteur de coordonnee (x, y, z)
     */
    public Vector toVector()
    {
    	return new Vector(this.x, this.y, this.z);
    }
    
    /**
     * Translate le point a le long du vecteur u "k fois"
     * 
     *  @param a Un point de coordonnees (x, y, z)
     *  @param u Un vecteur de coordonnees (a, b, c)
     *  @param k Un coefficient de translation
     *  
     *  @return Un nouveau point de coordonnee (x + a*k, y + b*k, z + c*k)
     */
    public static Point translateMul(Point a, Vector u, double k)
    {
    	return new Point(a.getX() + u.getX() * k, a.getY() + u .getY() * k, a.getZ() + u .getZ() * k);
    }
}
