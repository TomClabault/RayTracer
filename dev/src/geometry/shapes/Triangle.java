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
	 * Test si un point appartient au triangle
	 * 
	 * @param point Le point à tester
	 * 
	 * @return true si le point appartient au triangle. False sinon
	 */
	public boolean insideOutsideTest(Point point)
	{
		Vector sideAB = new Vector(this.A, this.B);
		Vector vecAP = new Vector(this.A, point);
		Vector normalLocal;
		
		normalLocal = Vector.crossProduct(sideAB, vecAP);
		if(Vector.dotProduct(normalLocal, this.planeNormal) < 0)//Le point est sur le côté droit du segment AB du triangle, pas dans le triangle donc
			return false;
		
		
		
		Vector sideBC = new Vector(this.B, this.C);
		Vector vecBP = new Vector(this.B, point);
		
		normalLocal = Vector.crossProduct(sideBC, vecBP);
		if(Vector.dotProduct(normalLocal, this.planeNormal) < 0)//Le point est sur le côté droit du segment BC, pas à l'intérieur du triangle
			return false;
		
		
		
		Vector sideCA = new Vector(this.C, this.A);
		Vector vecCP = new Vector(this.C, point);
		
		normalLocal = Vector.crossProduct(sideCA, vecCP);
		
		return true;
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
		double planeD;//Composante D du plan formé par les 3 points du triangle dans l'équation de plan Ax + By + Cz + D = 0
		
		if(Vector.dotProduct(this.planeNormal, ray.getDirection()) < 0.0000001d)//Si la normale du plan et la direction du rayon sont perpendiculaires, le plan et le rayon sont parallèles, pas d'intersection
			return null;
		
		planeD = Vector.dotProduct(this.planeNormal, new Vector(this.A.getX(), this.A.getY(), this.A.getZ()));
		
		//Le point d'intersection est sur le rayon. On peut trouver ses coordonnées avec l'équation P = ray.origin + k*ray.direction. Cette coeffVectorPoint = k
		double coeffVectorPoint = -(Vector.dotProduct(this.planeNormal, ray.getOriginV()) + planeD)
								  /Vector.dotProduct(this.planeNormal, ray.getDirection());
		
		if(coeffVectorPoint < 0)//L'intersection est dans la direction opposée du rayon, c'est à dire derrière la caméra
			return null;
		
		//Calcule les coordonnées du point d'intersection entre le rayon et le plan formé par les 3 points du triangle grâce à l'équation P = ray.origin + coeff.ray.direction
		intersection = Point.add(ray.getOrigin(), Point.scalarMul(coeffVectorPoint, ray.getDirectionP()));
		
		if(this.insideOutsideTest(intersection))//Si le point d'intersection du rayon et du plan est dans le triangle, on a trouve notre point d'intersection
			return intersection;//On le retourne
		else//Cela veut dire que le rayon intersecte le plan formé par le triangle mais pas le triangle lui même
			return null;
	}
}
