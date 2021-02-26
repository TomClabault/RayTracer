package geometry;

import maths.Point;
import maths.Ray;
import maths.Vector;

public interface Shape
{
	/*
	 * Permet d'obtenir la normale à un point donné de la forme
	 * 
	 * @param point Le point par rapport auquel on souhaite la normale
	 */
	public abstract Vector getNormal(Point point);
	
	/*
	 * Calcule le point d'intersection avec un rayon et le renvoie si existant. Le point d'intersection n'est cherché que "en face" du rayon.
	 * 
	 * @param ray Rayon avec lequel chercher une intersection
	 * 
	 * @return Renvoie le point d'intersection du rayon et de l'objet. Null s'il n'y a pas de point d'intersection
	 */
	public Point intersect(Ray ray);
}
