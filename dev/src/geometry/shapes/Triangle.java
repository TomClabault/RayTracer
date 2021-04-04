package geometry.shapes;

import geometry.Shape;
import materials.Material;
import maths.Vector3D;
import maths.Ray;

public class Triangle implements Shape
{
	Vector3D A, B, C;
	
	Vector3D planeNormal;//Vecteur normal du plan formé par les 3 points du triangle
	
	Material material;
	
	public Triangle(Vector3D A, Vector3D B, Vector3D C, Material material)
	{
		this.A = A;
		this.B = B;
		this.C = C;
		this.planeNormal = Vector3D.normalize(Vector3D.crossProduct(new Vector3D(A, B), new Vector3D(A, C)));
		
		this.material = material;
	}
	
	public Vector3D getNormal(Vector3D point)
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
	public boolean insideOutsideTest(Vector3D point)
	{
		Vector3D sideAB = new Vector3D(this.A, this.B);
		Vector3D vecAP = new Vector3D(this.A, point);
		Vector3D normalLocal;
		
		normalLocal = Vector3D.crossProduct(sideAB, vecAP);
		if(Vector3D.dotProduct(normalLocal, this.planeNormal) < 0)//Le point est sur le côté droit du segment AB du triangle, pas dans le triangle donc
			return false;
		
		
		
		Vector3D sideBC = new Vector3D(this.B, this.C);
		Vector3D vecBP = new Vector3D(this.B, point);
		
		normalLocal = Vector3D.crossProduct(sideBC, vecBP);
		if(Vector3D.dotProduct(normalLocal, this.planeNormal) < 0)//Le point est sur le côté droit du segment BC, pas à l'intérieur du triangle
			return false;
		
		
		
		Vector3D sideCA = new Vector3D(this.C, this.A);
		Vector3D vecCP = new Vector3D(this.C, point);
		
		normalLocal = Vector3D.crossProduct(sideCA, vecCP);
		if(Vector3D.dotProduct(normalLocal, this.planeNormal) < 0)//Le point est sur le côté droit du segment BC, pas à l'intérieur du triangle
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
	public Vector3D intersect(Ray ray, Vector3D outNormalAtInter)
	{
		Vector3D intersection = null;
		double denom =  -Vector3D.dotProduct(this.planeNormal, ray.getDirection());
		
		if(Math.abs(denom) < 0.0000001d)//Si la normale du plan et la direction du rayon sont perpendiculaires, le plan et le rayon sont parallèles, pas d'intersection
			return null;
		
		double sup = Vector3D.dotProduct(Vector3D.sub(ray.getOrigin(), this.A), planeNormal);//(Vector.dotProduct(this.planeNormal, ray.getOriginV()) + planeD);
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

	public Material getMaterial()
	{
		return this.material;
	}
	
	/*
	 * @link{geometry.shapes.Shape#getUVCoords}
	 */
	@Override
	public Vector3D getUVCoords(Vector3D point)
	{
		return null;
	}
}
