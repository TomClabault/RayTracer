package geometry.shapes;

import materials.Material;
import geometry.Shape;
import geometry.ShapeUtil;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class PlaneMaths extends ShapeUtil implements Shape
{
	//Equation de plan: (p - point).normal = 0
	private Vector normal;//partie (A, B, C) de l'équation
	private Point point;//partie D de l'équation
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et d'un point appartenant au plan
	 * 
	 * @param normal 	Vecteur normal au plan
	 * @param distance 	Distance du plan par rapport à l'origine dans la direction du vecteur 'normal'
	 * @param material Matériau qui sera utilisé pour le rendu du plan
	 */
	public PlaneMaths(Vector normal, double distance, Material material)
	{
		this(normal, Point.translateMul(new Point(0, 0, 0), Vector.normalizeV(normal), distance), material);
	}
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et d'un point appartenant au plan 
	 * 
	 * @param normal Vecteur normal au plan
	 * @param point Vector3D par lequel passe le plan
	 * @param material Matériau qui sera utilisé pour le rendu du plan
	 */
	public PlaneMaths(Vector normal, Point point, Material material)
	{
		this.normal = normal;
		this.point = point;
		
		super.material = material;
	}
	
	/*
	 * Permet d'obtenir la normal du plan
	 * 
	 * @param point Paramètre ignoré
	 * 
	 * @return Normale du plan
	 */
	public Vector getNormal(Point point) 
	{
		return this.normal;
	}
	
	/*
	 * @link{geometry.shapes.Shape#getUVCoords}
	 */
	@Override
	public Point getUVCoords(Point point)
	{
		return new Point(point.getX(), point.getZ(), 0);
	}
	
	/*
	 * Calcule de façon analytique l'intersection d'un rayon et d'une sphère
	 * 
	 * @param ray Le rayon avec lequel l'intersection avec la sphère doit être calculée
	 * @param outNormalAtInter 	Ce vecteur contiendra la normale du plan si un point d'intersection a été trouvé. Ce paramètre est inchangé sinon. 
	 * 							Si ce paramètre est null, la normale au point d'intersection ne sera pas stockée dans outNormalAtInter, même si un point d'intersection a été trouvé
	 * 
	 * @return Retourne le point d'intersection avec la sphère s'il existe (s'il y a deux points d'intersection, ne retourne que le point le plus près de l'origine du rayon). Retourne null sinon.
	 */
	public Point intersect(Ray ray, Vector outNormalAtInter) 
	{
		double NDir = Vector.dotProduct(normal, ray.getDirection());
		if(NDir > 0.0000001d && NDir < 0.0000001d)//Le rayon et le plan sont parallèles
			return null;
			
		
		Point p0l0 = Point.sub(this.point, ray.getOrigin());
		Vector p0l0Vec = new Vector(p0l0.getX(), p0l0.getY(), p0l0.getZ());
	    double coeffVectorPoint = Vector.dotProduct(p0l0Vec, this.normal) / NDir;
	    if(coeffVectorPoint > 0)
	    {
	    	if(outNormalAtInter != null)
	    		outNormalAtInter.copyIn(this.getNormal(null));
	    	
			return ray.determinePoint(coeffVectorPoint);
	    }
	    else
			return null;
	}
}
