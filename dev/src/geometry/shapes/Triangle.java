package geometry.shapes;

import geometry.Point;
import geometry.ShapeTriangle;
import geometry.Vector;

public class Triangle implements ShapeTriangle
{
	Point A, B, C;
	
	//Vecteur normal du plan formé par les 3 points du triangle
	Vector planeNormal;
	
	public Triangle(Point A, Point B, Point C)
	{
		this.A = A;
		this.B = B;
		this.C = C;
		
		this.planeNormal = Vector.crossProduct(new Vector(A, B), new Vector(A, C));
	}
	
	public Point intersect(Line line)
	{
		
	}
}
