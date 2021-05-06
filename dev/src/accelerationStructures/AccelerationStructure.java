package accelerationStructures;

import geometry.Shape;
import maths.Point;
import maths.Ray;
import maths.Vector;
import rayTracer.RayTracerStats;

/**
 * Definit une structure d'acceleration que peut utiliser le ray-tracer pour accelerer ses temps de rendu. Une structure d'acceleration
 * doit fournir un moyen de tester un rayon contre les objets de la scene au moyen d'une methode 'intersect'. Pour tout rayon passe a
 * 'intersect', la structure doit alors renvoyer l'objet le plus proche intersecte par ce rayon
 */
public interface AccelerationStructure 
{
	/**
	 * Test si le rayon passe en parametre intersecte des elements de la structure d'acceleration. Calcule et met a disposition le point
	 * d'intersection le plus proche trouve (si bel et bien trouve) ainsi que la normale au point d'intersection trouve de la surface 
	 * de l'objet intersecte.
	 * 
	 * @param interStats La structure contenant des statistiques a propos du nombre de tests d'intersection effectues. Sera automatiquement mise
	 * a jour. Possibilite de passer null pour ne pas mettre a jour de statistiques
	 * @param ray Le rayon dont on veut tester l'intersection avec la structure
	 * @param outInterPoint Si un point d'intersection est trouve, les coordonnees du point d'intersection trouve seront
	 * stockees dans ce parametre. Les coordonnees existantes de ce parametre seront alors ecrasees
	 * @param outNormalAtInter Si un point d'intersection est trouve, le vecteur normale au point d'intersection
	 * de la surface de l'objet sera stocke dans ce parametre. Les coordonnees existantes de ce parametre seront alors ecrasees. 
	 * 
	 * @return L'objet le plus proche de la camera qui a ete intersecte par le rayon. Retourne null si aucun objet n'a ete intersecte.
	 */
	public Shape intersect(RayTracerStats interStats, Ray ray, Point outInterPoint, Vector outNormalAtInter);
}
