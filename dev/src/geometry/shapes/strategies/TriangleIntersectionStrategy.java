package geometry.shapes.strategies;

import geometry.shapes.Triangle;
import maths.Point;
import maths.Ray;
import maths.Vector;

public interface TriangleIntersectionStrategy 
{
	/**
	 * Calcule l'intersection du triangle represente par cette instance avec un rayon passe en parametre. Cette methode ne cherche l'intersection que dans la direction du rayon (c'est a dire pas "derriere" le rayon / derriere la camera).
	 * 
	 * @param ray Le rayon avec lequel chercher une intersection
	 * @param outInterPoint		Si un point d'intersection est trouve, les coordonnees du point d'intersection seront placees dans ce parametre,
	 * 							ecrasant toutes coordonees pre-existantes
	 * @param outNormalAtInter 	Ce vecteur reçevra la normale du triangle si un point d'intersection avec le rayon est trouve. 
	 * 							Si aucun point d'intersection n'est trouve, ce vecteur reste inchange. 
	 * 							De meme, si outNormalAtInter vaut null a l'appel de la methode, le vecteur eestera inchange et la normale du triangle ne sera pas tockee.
	 *  
	 * @return Le point d'intersection du rayon et du triangle. Null s'il n'y a pas d'intersection
	 */
	public Double intersect(Triangle triangle, Ray ray, Point outInterPoint, Vector outNormalAtInter);
}
