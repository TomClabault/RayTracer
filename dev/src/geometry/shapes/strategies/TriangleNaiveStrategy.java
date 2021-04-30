package geometry.shapes.strategies;

import geometry.shapes.Triangle;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class TriangleNaiveStrategy implements TriangleIntersectionStrategy
{
	/**
	 * Test si un point appartient au triangle
	 * 
	 * @param point Le point à tester
	 * 
	 * @return true si le point appartient au triangle. False sinon
	 */
	public boolean insideOutsideTest(Triangle triangle, Point point)
	{
		Point A = triangle.getA();
		Point B = triangle.getB();
		Point C = triangle.getC();
		Vector planeNormal = triangle.getNormal(null);
		
		Vector sideAB = new Vector(A, B);
		Vector vecAP = new Vector(A, point);
		Vector normalLocal;
		
		normalLocal = Vector.crossProduct(sideAB, vecAP);
		if(Vector.dotProduct(normalLocal, planeNormal) < 0)//Le point est sur le côté droit du segment AB du triangle, pas dans le triangle donc
			return false;
		
		
		
		Vector sideBC = new Vector(B, C);
		Vector vecBP = new Vector(B, point);
		
		normalLocal = Vector.crossProduct(sideBC, vecBP);
		if(Vector.dotProduct(normalLocal, planeNormal) < 0)//Le point est sur le côté droit du segment BC, pas à l'intérieur du triangle
			return false;
		
		
		
		Vector sideCA = new Vector(C, A);
		Vector vecCP = new Vector(C, point);
		
		normalLocal = Vector.crossProduct(sideCA, vecCP);
		if(Vector.dotProduct(normalLocal, planeNormal) < 0)//Le point est sur le côté droit du segment BC, pas à l'intérieur du triangle
			return false;
		
		return true;
	}
	
	/**
	 * Calcule l'intersection du triangle représenté par cette instance avec un rayon passé en paramètre. Cette méthode ne cherche l'intersection que dans la direction du rayon (c'est à dire pas "derrière" le rayon / derrière la caméra).
	 * 
	 * @param ray Le rayon avec lequel chercher une intersection
	 * @param outNormalAtInter 	Ce vecteur reçevra la normale du triangle si un point d'intersection avec le rayon est trouvé. 
	 * 							Si aucun point d'intersection n'est trouvé, ce vecteur reste inchangé. 
	 * 							De même, si outNormalAtInter vaut null à l'appel de la méthode, le vecteur eestera inchangé et la normale du triangle ne sera pas tockée.
	 *  
	 * @return Le point d'intersection du rayon et du triangle. Null s'il n'y a pas d'intersection
	 */
	@Override
	public Point intersect(Triangle triangle, Ray ray, Vector outNormalAtInter)
	{
		Point A = triangle.getA();
		Vector planeNormal = triangle.getNormal(null);
		
		Point intersection = null;
		double denom =  -Vector.dotProduct(planeNormal, ray.getDirection());
		
		if(Math.abs(denom) < 0.0000001d)//Si la normale du plan et la direction du rayon sont perpendiculaires, le plan et le rayon sont parallèles, pas d'intersection
			return null;
		
		Point originMinA = Point.sub(ray.getOrigin(), A);
		Vector originMinAVec = new Vector(originMinA.getX(), originMinA.getY(), originMinA.getZ());
		
		double sup = Vector.dotProduct(originMinAVec, planeNormal);
		double coeffVectorPoint = sup/denom;
		
		if(coeffVectorPoint < 0)//L'intersection est dans la direction opposée du rayon, c'est à dire derrière la caméra
			return null;
		
		//Calcule les coordonnées du point d'intersection entre le rayon et le plan formé par les 3 points du triangle grâce à l'équation P = ray.origin + coeff.ray.direction
		intersection = ray.determinePoint(coeffVectorPoint);
		
		if(this.insideOutsideTest(triangle, intersection))//Si le point d'intersection du rayon et du plan est dans le triangle, on a trouve notre point d'intersection
		{
			if(outNormalAtInter != null)
				outNormalAtInter.copyIn(planeNormal);
			
			return intersection;//On le retourne
		}
		else//Cela veut dire que le rayon intersecte le plan formé par le triangle mais pas le triangle lui même
			return null;
	}
}
