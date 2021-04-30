package geometry.shapes.strategies;

import geometry.shapes.Triangle;
import maths.Point;
import maths.Ray;
import maths.Vector;

public interface TriangleIntersectionStrategy 
{
	public Point intersect(Triangle triangle, Ray ray, Vector outNormalAtInter);
}
