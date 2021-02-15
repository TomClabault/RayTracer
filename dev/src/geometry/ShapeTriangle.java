package geometry;

/*
 * Interface permettant d'implémenter des formes géomtriques construites à partir de triangles telles que les ico-sphère par exemple
 */
public interface ShapeTriangle 
{
	/*
	 * Calcule le point d'intersection et le renvoie si existant
	 * 
	 * @param line Droite avec laquelle tester l'intersection
	 * 
	 * @return Renvoie le point d'intersection de la droite et de l'objet. Null s'il n'y a pas de point d'intersection
	 */
	public Point intersect(Line line);
}
