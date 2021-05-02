package accelerationStructures;

import maths.Point;
import maths.Ray;
import maths.Vector;

public class BoundingBox 
{
	private Point[] bounds;
	private final double EPSILON = 0.0000001;
	
	public BoundingBox(Point bound1, Point bound2)
	{
		this.bounds = new Point[] {bound1, bound2};
	}
	
	public void extendBy(BoundingBox extender)
	{
		this.bounds[0].setX(Math.min(this.bounds[0].getX(), extender.getBounds(0).getX()));
		this.bounds[0].setY(Math.min(this.bounds[0].getY(), extender.getBounds(0).getY()));
		this.bounds[0].setZ(Math.min(this.bounds[0].getZ(), extender.getBounds(0).getZ()));
		
		this.bounds[1].setX(Math.min(this.bounds[0].getX(), extender.getBounds(1).getX()));
		this.bounds[1].setZ(Math.min(this.bounds[0].getY(), extender.getBounds(1).getY()));
		this.bounds[1].setY(Math.min(this.bounds[0].getZ(), extender.getBounds(1).getZ()));
	}
	
	/*
	 * Intersect from: https://www.scratchapixel.com/code.php?id=10&origin=/lessons/3d-basic-rendering/ray-tracing-rendering-simple-shapes&src=1
	 */
	public boolean intersect(Ray ray) 
	{ 
		Point rayOrigin = ray.getOrigin();
		Vector rayDirection = ray.getDirection();
		
	    double tmin = Math.abs(rayDirection.getX()) < EPSILON ? Double.MAX_VALUE * Math.signum((bounds[0].getX() - rayOrigin.getX())): (bounds[0].getX() - rayOrigin.getX()) / rayDirection.getX(); 
	    double tmax = Math.abs(rayDirection.getX()) < EPSILON ? Double.MAX_VALUE * Math.signum((bounds[1].getX() - rayOrigin.getX())): (bounds[1].getX() - rayOrigin.getX()) / rayDirection.getX(); 
	 
	    if (tmin > tmax)
    	{
    		double temp = tmin;
    		tmin = tmax;
    		tmax = temp; 
    	}
	 
	    double tymin = Math.abs(rayDirection.getY()) < EPSILON ? Double.MAX_VALUE * Math.signum((bounds[0].getY()) - rayOrigin.getY()): (bounds[0].getY() - rayOrigin.getY()) / rayDirection.getY(); 
	    double tymax = Math.abs(rayDirection.getY()) < EPSILON ? Double.MAX_VALUE * Math.signum((bounds[1].getY() - rayOrigin.getY())): (bounds[1].getY() - rayOrigin.getY()) / rayDirection.getY(); 
	 
	    if (tymin > tymax)
    	{
    		double temp = tymin;
    		tymin = tymax;
    		tymax = temp; 
    	}
	 
	    if ((tmin > tymax) || (tymin > tmax)) 
	        return false; 
	 
	    if (tymin > tmin) 
	        tmin = tymin; 
	 
	    if (tymax < tmax) 
	        tmax = tymax; 
	 
	    double tzmin = Math.abs(rayDirection.getZ()) < EPSILON ? Double.MAX_VALUE * Math.signum((bounds[0].getZ() - rayOrigin.getZ())) : (bounds[0].getZ() - rayOrigin.getZ()) / rayDirection.getZ(); 
	    double tzmax = Math.abs(rayDirection.getZ()) < EPSILON ? Double.MAX_VALUE * Math.signum((bounds[1].getZ() - rayOrigin.getZ())) : (bounds[1].getZ() - rayOrigin.getZ()) / rayDirection.getZ(); 
	 
	    if (tzmin > tzmax)
    	{
    		double temp = tzmin;
    		tzmin = tzmax;
    		tzmax = temp; 
    	}
	    
	    if ((tmin > tzmax) || (tzmin > tmax)) 
	        return false; 
	 
	    if (tzmin > tmin) 
	        tmin = tzmin; 
	 
	    if (tzmax < tmax) 
	        tmax = tzmax; 
	 
	    return true; 
	} 

	/**
	 * Retourne la limite inférieure ou supérieure de la bounding box en fonction de l'index passé en paramètre
	 * 
	 * @param index Entier entre 0 et 1. 
	 * 
	 * @return Retourne la limite inférieure de la bounding box si index == 0, limite supérieure si index == 1
	 */
	public Point getBounds(int index)
	{
		return this.bounds[index];
	}
	
	public void setBound(Point bound, int index)
	{
		this.bounds[index] = bound;
	}
}
