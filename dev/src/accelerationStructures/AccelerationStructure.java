package accelerationStructures;

import geometry.Shape;
import maths.Point;
import maths.Ray;
import maths.Vector;
import rayTracer.RayTracerStats;

/**
 * Définit une structure d'accélération que peut utiliser le ray-tracer pour accélérer ses temps de rendu. Une structure d'accélération
 * doit fournir un moyen de tester un rayon contre les objets de la scène au moyen d'une méthode 'intersect'. Pour tout rayon passé à
 * 'intersect', la structure doit alors renvoyer l'objet le plus proche intersecté par ce rayon
 */
public interface AccelerationStructure 
{
	/**
	 * Test si le rayon passé en paramètre intersecte des éléments de la structure d'accélération. Calcule et met à disposition le point
	 * d'intersection le plus proche trouvé (si bel et bien trouvé) ainsi que la normale au point d'intersection trouvé de la surface 
	 * de l'objet intersecté.
	 * 
	 * @param interStats La structure contenant des statistiques à propos du nombre de tests d'intersection effectués. Sera automatiquement mise
	 * à jour. Possibilité de passer null pour ne pas mettre à jour de statistiques
	 * @param ray Le rayon dont on veut tester l'intersection avec la structure
	 * @param outInterPoint Si un point d'intersection est trouvé, les coordonnées du point d'intersection trouvé seront
	 * stockées dans ce paramètre. Les coordonnées existantes de ce paramètre seront alors écrasées
	 * @param outNormalAtInter Si un point d'intersection est trouvé, le vecteur normale au point d'intersection
	 * de la surface de l'objet sera stocké dans ce paramètre. Les coordonnées existantes de ce paramètre seront alors écrasées. 
	 * 
	 * @return L'objet le plus proche de la caméra qui a été intersecté par le rayon. Retourne null si aucun objet n'a été intersecté.
	 */
	public Shape intersect(RayTracerStats interStats, Ray ray, Point outInterPoint, Vector outNormalAtInter);
}
