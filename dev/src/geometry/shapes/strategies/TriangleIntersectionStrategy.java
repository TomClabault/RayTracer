package geometry.shapes.strategies;

import geometry.shapes.Triangle;
import maths.Point;
import maths.Ray;
import maths.Vector;

public interface TriangleIntersectionStrategy 
{
	/**
	 * Calcule l'intersection du triangle représenté par cette instance avec un rayon passé en paramètre. Cette méthode ne cherche l'intersection que dans la direction du rayon (c'est à dire pas "derrière" le rayon / derrière la caméra).
	 * 
	 * @param ray Le rayon avec lequel chercher une intersection
	 * @param outInterPoint		Si un point d'intersection est trouvé, les coordonnées du point d'intersection seront placées dans ce paramètre,
	 * 							écrasant toutes coordonées pré-existantes
	 * @param outNormalAtInter 	Ce vecteur reçevra la normale du triangle si un point d'intersection avec le rayon est trouvé. 
	 * 							Si aucun point d'intersection n'est trouvé, ce vecteur reste inchangé. 
	 * 							De même, si outNormalAtInter vaut null à l'appel de la méthode, le vecteur eestera inchangé et la normale du triangle ne sera pas tockée.
	 *  
	 * @return Le point d'intersection du rayon et du triangle. Null s'il n'y a pas d'intersection
	 */
	public Double intersect(Triangle triangle, Ray ray, Point outInterPoint, Vector outNormalAtInter);
}
