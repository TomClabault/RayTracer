package geometry;

/*
 * Interface permettant d'implémenter des formes géomtriques construites à partir de triangles telles que les ico-sphère par exemple
 */
public interface ShapeTriangle 
{
	public boolean intersect(Line line);
}
