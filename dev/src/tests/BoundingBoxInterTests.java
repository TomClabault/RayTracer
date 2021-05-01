package tests;

import geometry.shapes.BoundingBox;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class BoundingBoxInterTests 
{
	public static Point interTest(BoundingBox box, Ray ray)
	{
		final double EPSILON = 0.0000001;
		Point outPoint = new Point(0, 0, 0);
		
		Vector inverseRayDir = new Vector(Math.abs(ray.getDirection().getX()) < EPSILON ? Double.MAX_VALUE : 1 / ray.getDirection().getX(),
										  Math.abs(ray.getDirection().getY()) < EPSILON ? Double.MAX_VALUE : 1 / ray.getDirection().getY(),
										  Math.abs(ray.getDirection().getZ()) < EPSILON ? Double.MAX_VALUE : 1 / ray.getDirection().getZ());
		box.intersect(ray.getOrigin(), inverseRayDir, outPoint);
		
		return outPoint;
	}
	
	public static void main(String[] args) 
	{
		//TODO (tom) finir les tests d'intersection
		BoundingBox boundingBox = new BoundingBox(new Point(-1, -1, -1), new Point(1, 1,- 2));
		
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(0, 0, -1))), new Point(0, 0, -1)));
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(0, 5, -1))), "null"));
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(0, -5, -1))), "null"));
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(0.5, 0, -0.5))), "null"));
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(-0.5, 0, -0.5))), "null"));
	}
}
