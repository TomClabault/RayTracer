package tests;

import geometry.shapes.Triangle;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class TestTriangles 
{
	public static void main(String[] args)
	{
		Triangle t1 = new Triangle(new Point(0, 0, -3), new Point(0, 0, -1), new Point(0, -2, 0));
		Ray ray = new Ray(new Point(1, 0, 0), new Vector(-1, -0.619, -1.206));
	
		Point inter = t1.intersect(ray);
	
		System.out.println(inter);
	}
}
