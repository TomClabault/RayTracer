package geometry.shapes;

import materials.Material;
import materials.MatteMaterial;
import geometry.Shape;
import javafx.scene.paint.Color;
import maths.Vector3D;
import maths.Ray;

/*
 * Classe représentant une sphère décrite par son centre ainsi que son rayon. Représente la "version" mathématique d'une sphère. 
 * Pour une représentation polygonale d'une sphère, voir SphereTriangle
 */
public class SphereMaths implements Shape
{
	Vector3D center;
	double radius;

	Material material;
	
	/*
	 * Crée une sphère blanche à partie de son centre et de son rayon
	 * 
	 * @param center Vector3D représentant le centre de la sphère
	 * @param radius Rayon de la sphère 
	 */
	public SphereMaths(Vector3D center, double radius)
	{
		this(center, radius, new MatteMaterial(Color.rgb(255, 255, 255)));
	}
	
	/*
	 * Crée une sphère blanche à partie de son centre, de son rayon et de son matériau
	 * 
	 * @param center Vector3D représentant le centre de la sphère
	 * @param radius Rayon de la sphère 
	 * @param material Matériau utilisé pour le rendu de la sphère
	 */
	public SphereMaths(Vector3D center, double radius, Material material)
	{
		this.center = center;
		this.radius = radius;
		
		this.material = material;
	}
	
	/*
	 * Permet d'obtenir le centre de la sphère
	 * 
	 * @return Instance sur le point représentant le centre de la sphère 
	 */
	public Vector3D getCenter()
	{
		return this.center;
	}
	
	/*
	 * Permet d'obtenir le matériau de la sphère
	 * 
	 * @return Matériau de la sphère
	 */
	public Material getMaterial() 
	{
		return this.material;
	}
	
	/*
	 * Retourne le vecteur normal à la sphère à un point donné
	 * 
	 * @param point Le point définissant la direction du vecteur normal de la sphère
	 * 
	 * @return Un vecteur d'origine le centre de la sphère et de direction le point passé en argument représentant la normale de la sphère en un point donné. Ce vecteur est normalisé  
	 */
	public Vector3D getNormal(Vector3D point)
	{
		return Vector3D.normalize(Vector3D.sub(point, center));
	}
	
	/*
	 * @link{geometry.shapes.Shape#getUVCoords}
	 */
	@Override
	public Vector3D getUVCoords(Vector3D point)
	{
		Vector3D UVCoords = new Vector3D(0, 0, 0);
		
		Vector3D toSphereOrigin = new Vector3D(point, this.center);
		toSphereOrigin.normalize();
		
		/*
		 * Formules de: https://en.wikipedia.org/wiki/UV_mapping
		 */
		UVCoords.setX(0.5 + Math.atan2(toSphereOrigin.getX(), toSphereOrigin.getZ())/(2*Math.PI));
		UVCoords.setY(0.5 - Math.asin(toSphereOrigin.getY())/Math.PI);
		
		return UVCoords;
	}
	
	/*
	 * Permet d'obtenir les coordonnées de texture UV d'une supposée sphère de rayon 1 à partir d'un point de cette sphère
	 * 
	 * @param point Le point de la sphère dont on veut les coordonnées UV. Le point est supposé être effectivement sur la sphère. Aucune vérification n'est faite
	 * 
	 * @return Le point de coordonnées (x, y, z) tel que x = u et y = v. z sera toujours égal à 0 
	 */
	public static Vector3D getUVCoord(Vector3D point)
	{
		Vector3D UVCoords = new Vector3D(0, 0, 0);
		
		/*
		 * Formules de: https://en.wikipedia.org/wiki/UV_mapping
		 */
		UVCoords.setX(0.5 + Math.atan2(point.getX(), point.getZ())/(2*Math.PI));
		UVCoords.setY(0.5 - Math.asin(point.getY())/Math.PI);
		
		return UVCoords;
	}
	
	/*
	 * Calcule de façon analytique l'intersection d'un rayon et d'une sphère
	 * 
	 * @param ray 				Le rayon avec lequel l'intersection avec la sphère doit être calculée
	 * @param outNormalAtInter 	La normale au point d'intersection s'il existe. Inchangé s'il n'y a pas de point d'intersection avec le rayon passé en argument. Si ce paramètre est null, la normale ne sera pas automatiquement calculée
	 * 
	 * @return Retourne le point d'intersection avec la sphère s'il existe (s'il y a deux points d'intersection, ne retourne que le point le plus près de l'origine du rayon). Retourne null sinon.
	 * intersect modifie le paramètre outNormalAtInter pour y stocker la normale au point d'intersection (si outNormalAtInter n'est pas null à l'appel de la méthode). 
	 * S'il n'y a pas de point d'intersection, le vecteur reste inchangé.
	 */
	public Vector3D intersect(Ray ray, Vector3D outNormalAtInter)
	{
		Vector3D intersection = null;

		//Équation de sphère: (P-C)² - R² = 0 avec P un point sur la sphère, C le centre de la sphère et R le rayon
		//Équation paramétrique d'un rayon: O + kD avec O l'origine du rayon, D la direction du rayon et k un réel
		//En substituant on obtient (O + kD - C)² - R² = 0 <--> O² + (kD)² + C² + 2OkD + 2OC + 2kDC - R² = 0 <--> k²(D²) + k(2OD + 2DC) + (O² + C² + 2OC - R²) = 0
		//On cherche k
		
		Vector3D OC = new Vector3D(ray.getOrigin(), center);
		
		double a = Vector3D.dotProduct(ray.getDirection(), ray.getDirection());// = D²
		double b = -2 * Vector3D.dotProduct(ray.getDirection(), OC);// = 2D(O-C)
		double c = Vector3D.dotProduct(OC, OC) - radius*radius;
		
		double b2 = b*b;
		double ac4 = 4*a*c;
		double discri = b2 - ac4;
		double k1 = 0, k2 = 0;
		
		//Détermination de k par la méthode du discriminant
		if(discri < 0)
			return null;
		else if(discri == 0)
			 k1 = -b/(2*a);
		else
		{
			k1 = (-b - Math.sqrt(discri))/(2*a);
			k2 = (-b + Math.sqrt(discri))/(2*a);
			
			//Les deux intersections sont derrière la caméra, il n'y a donc pas d'intersection valable, on renvoie null
			if(k1 < 0 && k2 < 0)
				return null;
			
			//On ne prend en compte que la première intersection avec la sphère donc on cherche quel k est le plus petit
			if(k2 < k1)
			{
				double temp = k1;
				k1 = k2;
				k2 = temp;
			}
			
			if(k1 < 0)//Si le k le plus petit est en fait négatif, on choisit l'autre k
				k1 = k2;
		}
		if(k1 < 0)//Le point d'intersection est derrière la caméra
			return null;
		
		//On peut maintenant calculer les coordonnées du point d'intersection avec la sphère à l'aide de k1 qui contient le "bon" k
		intersection = ray.determinePoint(k1);
		if(outNormalAtInter != null)
			outNormalAtInter.copyIn(this.getNormal(intersection));//On défini la normale au point d'intersection
		
		return intersection;
	}

	public void setCenter(Vector3D center) 
	{
		this.center = center;
	}
	
	public void setMaterial(Material material) 
	{
		this.material = material;
	}
	
	public void setRadius(double radius) 
	{
		this.radius = radius;
	}
}
