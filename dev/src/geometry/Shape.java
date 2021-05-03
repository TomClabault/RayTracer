package geometry;

import accelerationStructures.BoundingBox;
import accelerationStructures.BoundingVolume;
import materials.Material;
import maths.Point;
import maths.Ray;
import maths.Vector;

public interface Shape
{
	/**
	 * Calcule et retourne la boundinx box de la forme
	 * 
	 * @return La bounding box de la forme {@link accelerationStructures.BoundingBox}.
	 * Retourne null si la forme n'a pas de bounding box (un plan par exemple) 
	 */
	public BoundingBox getBoundingBox();
	
	/**
	 * Calcule et retourne le BoundingVolume de la forme
	 * 
	 * @return Le bounding volume de la forme. {@link accelerationStructures.BoundingVolume}
	 * Retourne null si la forme n'a pas de bounding volume (un plan par exemple)
	 */
	public BoundingVolume getBoundingVolume();
	
	/**
	 * Permet de récupérer le matériau de la forme
	 * 
	 * @return Le matériau de la forme caractérisant son aspect visuel
	 */
	public Material getMaterial();
	
	/**
	 * Permet d'obtenir la normale à un point donné de la forme
	 * 
	 * @param point Le point par rapport auquel on souhaite la normale. 
	 * Certaines formes ont la même normale pour tout point appartenant à la forme. Dans un tel cas, le paramètre
	 * 'point' sera ignoré.
	 */
	public Vector getNormal(Point point);
	
	/**
	 * Retourne le nombre d'objet dont est composée la forme. Certaines étant composées d'une multitude triangle, cette
	 * méthode retournerait donc le nombre de triangles dont est composée la forme. Pour des formes telle que les sphères,
	 * doit retourner 1 puisqu'une sphère n'est composée que d'un seul objet, elle même.
	 *  
	 * @return Le nombre d'objet dont est composée la forme. 
	 */
	public int getSubObjectCount();
	
	/**
	 * Permet de récupérer les coordonnées (u, v) de la forme au point donné
	 * 
	 * @param point Le point auquel on souhaite récupérer les coordonnées u et v 
	 * 
	 * @return Retourne un point (x, y, z) contenant les coordoonnées (u, v) tel que x = u, y = v et z = 0. La troisième coordonnée du point sera toujours fixée à 0 car non utilisée.
	 */
	public Point getUVCoords(Point point);
	
	/**
	 * Calcule le point d'intersection avec un rayon et le renvoie si existant. Le point d'intersection n'est cherché que "en face" du rayon.
	 * 
	 * @param ray 				Rayon avec lequel chercher une intersection
	 * @param outInterPoint		Si un point d'intersection est trouvé, les coordonnées du point d'intersection trouvé remplaceront les coordonées
	 * 							de cette instance de point. 
	 * @param outNormalAtInter 	Si un point d'intersection est trouvé, la méthode intersect redéfini ce vecteur pour qu'il représente la normale au point d'intersection trouvé de la forme
	 * 							Si aucun point d'intersection n'est trouvé, ce vecteur reste inchangé
	 * 							Si outNormalAtInter est null à l'appel de la méthode, intersect ne calculera pas la normale à l'intersection et laissera le vecteur inchangé
	 * 
	 * @return Renvoie le coefficient t qui, appliqué à l'équation du rayon, donne les coordonnées du point d'intersection.
	 * Retourne null si aucun point d'intersection n'a été trouvé
	 */
	public Double intersect(Ray ray, Point outInterPoint, Vector outNormalAtInter);
	
	/**
	 * Permet de redéfinir le matériau qui sera utilisé pour le rendu de l'objet
	 * 
	 * @param newMaterial Le nouveau matériau de l'objet
	 */
	public void setMaterial(Material newMaterial);
}
