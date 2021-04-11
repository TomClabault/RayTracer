package geometry.shapes;

import geometry.Shape;
import geometry.ShapeTriangleUtil;
import materials.Material;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class Triangle extends ShapeTriangleUtil implements Shape
{
	private Point A, B, C;
	
	private Vector planeNormal;//Vecteur normal du plan formé par les 3 points du triangle
	
	public Triangle(Point A, Point B, Point C, Material material)
	{
		this.A = A;
		this.B = B;
		this.C = C;
		this.planeNormal = Vector.normalizeV(Vector.crossProduct(new Vector(A, B), new Vector(A, C)));
		
		super.material = material;
	}

	public Point getA() {return this.A;}
	public Point getB() {return this.B;}
	public Point getC() {return this.C;}



	public Vector getNormal(Point point)
	{
		return this.planeNormal;
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
		if(Vector.dotProduct(normalLocal, this.planeNormal) < 0)//Le point est sur le côté droit du segment BC, pas à l'intérieur du triangle
			return false;
		
		return true;
	}
	
	/*
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
	public Point intersect(Ray ray, Vector outNormalAtInter)
	{
		Point intersection = null;
		double denom =  -Vector.dotProduct(this.planeNormal, ray.getDirection());
		
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
		
		if(this.insideOutsideTest(intersection))//Si le point d'intersection du rayon et du plan est dans le triangle, on a trouve notre point d'intersection
		{
			if(outNormalAtInter != null)
				outNormalAtInter.copyIn(this.planeNormal);
			
			return intersection;//On le retourne
		}
		else//Cela veut dire que le rayon intersecte le plan formé par le triangle mais pas le triangle lui même
			return null;
	}
		
	/*
	 * Permet de redéfinir le point A du triangle
	 * 
	 * @param A Le nouveau point A du triangle
	 */
	public void setA(Point A)
	{
		this.A = A;
	}
	
	/*
	 * Analogue à @link{geometry.shapes.Triangle#setA}
	 */
	public void setB(Point B)
	{
		this.B = B;
	}
	
	/*
	 * Analogue à @link{geometry.shapes.Triangle#setA}
	 */
	public void setC(Point C)
	{
		this.C = C;
	}
}
