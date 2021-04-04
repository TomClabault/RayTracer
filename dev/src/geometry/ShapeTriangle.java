package geometry;

import java.util.ArrayList;

import materials.Material;
import geometry.shapes.Triangle;
import maths.Vector3D;
import maths.Ray;

/*
 * Interface permettant d'implémenter des formes géomtriques construites à partir de triangles telles que les ico-sphère par exemple
 */
public abstract class ShapeTriangle implements Shape
{
	protected Material material;
	protected ArrayList<Triangle> listeTriangle;


	/*
	 * Permet d'obtenir la liste des triangles composant la forme
	 * 
	 * @return ArrayList<Triangle> contenant tous les triangles composant la forme
	 */

	public ArrayList<Triangle> getTriangleList()
	{
		return listeTriangle;
	}

	public Vector3D intersect(Ray ray, Vector3D outNormalAtInter)
	{
		Double distancemin = null;
		Vector3D intersection = null;
		Vector3D closestInterPoint = null;
		Triangle intersectedTriangle = null;
		
		for (int i = 0; i < listeTriangle.size(); i++)
		{
			Triangle triangle = listeTriangle.get(i);
			intersection = triangle.intersect(ray, null);
			if(intersection != null)
			{
				double distance = Vector3D.distance(intersection, ray.getOrigin());
				if(distancemin == null || distance < distancemin )
				{
					distancemin = distance;
					
					closestInterPoint = intersection;
					intersectedTriangle = listeTriangle.get(i);
				}
			}


		}
		if (outNormalAtInter != null)
		{
			if (intersectedTriangle != null)
			{
				outNormalAtInter.copyIn(intersectedTriangle.getNormal(null));
			}
		}
		
		return closestInterPoint;

	}
	public Vector3D getNormal(Vector3D point)
	{
		for (int i = 0 ; i < listeTriangle.size() ;i++)
		{
			if (listeTriangle.get(i).insideOutsideTest(point) == true)
			{
				return listeTriangle.get(i).getNormal(point);
			}
		}
		return null;
	}

	public Material getMaterial()
	{
		return material;
	}



}
