package geometry.shapes;

import materials.Material;
import accelerationStructures.BoundingBox;
import accelerationStructures.BoundingVolume;
import exceptions.InvalidSphereException;
import geometry.Shape;
import geometry.ShapeUtil;
import maths.CoordinateObject;
import maths.Point;
import maths.Ray;
import maths.Vector;

/**
 * Classe representant une sphere decrite par son centre ainsi que son rayon. Represente la "version" mathematique d'une sphere. 
 * Pour une representation polygonale d'une sphere, voir SphereTriangle
 */
public class Sphere extends ShapeUtil implements Shape
{
	private Point center;
	private double radius;
	
	/**
	 * Cree une sphere blanche a partie de son centre, de son rayon et de son materiau
	 * 
	 * @param center Vector3D representant le centre de la sphere
	 * @param radius Rayon de la sphere 
	 * @param material Materiau utilise pour le rendu de la sphere
	 */
	public Sphere(Point center, double radius, Material material)
	{
		if(radius < 0)
			throw new InvalidSphereException("La sphere que vous avez essaye de creer est invalide.");
			
		this.center = center;
		this.radius = radius;
		
		super.material = material;
	}
	
	/**
	 * {@link geometry.Shape#getBoundingBox()}
	 */
	public BoundingBox getBoundingBox()
	{
		return new BoundingBox(Point.translateMul(center, new Vector(-1, -1, -1), radius), Point.translateMul(center, new Vector(1, 1, 1), radius));
	}
	
	/**
	 * {@link geometry.Shape#getBoundingVolume()}
	 */
	@Override
	public BoundingVolume getBoundingVolume() 
	{
		BoundingVolume boundingVolume = new BoundingVolume();

		for(int i = 0; i < BoundingVolume.PLANE_SET_NORMAL_COUNT; i++)
		{
			double dMin = Vector.dotProduct(BoundingVolume.PLANE_SET_NORMALS[i], Point.translateMul(center, BoundingVolume.PLANE_SET_NORMALS[i].getNegated(), radius).toVector());
			double dMax = Vector.dotProduct(BoundingVolume.PLANE_SET_NORMALS[i], Point.translateMul(center, BoundingVolume.PLANE_SET_NORMALS[i], radius).toVector());
			
			boundingVolume.setBounds(dMin, dMax, i);
		}
		boundingVolume.setEnclosedObject(this);
		
		return boundingVolume;
	}
	
	/**
	 * Permet d'obtenir le centre de la sphere
	 * 
	 * @return Instance sur le point representant le centre de la sphere 
	 */
	public Point getCenter()
	{
		return this.center;
	}
	
	/**
	 * Retourne le vecteur normal a la sphere a un point donne
	 * 
	 * @param point Le point definissant la direction du vecteur normal de la sphere
	 * 
	 * @return Un vecteur d'origine le centre de la sphere et de direction le point passe en argument representant la normale de la sphere en un point donne. Ce vecteur est normalise  
	 */
	public Vector getNormal(Point point)
	{
		return Vector.normalizeV(Point.sub(point, center));
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
		Point UVCoords = new Point(0, 0, 0);
		
		Vector toSphereOrigin = new Vector(point, this.center);
		toSphereOrigin.normalize();
		
		/*
		 * Formules de: https://en.wikipedia.org/wiki/UV_mapping
		 */
		UVCoords.setX(0.5 + Math.atan2(toSphereOrigin.getX(), toSphereOrigin.getZ())/(2*Math.PI));
		UVCoords.setY(0.5 - Math.asin(toSphereOrigin.getY())/Math.PI);
		
		return UVCoords;
	}
	
	/**
	 * Permet d'obtenir les coordonnees de texture UV d'une supposee sphere de rayon 1 a partir d'un point de cette sphere
	 * 
	 * @param point Le point de la sphere dont on veut les coordonnees UV. Le point est suppose etre effectivement sur la sphere. Aucune verification n'est faite
	 * 
	 * @return Le point de coordonnees (x, y, z) tel que x = u et y = v. z sera toujours egal a 0 
	 */
	public static Point getUVCoordsUnitSphere(CoordinateObject point)
	{
		Point UVCoords = new Point(0, 0, 0);
		
		/*
		 * Formules de: https://en.wikipedia.org/wiki/UV_mapping
		 */
		UVCoords.setX(0.5 + Math.atan2(point.getX(), point.getZ())/(2*Math.PI));
		UVCoords.setY(0.5 - Math.asin(point.getY())/Math.PI);
		
		return UVCoords;
	}
	
	/**
	 * Calcule de façon analytique l'intersection d'un rayon et d'une sphere
	 * 
	 * @param ray 				Le rayon avec lequel l'intersection avec la sphere doit etre calculee
	 * @param outInterPoint		Si un point d'intersection est trouve, les coordonnees du point d'intersection seront placees dans ce parametre,
	 * 							ecrasant toutes coordonees pre-existantes
	 * @param outNormalAtInter 	La normale au point d'intersection s'il existe. Inchange s'il n'y a pas de point d'intersection avec le rayon passe en argument. Si ce parametre est null, la normale ne sera pas automatiquement calculee
	 * 
	 * @return Retourne le point d'intersection avec la sphere s'il existe (s'il y a deux points d'intersection, ne retourne que le point le plus pres de l'origine du rayon). Retourne null sinon.
	 * intersect modifie le parametre outNormalAtInter pour y stocker la normale au point d'intersection (si outNormalAtInter n'est pas null a l'appel de la methode). 
	 * S'il n'y a pas de point d'intersection, le vecteur reste inchange.
	 */
	public Double intersect(Ray ray, Point outInterPoint, Vector outNormalAtInter)
	{
		Point intersection = null;

		//equation de sphere: (P-C)² - R² = 0 avec P un point sur la sphere, C le centre de la sphere et R le rayon
		//equation parametrique d'un rayon: O + kD avec O l'origine du rayon, D la direction du rayon et k un reel
		//En substituant on obtient (O + kD - C)² - R² = 0 <--> O² + (kD)² + C² + 2OkD + 2OC + 2kDC - R² = 0 <--> k²(D²) + k(2OD + 2DC) + (O² + C² + 2OC - R²) = 0
		//On cherche k
		
		Vector OC = new Vector(ray.getOrigin(), center);
		
		double a = Vector.dotProduct(ray.getDirection(), ray.getDirection());// = D²
		double b = -2 * Vector.dotProduct(ray.getDirection(), OC);// = 2D(O-C)
		double c = Vector.dotProduct(OC, OC) - radius*radius;
		
		double b2 = b*b;
		double ac4 = 4*a*c;
		double discri = b2 - ac4;
		double k1 = 0, k2 = 0;
		
		//Determination de k par la methode du discriminant
		if(discri < 0)
			return null;
		else if(discri == 0)
			 k1 = -b/(2*a);
		else
		{
			k1 = (-b - Math.sqrt(discri))/(2*a);
			k2 = (-b + Math.sqrt(discri))/(2*a);
			
			//Les deux intersections sont derriere la camera, il n'y a donc pas d'intersection valable, on renvoie null
			if(k1 < 0 && k2 < 0)
				return null;
			
			//On ne prend en compte que la premiere intersection avec la sphere donc on cherche quel k est le plus petit
			if(k2 < k1)
			{
				double temp = k1;
				k1 = k2;
				k2 = temp;
			}
			
			if(k1 < 0)//Si le k le plus petit est en fait negatif, on choisit l'autre k
				k1 = k2;
		}
		if(k1 < 0)//Le point d'intersection est derriere la camera
			return null;
		
		//On peut maintenant calculer les coordonnees du point d'intersection avec la sphere a l'aide de k1 qui contient le "bon" k
		intersection = ray.determinePoint(k1);
		if(outNormalAtInter != null)
			outNormalAtInter.copyIn(this.getNormal(intersection));//On defini la normale au point d'intersection
		outInterPoint.copyIn(intersection);
		
		return k1;
	}
	
	/**
	 * Permet de redefinir la position du centre de la sphere
	 * 
	 * @param center Le nouveau centre de la sphere dans la scene
	 */
	public void setCenter(Point center) 
	{
		this.center = center;
	}
	
	/**
	 * Permet de redefinir le rayon de la sphere
	 * 
	 * @param radius Le nouveau rayon de la sphere
	 */
	public void setRadius(double radius) 
	{
		this.radius = radius;
	}
	
	@Override
	public String toString()
	{
		return String.format("[Sphere Shape] Centre: %s | Radius: %3.3f | Material: %s", this.center, this.radius, this.material);
	}
}
