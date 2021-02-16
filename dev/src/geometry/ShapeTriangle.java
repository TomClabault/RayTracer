package geometry;

/*
 * Interface permettant d'implémenter des formes géomtriques construites à partir de triangles telles que les ico-sphère par exemple
 */
public interface ShapeTriangle 
{
	/*
	 * Calcule le point d'intersection avec un rayon et le renvoie si existant
	 * 
	 * @param ray Rayon avec lequel chercher une intersection
	 * 
	 * @return Renvoie le point d'intersection du rayon et de l'objet. Null s'il n'y a pas de point d'intersection
	 */
	public Point intersect(Ray ray);
}
