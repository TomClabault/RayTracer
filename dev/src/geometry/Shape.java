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
	 * Permet de recuperer le materiau de la forme
	 * 
	 * @return Le materiau de la forme caracterisant son aspect visuel
	 */
	public Material getMaterial();
	
	/**
	 * Permet d'obtenir la normale a un point donne de la forme
	 * 
	 * @param point Le point par rapport auquel on souhaite la normale. 
	 * Certaines formes ont la meme normale pour tout point appartenant a la forme. Dans un tel cas, le parametre
	 * 'point' sera ignore.
	 */
	public Vector getNormal(Point point);
	
	/**
	 * Retourne le nombre d'objet dont est composee la forme. Certaines etant composees d'une multitude triangle, cette
	 * methode retournerait donc le nombre de triangles dont est composee la forme. Pour des formes telle que les spheres,
	 * doit retourner 1 puisqu'une sphere n'est composee que d'un seul objet, elle meme.
	 *  
	 * @return Le nombre d'objet dont est composee la forme. 
	 */
	public int getSubObjectCount();
	
	/**
	 * Permet de recuperer les coordonnees (u, v) de la forme au point donne
	 * 
	 * @param point Le point auquel on souhaite recuperer les coordonnees u et v 
	 * 
	 * @return Retourne un point (x, y, z) contenant les coordoonnees (u, v) tel que x = u, y = v et z = 0. La troisieme coordonnee du point sera toujours fixee a 0 car non utilisee.
	 */
	public Point getUVCoords(Point point);
	
	/**
	 * Calcule le point d'intersection avec un rayon et le renvoie si existant. Le point d'intersection n'est cherche que "en face" du rayon.
	 * 
	 * @param ray 				Rayon avec lequel chercher une intersection
	 * @param outInterPoint		Si un point d'intersection est trouve, les coordonnees du point d'intersection trouve remplaceront les coordonees
	 * 							de cette instance de point. 
	 * @param outNormalAtInter 	Si un point d'intersection est trouve, la methode intersect redefini ce vecteur pour qu'il represente la normale au point d'intersection trouve de la forme
	 * 							Si aucun point d'intersection n'est trouve, ce vecteur reste inchange
	 * 							Si outNormalAtInter est null a l'appel de la methode, intersect ne calculera pas la normale a l'intersection et laissera le vecteur inchange
	 * 
	 * @return Renvoie le coefficient t qui, applique a l'equation du rayon, donne les coordonnees du point d'intersection.
	 * Retourne null si aucun point d'intersection n'a ete trouve
	 */
	public Double intersect(Ray ray, Point outInterPoint, Vector outNormalAtInter);
	
	/**
	 * Permet de redefinir le materiau qui sera utilise pour le rendu de l'objet
	 * 
	 * @param newMaterial Le nouveau materiau de l'objet
	 */
	public void setMaterial(Material newMaterial);
}
