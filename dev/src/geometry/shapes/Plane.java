package geometry.shapes;

import materials.Material;
import accelerationStructures.BoundingBox;
import accelerationStructures.BoundingVolume;
import geometry.Shape;
import geometry.ShapeUtil;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class Plane extends ShapeUtil implements Shape
{
	//Equation de plan: (p - point).normal = 0
	private Vector normal;//partie (A, B, C) de l'equation
	private Point point;//partie D de l'equation
	
	/**
	 * Cree un plan a partir d'un vecteur normal au plan et d'un point appartenant au plan
	 * 
	 * @param normal 	Vecteur normal au plan
	 * @param distance 	Distance du plan par rapport a l'origine dans la direction du vecteur 'normal'
	 * @param material Materiau qui sera utilise pour le rendu du plan
	 */
	public Plane(Vector normal, double distance, Material material)
	{
		this(normal, Point.translateMul(new Point(0, 0, 0), Vector.normalizeV(normal), distance), material);
	}
	
	/**
	 * Cree un plan a partir d'un vecteur normal au plan et d'un point appartenant au plan 
	 * 
	 * @param normal Vecteur normal au plan
	 * @param point Vector3D par lequel passe le plan
	 * @param material Materiau qui sera utilise pour le rendu du plan
	 */
	public Plane(Vector normal, Point point, Material material)
	{
		this.normal = normal;
		this.point = point;
		
		super.material = material;
	}
	
	/**
	 * {@link geometry.Shape#getBoundingBox()}
	 */
	@Override
	public BoundingBox getBoundingBox()
	{
		return null;//Un plan est infini, pas de bounding box
	}
	
	/**
	 * {@link geometry.Shape#getBoundingVolume()}
	 */
	@Override
	public BoundingVolume getBoundingVolume() 
	{
		return null;//Un plan n'a pas de bounding volume. Un plan est infini
	}
	
	/**
	 * Permet d'obtenir la normal du plan
	 * 
	 * @param point Parametre ignore
	 * 
	 * @return Normale du plan
	 */
	public Vector getNormal(Point point) 
	{
		return this.normal;
	}
	
	/**
	 * {@link geometry.Shape#getSubObjectCount()}
	 */
	@Override
	public int getSubObjectCount() 
	{
		return 1;
	}
	
	/**
	 * {@link geometry.Shape#getUVCoords}
	 */
	@Override
	public Point getUVCoords(Point point)
	{
		return new Point(point.getX(), point.getZ(), 0);
	}
	
	/**
	 * Calcule de façon analytique l'intersection d'un rayon et d'une sphere
	 * 
	 * @param ray Le rayon avec lequel l'intersection avec la sphere doit etre calculee
	 * @param outInterPoint		Si un point d'intersection est trouve, les coordonnees du point d'intersection seront placees dans ce parametre,
	 * 							ecrasant toutes coordonees pre-existantes
	 * @param outNormalAtInter 	Ce vecteur contiendra la normale du plan si un point d'intersection a ete trouve. Ce parametre est inchange sinon. 
	 * 							Si ce parametre est null, la normale au point d'intersection ne sera pas stockee dans outNormalAtInter, meme si un point d'intersection a ete trouve
	 * 
	 * @return Retourne le point d'intersection avec la sphere s'il existe (s'il y a deux points d'intersection, ne retourne que le point le plus pres de l'origine du rayon). Retourne null sinon.
	 */
	public Double intersect(Ray ray, Point outInterPoint, Vector outNormalAtInter) 
	{
		double NDir = Vector.dotProduct(normal, ray.getDirection());
		if(NDir > 0.0000001d && NDir < 0.0000001d)//Le rayon et le plan sont paralleles
			return null;
			
		
		Point p0l0 = Point.sub(this.point, ray.getOrigin());
		Vector p0l0Vec = new Vector(p0l0.getX(), p0l0.getY(), p0l0.getZ());
	    double coeffVectorPoint = Vector.dotProduct(p0l0Vec, this.normal) / NDir;
	    if(coeffVectorPoint > 0)
	    {
	    	if(outNormalAtInter != null)
	    		outNormalAtInter.copyIn(this.getNormal(null));
	    	
	    	outInterPoint.copyIn(ray.determinePoint(coeffVectorPoint));
			return coeffVectorPoint;
	    }
	    else
			return null;
	}
	
	@Override
	public String toString()
	{
		return String.format("[Plane Shape] Normal: %s | Point: %s | Material: %s", this.normal, this.point, this.material);
	}
}
