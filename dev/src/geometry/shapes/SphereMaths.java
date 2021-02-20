package geometry.shapes;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import geometry.Point;
import geometry.Ray;
import geometry.ShapeMaths;
import geometry.Vector;
import javafx.scene.paint.Color;

/*
 * Classe représentant une sphère décrite par son centre ainsi que son rayon. Représente la "version" mathématique d'une sphère. 
 * Pour une représentation polygonale d'une sphère, voir SphereTriangle
 */
public class SphereMaths implements ShapeMaths
{
	Point center;

	Color sphereColor;
	
	double radius;
	
	/*
	 * Crée une sphère blanche à partie de son centre et de son rayon
	 * 
	 * @param center Point représentant le centre de la sphère
	 * @param radius Rayon de la sphère 
	 */
	public SphereMaths(Point center, double radius)
	{
		this(center, Color.rgb(255, 255, 255), radius);
	}
	
	/*
	 * Crée une sphère blanche à partie de son centre et de son rayon
	 * 
	 * @param center Point représentant le centre de la sphère
	 * @param sphereColor Objet Color.RGB représentant la couleur de la sphère
	 * @param radius Rayon de la sphère 
	 */
	public SphereMaths(Point center, Color sphereColor, double radius) 
	{
		this.center = center;
		this.sphereColor = sphereColor;
		
		this.radius = radius;
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
	
	public Vector getNormal(Point point)
	{
		return Vector.normalize(Point.p2v(Point.sub(point, center)));
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

		//System.out.println("Ray: " + ray);
		
		//Équation de sphère: (P-C)² - R² = 0 avec P un point sur la sphère, C le centre de la sphère et R le rayon
		//Équation paramétrique d'un rayon: O + kD avec O l'origine du rayon, D la direction du rayon et k un réel
		//En substituant on obtient (O + kD - C)² - R² = 0 <--> O² + (kD)² + C² + 2OkD + 2OC + 2kDC - R² = 0 <--> k²(D²) + k(2OD + 2DC) + (O² + C² + 2OC - R²) = 0
		//On cherche k
		
		Vector OC = new Vector(center, ray.getOrigin());
		
		double a = Vector.dotProduct(ray.getDirection(), ray.getDirection());// = D²
		double b = 2*Vector.dotProduct(ray.getDirection(), OC);// = 2D(O-C)
		double c = Vector.dotProduct(OC, OC) - radius*radius;
		
		assert a == 1 : String.format("a != 1 dans SphereMaths.intersect()\na = %.3f\n%s\n", a, ray);;
		
		//System.out.println(String.format("a, b, c = %.3f, %.3f, %.3f", a, b, c));
		
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
			k1 = (-b - Math.sqrt(discri))/2*a;
			k2 = (-b + Math.sqrt(discri))/2*a;
			
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
	
		//On peut maintenant calculer les coordonnées du point d'intersection avec la sphère à l'aide de k1 qui contient le "bon" k
		intersection = ray.determinePoint(k1);
		
		return intersection;
	}
}
