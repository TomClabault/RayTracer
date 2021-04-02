package geometry;

import materials.Material;
import maths.Point;
import maths.Ray;
import maths.Vector;

public interface Shape
{
	public Material getMaterial();
	
	/*
	 * Permet d'obtenir la normale à un point donné de la forme
	 * 
	 * @param point Le point par rapport auquel on souhaite la normale
	 */
	public Vector getNormal(Point point);
	
	/*
	 * Permet de récupérer les coordonnées (u, v) de la forme au point donné
	 * 
	 * @param Point Le point auquel on souhaite récupérer les coordonnées u et v 
	 * 
	 * @return Retourne un point (x, y, z) contenant les coordoonnées (u, v) tel que x = u, y = v et z = 0. La troisième coordonnée du point sera toujours fixée à 0 car non utilisée.
	 */
	public Point getUVCoords(Point point);
	
	/*
	 * Calcule le point d'intersection avec un rayon et le renvoie si existant. Le point d'intersection n'est cherché que "en face" du rayon.
	 * 
	 * @param ray 				Rayon avec lequel chercher une intersection
	 * @param outNormalAtInter 	Si un point d'intersection est trouvé, la méthode intersect redéfini ce vecteur pour qu'il représente la normale au point d'intersection trouvé de la forme
	 * 							Si aucun point d'intersection n'est trouvé, ce vecteur reste inchangé
	 * 							Si outNormalAtInter est null à l'appel de la méthode, intersect ne calculera pas la normale à l'intersection et laissera le vecteur inchangé
	 * 
	 * @return Renvoie le point d'intersection du rayon et de l'objet. Null s'il n'y a pas de point d'intersection
	 */
	public Point intersect(Ray ray, Vector outNormalAtInter);
}
