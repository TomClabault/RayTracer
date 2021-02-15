package geometry.shapes;

import geometry.Point;
import geometry.Ray;
import geometry.ShapeTriangle;
import geometry.Vector;

public class Triangle implements ShapeTriangle
{
	Point A, B, C;
	
	Vector planeNormal;//Vecteur normal du plan formé par les 3 points du triangle
	float planeD;//Composante D du plan formé par les 3 points du triangle comme dans l'équation de plan Ax + By + Cz + D = 0
	
	public Triangle(Point A, Point B, Point C)
	{
		this.A = A;
		this.B = B;
		this.C = C;
		
		this.planeNormal = Vector.crossProduct(new Vector(A, B), new Vector(A, C));
		
		this.planeD = Vector.dotProduct(this.planeNormal, new Vector(A.getX(), A.getY(), A.getZ()));
	}
	
	/*
	 * Calcule l'intersection du triangle représenté par cette instance avec un rayon passé en paramètre. Cette méthode ne cherche l'intersection que dans la direction du rayon (c'est à dire pas "derrière" le rayon / derrière la caméra).
	 * 
	 * @param Le rayon avec lequel chercher une intersection
	 * 
	 * @return Le point d'intersection du rayon et du triangle. Null s'il n'y a pas d'intersection
	 */
	public Point intersect(Ray ray)
	{
		float coeff = -(Vector.dotProduct(this.planeNormal, ray.getOrigin()) + this.planeD)/(this.planeNormal, ray.getDirection());
	}
}
