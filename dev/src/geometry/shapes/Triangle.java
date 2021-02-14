package geometry.shapes;

import geometry.Point;

public class Triangle implements Shape
{
	Point A, B, C;
	
	public Triangle(Point A, Point B, Point C)
	{
		this.A = A;
		this.B = B;
		this.C = C;
	}
}
