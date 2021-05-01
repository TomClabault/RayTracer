package geometry.shapes;

import maths.Point;
import maths.Ray;
import maths.Vector;

public class BoundingBox 
{
	private Point[] bounds;
	
	public BoundingBox()
	{
		this.bounds = new Point[2];
	}
	
	public BoundingBox(Point bound1, Point bound2)
	{
		this.bounds = new Point[] {bound1, bound2};
	}
	
	/*
	 * Intersect from: https://www.scratchapixel.com/code.php?id=10&origin=/lessons/3d-basic-rendering/ray-tracing-rendering-simple-shapes&src=1
	 */
	public boolean intersect(Point rayOrigin, Vector invRayDirection, Point outInterPoint) 
    { 
        double tmin, tmax, tymin, tymax, tzmin, tzmax; 
        double t;
        
        int[] sign = new int[] {invRayDirection.getX() < 0 ? 1 : 0, invRayDirection.getY() < 0 ? 1 : 0, invRayDirection.getZ() < 0 ? 1 : 0};
        
        tmin = (bounds[sign[0]].getX() - rayOrigin.getX()) * invRayDirection.getX(); 
        tmax = (bounds[1-sign[0]].getX() - rayOrigin.getX()) * invRayDirection.getX(); 
        tymin = (bounds[sign[1]].getY() - rayOrigin.getY()) * invRayDirection.getY(); 
        tymax = (bounds[1-sign[1]].getY() - rayOrigin.getY()) * invRayDirection.getY(); 
 
        if ((tmin > tymax) || (tymin > tmax)) 
            return false; 
 
        if (tymin > tmin) 
        tmin = tymin; 
        if (tymax < tmax) 
        tmax = tymax; 
 
        tzmin = (bounds[sign[2]].getZ() - rayOrigin.getZ()) * invRayDirection.getZ(); 
        tzmax = (bounds[1-sign[2]].getZ() - rayOrigin.getZ()) * invRayDirection.getZ(); 
 
        if ((tmin > tzmax) || (tzmin > tmax)) 
            return false; 
 
        if (tzmin > tmin) 
        tmin = tzmin; 
        if (tzmax < tmax) 
        tmax = tzmax; 
 
        t = tmin; 
 
        if (t < 0) { 
            t = tmax; 
            if (t < 0) return false; 
        } 
 
        if(outInterPoint != null)
        	outInterPoint.copyIn(new Ray(rayOrigin, new Vector(invRayDirection.getX() == Double.MAX_VALUE ? 0 : 1/invRayDirection.getX(),
        													   invRayDirection.getY() == Double.MAX_VALUE ? 0 : 1/invRayDirection.getY(),
        													   invRayDirection.getZ() == Double.MAX_VALUE ? 0 : 1/invRayDirection.getZ())).determinePoint(t));
        return true; 
    } 
	
	public void setBound(Point bound, int index)
	{
		this.bounds[index] = bound;
	}
}
