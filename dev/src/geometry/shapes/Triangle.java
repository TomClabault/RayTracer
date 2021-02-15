package geometry.shapes;

import geometry.Point;
import geometry.ShapeTriangle;

public class Triangle implements ShapeTriangle
{
	Point A, B, C;
	
	public Triangle(Point A, Point B, Point C)
	{
		this.A = A;
		this.B = B;
		this.C = C;
	}
	
	public Point intersect(Line line)
	{
		
	}
}
