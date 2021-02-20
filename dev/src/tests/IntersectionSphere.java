package tests;

import geometry.Point;
import geometry.Ray;
import geometry.shapes.SphereMaths;

public class IntersectionSphere 
{
	public static void tryIntersect(SphereMaths sphere, Ray ray, Point expected)
	{
		Point intersection = sphere.intersect(ray);
		
		if(intersection == null)
		{
			if(expected == null)
				System.out.println("Point d'intersection correct");
			else
				System.out.println(String.format("Erreur, le point d'intersection trouvé est: %s.\nAttendu: %s", intersection == null ? "NullInter" : intersection, expected == null ? "NullInter" : expected));
		}
		else if(!intersection.equals(expected))
			System.out.println(String.format("Erreur, le point d'intersection trouvé est: %s.\nAttendu: %s", intersection == null ? "NullInter" : intersection, expected == null ? "NullInter" : expected));
		else
			System.out.println("Point d'intersection correct");
	}

	
	public static void main(String[] args)
	{
		tryIntersect(new SphereMaths(new Point(0, 0, -2), 1), new Ray(new Point(0, 0, 0), new Point(0, 0, -1), true), new Point(0, 0, -1));
		tryIntersect(new SphereMaths(new Point(0, 0, 2), 1), new Ray(new Point(0, 0, 0), new Point(0, 0, -1), true), null);
		tryIntersect(new SphereMaths(new Point(-1, 0, -2), 1), new Ray(new Point(0, 0, 0), new Point(0, 0, -1), true), new Point(0, 0, -2));
		tryIntersect(new SphereMaths(new Point(2, 0, 0), 1), new Ray(new Point(0, 0, 0), new Point(1.4, 0.2, 0.4), true), new Point(1.4, 0.2, 0.4));
	}
}
