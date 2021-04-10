package geometry;

import java.util.ArrayList;

import geometry.shapes.Triangle;
import materials.Material;
import maths.Point;
import maths.Ray;
import maths.Vector;

public abstract class ShapeTriangleUtil
{
	protected Material material;
	protected ArrayList<Triangle> listeTriangle;

	/*
	 * @link{geometry.Shape#intersect} 
	 */
	public Point intersect(Ray ray, Vector outNormalAtInter)
	{
		Double distancemin = null;
		Point intersection = null;
		Point closestInterPoint = null;
		Triangle intersectedTriangle = null;
		
		for (int i = 0; i < listeTriangle.size(); i++)
		{
			Triangle triangle = listeTriangle.get(i);
			intersection = triangle.intersect(ray, null);
			if(intersection != null)
			{
				double distance = Point.distance(intersection, ray.getOrigin());
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
	
	/*
	 * @link{geometry.Shape#getNormal} 
	 */
	public Vector getNormal(Point point)
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

	/*
	 * @link{geometry.Shape#getMaterial} 
	 */
	public Material getMaterial()
	{
		return material;
	}
	
	/*
	 * @link{geometry.Shape#getUVCoords}
	 */
	@Override
	public Point getUVCoords(Point point)
	{
		return null;
	}
	
	/*
	 * @link{geometry.Shape#setMaterial} 
	 */
	public void setMaterial(Material material)
	{
		this.material = material;
	}
}
