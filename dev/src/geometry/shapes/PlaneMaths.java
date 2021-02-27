package geometry.shapes;

import geometry.ShapeMaths;
import geometry.materials.Material;
import geometry.materials.MatteMaterial;
import javafx.scene.paint.Color;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class PlaneMaths implements ShapeMaths
{
	//Equation de plan: (p - point).normal = 0
	private Vector normal;//partie (A, B, C) de l'équation
	private Point point;//partie D de l'équation
	
	private Material material;
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et d'un point appartenant au plan
	 * 
	 * @param normal Vecteur normal au plan
	 * @param point Point par lequel passe le vecteur
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
	
	@Override
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
	@Override
	public Vector getNormal(Point point) 
	{
		return this.normal;
	}
	
	/*
	 * Calcule de façon analytique l'intersection d'un rayon et d'une sphère
	 * 
	 * @param ray Le rayon avec lequel l'intersection avec la sphère doit être calculée
	 * 
	 * @return Retourne le point d'intersection avec la sphère s'il existe (s'il y a deux points d'intersection, ne retourne que le point le plus près de l'origine du rayon). Retourne null sinon.
	 */
	@Override
	public Point intersect(Ray ray) 
	{
		double NL = Vector.dotProduct(normal, ray.getDirection());
		if(NL > 0.0000001d && NL < 0.0000001d)//Le rayon et le plan sont parallèles
			return null;
			
		
	    Vector p0l0 = Point.p2v(Point.sub(this.point, ray.getOrigin())); 
	    double coeffVectorPoint = Vector.dotProduct(p0l0, this.normal) / NL;
	    if(coeffVectorPoint > 0)
			return ray.determinePoint(coeffVectorPoint);
	    else
			return null;
	}
}
