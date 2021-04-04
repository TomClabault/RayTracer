package tests;

import materials.MetallicMaterial;
import geometry.shapes.Triangle;
import javafx.scene.paint.Color;
import maths.Vector3D;
import maths.Ray;

public class TestTriangles 
{
	public static void testInter(Triangle triangle, Ray ray, Vector3D interPointExpected)
	{
		Vector3D inter = triangle.intersect(ray, null);
		System.out.println(inter + " ; Expected = " + interPointExpected);	
	}
	
	public static void main(String[] args)
	{
		testInter(new Triangle(new Vector3D(-1, 0, 2), new Vector3D(0, 1, 2), new Vector3D(1, 0, 2), new MetallicMaterial(Color.rgb(0, 0, 0))), new Ray(new Vector3D(0, 0, 0), new Vector3D(0, 0.8, 2)), new Vector3D(0, 0.8, 2.0));
		testInter(new Triangle(new Vector3D(-1, 0, -2), new Vector3D(1, 0, -2), new Vector3D(0, 1, -2),new MetallicMaterial(Color.rgb(0, 0, 0))), new Ray(new Vector3D(0, 0, 0), new Vector3D(0, 0.4, -1)), new Vector3D(0.000, 0.800, -2.000));
		testInter(new Triangle(new Vector3D(-1, -1, -6), new Vector3D(1, -1, -6), new Vector3D(0, 0, -6),new MetallicMaterial(Color.rgb(0, 0, 0))), new Ray(new Vector3D(0, 0, -4.5), new Vector3D(0.002, -0.499, -0.867)), new Vector3D(0.003, -0.863, -6.000));
	}
}
