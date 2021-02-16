package geometry.shapes;

import geometry.Point;
import geometry.Ray;
import geometry.ShapeTriangle;
import geometry.Vector;

public class Triangle implements ShapeTriangle
{
	Point A, B, C;
	
	Vector planeNormal;//Vecteur normal du plan formé par les 3 points du triangle
	
	public Triangle(Point A, Point B, Point C)
	{
		this.A = A;
		this.B = B;
		this.C = C;
		
		this.planeNormal = Vector.crossProduct(new Vector(A, B), new Vector(A, C));
	}
	
	/*
	 * Calcule l'intersection du triangle représenté par cette instance avec un rayon passé en paramètre. Cette méthode ne cherche l'intersection que dans la direction du rayon (c'est à dire pas "derrière" le rayon / derrière la caméra).
	 * 
	 * @param ray Le rayon avec lequel chercher une intersection
	 * 
	 * @return Le point d'intersection du rayon et du triangle. Null s'il n'y a pas d'intersection
	 */
	public Point intersect(Ray ray)
	{
		Point intersection = null;
		float planeD;//Composante D du plan formé par les 3 points du triangle dans l'équation de plan Ax + By + Cz + D = 0
		
		if(Vector.dotProduct(this.planeNormal, ray.getDirection()) < 0.0000001d)//Si la normale du plan et la direction du rayon sont perpendiculaire, le plan et le rayon sont parallèles, pas d'intersection
			return null;
		
		planeD = Vector.dotProduct(this.planeNormal, new Vector(this.A, this.B));
		
		//Le point d'intersection est sur le rayon. On peut trouver ses coordonnées avec l'équation P = ray.origin + k*ray.direction. Cette variable calcule k
		float coeffVectorPoint = -(Vector.dotProduct(this.planeNormal, Vector.pointToV(ray.getOrigin())) + planeD)
								  /Vector.dotProduct(this.planeNormal, ray.getDirection());
		
		if(coeffVectorPoint < 0)//L'intersection est dans la direction opposée du rayon, c'est à dire derrière la caméra
			return null;
		
		return intersection;
	}
}
