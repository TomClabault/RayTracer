package tests;

import accelerationStructures.BoundingBox;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class BoundingBoxInterTests 
{
	public static boolean interTest(BoundingBox box, Ray ray)
	{
		return box.intersect(ray);
	}
	
	public static void main(String[] args) 
	{
		BoundingBox boundingBox = new BoundingBox(new Point(-1, -1, -1), new Point(1, 1, -3));
		
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(0, 0, -1))), true));
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(0, 5, -1))), false));
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(0, -5, -1))), false));
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(0.5, 0, -0.5))), true));
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(-0.5, 0, -0.5))), true));
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(-0.4, 0, -0.5))), true));
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(-0.3, 0, -0.5))), true));
		System.out.println(String.format("Result : %s | Expected : %s", interTest(boundingBox, new Ray(new Point(0, 0, 0), new Vector(-0.3, 0.1, -0.5))), true));
	}
}
