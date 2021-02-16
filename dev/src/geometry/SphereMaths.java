package geometry;

public class SphereMaths implements ShapeMaths
{
	Point center;
	
	double radius;
	
	public SphereMaths(Point center, double radius)
	{
		this.center = center;
		this.radius = radius;
	}
	
	public Point intersect(Ray ray)
	{
		Point intersection = null;
		
		//Équation de sphère: (P-C)² - R² = 0 avec P un point sur la sphère, C le centre de la sphère et R le rayon
		//Équation paramétrique d'un rayon: O + kD avec O l'origine du rayon, D la direction du rayon et k un réel
		//En substituant on obtient (O + kD - C)² - R² = 0 <--> O² + (kD)² + C² + 2OkD + 2OC + 2kDC - R² = 0 <--> k²(D²) + k(2OD + 2DC) + (O² + C² + 2OC - R²) = 0
		//On cherche k
		
		double a = Vector.dotProduct(ray.getDirection(), ray.getDirection());// = D²
		double b = 2*Vector.dotProduct(ray.getOriginV(), ray.getDirection()) + 2*Vector.dotProduct(ray.getDirection(), Point.p2v(this.center));// = 2OD + 2DC
		double c =   Vector.dotProduct(ray.getOriginV(), ray.getOriginV()) 
				+   Vector.dotProduct(Point.p2v(this.center), Point.p2v(this.center)) 
			    + 2*Vector.dotProduct(ray.getOriginV(), Point.p2v(this.center))
			    - this.radius*this.radius;
		
		double b2 = b*b;
		double ac4 = 4*a*c;
		double discri = b2 - ac4;
		double k1 = 0, k2 = 0;
		
		//Détermination de k par la méthode du discriminant
		if(discri < 0)
			return null;
		else if(discri == 0)
			 k1 = -b/2*a;
		else
		{
			k1 = (-b - Math.sqrt(discri))/2*a;
			k2 = (-b + Math.sqrt(discri))/2*a;
		}
		
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
		//On peut maintenant calculer les coordonnées du point d'intersection avec la sphère à l'aide de k1 qui contient le "bon" k
		
		intersection = Point.add(ray.getOrigin(), Point.scalarMul(k1, ray.getDirectionP()));
		
		return intersection;
	}
}
