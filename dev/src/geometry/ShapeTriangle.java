package geometry;

import java.util.ArrayList;

import geometry.shapes.Triangle;

/*
 * Interface permettant d'implémenter des formes géomtriques construites à partir de triangles telles que les ico-sphère par exemple
 */
public interface ShapeTriangle extends Shape
{
	/*
	 * Permet d'obtenir la liste des triangles composant la forme
	 * 
	 * @return ArrayList<Triangle> contenant tous les triangles composant la forme
	 */
	public abstract ArrayList<Triangle> getTriangleList();
}
