package geometry.shapes;

import materials.Material;
import materials.MatteMaterial;
import geometry.Shape;
import javafx.scene.paint.Color;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class PlaneMaths implements Shape
{
	//Equation de plan: (p - point).normal = 0
	private Vector normal;//partie (A, B, C) de l'équation
	private Point point;//partie D de l'équation
	
	private Material material;
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et d'un point appartenant au plan
	 * 
	 * @param normal Vecteur normal au plan
	 * @param point Point par lequel passe le plan
	 */
	public PlaneMaths(Vector normal, Point point)
	{
		this(normal, point, new MatteMaterial(Color.rgb(128, 128, 128)));
	}
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et d'un point appartenant au plan 
	 * 
	 * @param normal Vecteur normal au plan
	 * @param point Point par lequel passe le plan
	 * @param material Matériau qui sera utilisé pour le rendu du plan
	 */
	public PlaneMaths(Vector normal, Point point, Material material)
	{
		this.normal = normal;
		this.point = point;
		
		this.material = material;
	}
	
	public Material getMaterial() 
	{
		return this.material;
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
			
		
	    Vector p0l0 = Point.p2v(Point.sub(this.point, ray.getOrigin())); 
	    double coeffVectorPoint = Vector.dotProduct(p0l0, this.normal) / NDir;
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
