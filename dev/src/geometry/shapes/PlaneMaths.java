package geometry.shapes;

import geometry.Point;
import geometry.Ray;
import geometry.ShapeMaths;
import geometry.Vector;
import javafx.scene.paint.Color;

public class PlaneMaths implements ShapeMaths
{
	//Equation de plan: Ax + By + Cz + D = 0
	Vector normal;//partie (A, B, C) de l'équation
	double d;//partie D de l'équation
	
	Color color;
	int shininess;
	double diffuse;
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et du coefficient D de l'équation de plan. D agit sur le "coulissement" du plan le long de sa normale. 
	 * Un D positif remontera le plan dans le sens de sa normale, un D négatif le descendra
	 * 
	 * @param normal Vecteur normal au plan
	 * @param d Composante D de l'équation du plan
	 */
	public PlaneMaths(Vector normal, double d)
	{
		this(normal, d, Color.rgb(128, 128, 128), 100, 1);
	}
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et du coefficient D de l'équation de plan. D agit sur le "coulissement" du plan le long de sa normale. 
	 * Un D positif remontera le plan dans le sens de sa normale, un D négatif le descendra
	 * 
	 * @param normal Vecteur normal au plan
	 * @param d Composante D de l'équation du plan
	 * @param color Couleur du plan
	 */
	public PlaneMaths(Vector normal, double d, Color color)
	{
		this.normal = normal;
		this.d = d;

		this.color = color;
		this.shininess = 2;
		this.diffuse = 0.5;
	}
	
	/*
	 * Crée un plan à partir d'un vecteur normal au plan et du coefficient D de l'équation de plan. D agit sur le "coulissement" du plan le long de sa normale. 
	 * Un D positif remontera le plan dans le sens de sa normale, un D négatif le descendra
	 * 
	 * @param normal Vecteur normal au plan
	 * @param d Composante D de l'équation du plan
	 * @param color Couleur du plan
	 * @param shininess Brillance du plan. Plus la valeur est petite pour le plan est brillant
	 * @param diffuseCoeff Coefficient de diffusion de la lumière à l'impact du plan. Plus le coefficient est petit moins le plan est diffus
	 */
	public PlaneMaths(Vector normal, double d, Color color, int shininess, double diffuseCoeff)
	{
		this.normal = normal;
		this.d = d;

		this.color = color;
		this.shininess = shininess;
		this.diffuse = diffuseCoeff;
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
		if(Math.abs(Vector.dotProduct(ray.getDirection(), this.normal)) < 0.0000001d)//Le rayon et le plan sont parallèles
			return null;
			
		double coeffVectorPoint = (Vector.dotProduct(this.normal, ray.getOriginV()) + this.d) / Vector.dotProduct(this.normal, ray.getDirection());
		if(coeffVectorPoint > 0)
			return ray.determinePoint(coeffVectorPoint);
		else
			return null;
	}
}
