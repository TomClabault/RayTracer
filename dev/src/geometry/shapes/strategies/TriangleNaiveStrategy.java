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
	 * @param point Le point a tester
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
		if(Vector.dotProduct(normalLocal, planeNormal) < 0)//Le point est sur le cote droit du segment AB du triangle, pas dans le triangle donc
			return false;
		
		
		
		Vector sideBC = new Vector(B, C);
		Vector vecBP = new Vector(B, point);
		
		normalLocal = Vector.crossProduct(sideBC, vecBP);
		if(Vector.dotProduct(normalLocal, planeNormal) < 0)//Le point est sur le cote droit du segment BC, pas a l'interieur du triangle
			return false;
		
		
		
		Vector sideCA = new Vector(C, A);
		Vector vecCP = new Vector(C, point);
		
		normalLocal = Vector.crossProduct(sideCA, vecCP);
		if(Vector.dotProduct(normalLocal, planeNormal) < 0)//Le point est sur le cote droit du segment BC, pas a l'interieur du triangle
			return false;
		
		return true;
	}
	
	/**
	 * {@link geometry.shapes.strategies.TriangleIntersectionStrategy#intersect(Triangle, Ray, Point, Vector)}
	 */
	@Override
	public Double intersect(Triangle triangle, Ray ray, Point outInterPoint, Vector outNormalAtInter)
	{
		Point A = triangle.getA();
		Vector planeNormal = triangle.getNormal(null);
		
		Point intersection = null;
		double denom =  -Vector.dotProduct(planeNormal, ray.getDirection());
		
		if(Math.abs(denom) < 0.0000001d)//Si la normale du plan et la direction du rayon sont perpendiculaires, le plan et le rayon sont paralleles, pas d'intersection
			return null;
		
		Point originMinA = Point.sub(ray.getOrigin(), A);
		Vector originMinAVec = new Vector(originMinA.getX(), originMinA.getY(), originMinA.getZ());
		
		double sup = Vector.dotProduct(originMinAVec, planeNormal);
		double coeffVectorPoint = sup/denom;
		
		if(coeffVectorPoint < 0)//L'intersection est dans la direction opposee du rayon, c'est a dire derriere la camera
			return null;
		
		//Calcule les coordonnees du point d'intersection entre le rayon et le plan forme par les 3 points du triangle grâce a l'equation P = ray.origin + coeff.ray.direction
		intersection = ray.determinePoint(coeffVectorPoint);
		
		if(this.insideOutsideTest(triangle, intersection))//Si le point d'intersection du rayon et du plan est dans le triangle, on a trouve notre point d'intersection
		{
			if(outNormalAtInter != null)
				outNormalAtInter.copyIn(planeNormal);
			
			if(outInterPoint != null)
				outInterPoint.copyIn(intersection);
			
			return coeffVectorPoint;//On le retourne
		}
		else//Cela veut dire que le rayon intersecte le plan forme par le triangle mais pas le triangle lui meme
			return null;
	}
}
