package geometry.shapes;

import materials.Material;
import materials.MatteMaterial;
import geometry.Shape;
import javafx.scene.paint.Color;
import maths.Vector3D;
import maths.Ray;

public class PlaneMaths implements Shape
{
	//Equation de plan: (p - point).normal = 0
	private Vector3D normal;//partie (A, B, C) de l'équation
	private Vector3D point;//partie D de l'équation
	
	private Material material;
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et d'un point appartenant au plan
	 * 
	 * @param normal Vecteur normal au plan
	 * @param point Vector3D par lequel passe le plan
	 */
	public PlaneMaths(Vector3D normal, Vector3D point)
	{
		this(normal, point, new MatteMaterial(Color.rgb(128, 128, 128)));
	}
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et d'un point appartenant au plan
	 * 
	 * @param normal 	Vecteur normal au plan
	 * @param distance 	Distance du plan par rapport à l'origine dans la direction du vecteur 'normal'
	 */
	public PlaneMaths(Vector3D normal, double distance)
	{
		this(normal, Vector3D.scalarMul(Vector3D.normalize(normal), distance), new MatteMaterial(Color.rgb(128, 128, 128)));
	}
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et d'un point appartenant au plan
	 * 
	 * @param normal 	Vecteur normal au plan
	 * @param distance 	Distance du plan par rapport à l'origine dans la direction du vecteur 'normal'
	 * @param material Matériau qui sera utilisé pour le rendu du plan
	 */
	public PlaneMaths(Vector3D normal, double distance, Material material)
	{
		this(normal, Vector3D.scalarMul(Vector3D.normalize(normal), distance), material);
	}
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et d'un point appartenant au plan 
	 * 
	 * @param normal Vecteur normal au plan
	 * @param point Vector3D par lequel passe le plan
	 * @param material Matériau qui sera utilisé pour le rendu du plan
	 */
	public PlaneMaths(Vector3D normal, Vector3D point, Material material)
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
	public Vector3D getNormal(Vector3D point) 
	{
		return this.normal;
	}
	
	/*
	 * @link{geometry.shapes.Shape#getUVCoords}
	 */
	@Override
	public Vector3D getUVCoords(Vector3D point)
	{
		return new Vector3D(point.getX(), point.getZ(), 0);
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
	public Vector3D intersect(Ray ray, Vector3D outNormalAtInter) 
	{
		double NDir = Vector3D.dotProduct(normal, ray.getDirection());
		if(NDir > 0.0000001d && NDir < 0.0000001d)//Le rayon et le plan sont parallèles
			return null;
			
		
	    Vector3D p0l0 = Vector3D.sub(this.point, ray.getOrigin()); 
	    double coeffVectorPoint = Vector3D.dotProduct(p0l0, this.normal) / NDir;
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
