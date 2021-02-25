package geometry.shapes;

import geometry.ShapeMaths;
import javafx.scene.paint.Color;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class PlaneMaths implements ShapeMaths
{
	//Equation de plan: (p - point).normal = 0
	Vector normal;//partie (A, B, C) de l'équation
	Point point;//partie D de l'équation
	
	Color color;
	int shininess;
	double diffuse;
	double reflectiveCoeff;
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et d'un point appartenant au plan
	 * 
	 * @param normal Vecteur normal au plan
	 * @param point Point par lequel passe le vecteur
	 */
	public PlaneMaths(Vector normal, Point point)
	{
		this(normal, point, Color.rgb(128, 128, 128), 100, 1, 0);
	}
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et d'un point appartenant au plan 
	 * 
	 * @param normal Vecteur normal au plan
	 * @param point Point par lequel passe le vecteur
	 * @param color Couleur du plan
	 */
	public PlaneMaths(Vector normal, Point point, Color color)
	{
		this(normal, point, color, 2, 0.5, 0);
	}
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et d'un point appartenant au plan 
	 * 
	 * @param normal Vecteur normal au plan
	 * @param point Point par lequel passe le vecteur
	 * @param color Couleur du plan
	 * @param shininess Brillance du plan. Plus la valeur est petite pour le plan est brillant
	 * @param diffuseCoeff Coefficient de diffusion de la lumière à l'impact du plan. Plus le coefficient est petit moins le plan est diffus
	 */
	public PlaneMaths(Vector normal, Point point, Color color, int shininess, double diffuseCoeff, double reflectiveCoeff)
	{
		this.normal = normal;
		this.point = point;

		this.color = color;
		this.shininess = shininess;
		this.diffuse = diffuseCoeff;
		this.reflectiveCoeff = reflectiveCoeff;
	}
	
	/*
	 * Permet d'obtenir la couleur du plan
	 * 
	 * @return Un objet Color représentant la couleur du plan
	 */
	@Override
	public Color getColor() 
	{
		return this.color;
	}
	
	/*
	 * Permet d'obtenir la caractéristique diffuse du plan
	 * 
	 * @return Un réel entre 0 et 1 représentant la caractéristique diffuse du plan
	 */
	@Override
	public double getDiffuse() 
	{
		return this.diffuse;
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
	 * Retourne le coefficient de réflectivité du plan
	 * 
	 * @return Réel entre 0 et 1 représentant la réflectivité du plan
	 */
	public double getReflectiveCoeff()
	{
		return this.reflectiveCoeff;
	}
	
	/*
	 * Permet d'obtenir la brillance de l'objet
	 * 
	 * @return Un entier positif représentant la brillance de l'objet
	 */
	@Override
	public int getShininess() 
	{
		return this.shininess;
	}

	/*
	 * Permet d'obtenir le coefficient de spécularité de l'objet
	 * 
	 * @return Un réel entre 0 et 1 représentant le coefficient de spécularité de l'objet
	 */
	@Override
	public double getSpecularCoeff() 
	{
		// TODO Auto-generated method stub
		return 0;
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
