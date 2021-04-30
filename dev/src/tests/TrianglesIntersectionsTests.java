package tests;

import materials.MetallicMaterial;
import geometry.shapes.Triangle;
import javafx.scene.paint.Color;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class TrianglesIntersectionsTests 
{
	public static void testInter(Triangle triangle, Ray ray, Point interPointExpected)
	{
		Point inter = new Point(0, 0, 0);
		
		triangle.intersect(ray, inter, null);
		System.out.println(inter + " ; Expected = " + interPointExpected);	
	}
	
	public static void main(String[] args)
	{
		testInter(new Triangle(new Point(-1, 0, 2), new Point(0, 1, 2), new Point(1, 0, 2), new MetallicMaterial(Color.rgb(0, 0, 0))), new Ray(new Point(0, 0, 0), new Vector(0, 0.8, 2)), new Point(0, 0.8, 2.0));
		testInter(new Triangle(new Point(-1, 0, -2), new Point(1, 0, -2), new Point(0, 1, -2),new MetallicMaterial(Color.rgb(0, 0, 0))), new Ray(new Point(0, 0, 0), new Vector(0, 0.4, -1)), new Point(0.000, 0.800, -2.000));
		testInter(new Triangle(new Point(-1, -1, -6), new Point(1, -1, -6), new Point(0, 0, -6),new MetallicMaterial(Color.rgb(0, 0, 0))), new Ray(new Point(0, 0, -4.5), new Vector(0.002, -0.499, -0.867)), new Point(0.003, -0.863, -6.000));
	}
}
