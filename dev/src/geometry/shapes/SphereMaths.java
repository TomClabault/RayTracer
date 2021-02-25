package geometry.shapes;

import geometry.ShapeMaths;
import javafx.scene.paint.Color;
import maths.Point;
import maths.Ray;
import maths.Vector;

/*
 * Classe représentant une sphère décrite par son centre ainsi que son rayon. Représente la "version" mathématique d'une sphère. 
 * Pour une représentation polygonale d'une sphère, voir SphereTriangle
 */
public class SphereMaths implements ShapeMaths
{
	Point center;
	double radius;

	Color sphereColor;
	int shininess;
	double specularCoeff;
	double diffuseComponent;
	boolean isReflective;
	
	/*
	 * Crée une sphère blanche à partie de son centre et de son rayon
	 * 
	 * @param center Point représentant le centre de la sphère
	 * @param radius Rayon de la sphère 
	 */
	public SphereMaths(Point center, double radius)
	{
		this(center, radius, Color.rgb(255, 255, 255), 20, 1, 0.5, false);
	}
	
	/*
	 * Crée une sphère blanche à partie de son centre et de son rayon
	 * 
	 * @param center Point représentant le centre de la sphère
	 * @param radius Rayon de la sphère 
	 * @param sphereColor Objet Color.RGB représentant la couleur de la sphère
	 * @param specularComponent Caractéristique spéculaire de la sphère. Entier positif
	 */
	public SphereMaths(Point center, double radius, Color sphereColor, int shininess, double specularCoeff, double diffuseComponent, boolean isReflective) 
	{
		this.center = center;
		this.radius = radius;

		this.sphereColor = sphereColor;
		this.shininess = shininess;
		this.specularCoeff = specularCoeff;
		this.diffuseComponent = diffuseComponent;
		this.isReflective = isReflective;
	}
	
	/*
	 * Permet d'obtenir le centre de la sphère
	 * 
	 * @return Instance sur le point représentant le centre de la sphère 
	 */
	public Point getCenter()
	{
		return this.center;
	}
	
	/*
	 * Permet d'obtenir la couleur de la sphère
	 * 
	 * @return Objet Color.RGB contenant la couleur de la sphère
	 */
	public Color getColor()
	{
		return this.sphereColor;
	}
	
	/*
	 * Permet d'obtenir la composante diffuse d'un objet
	 * 
	 * @return Retourne un réel entre 0 et 1 représentant le pourcentage de diffusion de la lumière par l'objet
	 */
	public double getDiffuse()
	{
		return this.diffuseComponent;
	}
	
	/*
	 * Permet de savoir si la sphère réfléchi la lumière ou non
	 * 
	 * @return Retourne true si l'objet est réfléchissant, false sinon
	 */
	public boolean getIsReflective()
	{
		return this.isReflective;
	}
	
	/*
	 * Retourne le vecteur normal à la sphère à un point donné
	 * 
	 * @param point Le point définissant la direction du vecteur normal de la sphère
	 * 
	 * @return Un vecteur d'origine le centre de la sphère et de direction le point passé en argument représentant la normale de la sphère en un point donné. Ce vecteur est normalisé  
	 */
	public Vector getNormal(Point point)
	{
		return Vector.normalize(Point.p2v(Point.sub(point, center)));
	}
	
	/*
	 * Retourne la composante spéculaire de l'objet
	 * 
	 * @return Un entier positif représentant la composante spéculaire de l'objet
	 */
	public int getShininess()
	{
		return this.shininess;
	}
	
	public double getSpecularCoeff()
	{
		return this.specularCoeff;
	}
	
	/*
	 * Calcule de façon analytique l'intersection d'un rayon et d'une sphère
	 * 
	 * @param ray Le rayon avec lequel l'intersection avec la sphère doit être calculée
	 * 
	 * @return Retourne le point d'intersection avec la sphère s'il existe (s'il y a deux points d'intersection, ne retourne que le point le plus près de l'origine du rayon). Retourne null sinon.
	 */
	public Point intersect(Ray ray)
	{
		Point intersection = null;

		//Équation de sphère: (P-C)² - R² = 0 avec P un point sur la sphère, C le centre de la sphère et R le rayon
		//Équation paramétrique d'un rayon: O + kD avec O l'origine du rayon, D la direction du rayon et k un réel
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
		
		return intersection;
	}
}
