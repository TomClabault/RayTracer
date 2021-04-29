package geometry;

import java.util.ArrayList;

import geometry.shapes.Triangle;
import javafx.scene.paint.Color;
import materials.Material;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class ArbitraryTriangleShape implements Shape
{
	protected Material material;
	protected ArrayList<Triangle> listeTriangle;
	
	public ArbitraryTriangleShape()
	{
		this(new Material(Color.rgb(0, 0, 0), 0, 0, 0, 0, 0, false, 0, 0));
	}
	
	public ArbitraryTriangleShape(Material material)
	{
		this.material = material;
		this.listeTriangle = new ArrayList<>();
	}
	
	/**
	 * Permet d'ajouter un triangle à la liste de triangles de la forme
	 * 
	 * @param triangle Le triangle à ajouter
	 */
	public void addTriangle(Triangle triangle)
	{
		this.listeTriangle.add(triangle);
	}
	
	/**
	 * {@link geometry.Shape#intersect} 
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
	
	/**
	 * {@link geometry.Shape#getNormal} 
	 * @deprecated Utiliser 'outNormalAtInter' de {@link geometry.ArbitraryTriangleShape#intersect(Ray, Vector)} pour récupérer la normal à un point d'intersection
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

	/**
	 * {@link geometry.Shape#getMaterial} 
	 */
	public Material getMaterial()
	{
		return material;
	}
	
	/**
	 * {@link geometry.Shape#getUVCoords}
	 */
	public Point getUVCoords(Point point)
	{
		return null;
	}
	
	/**
	 * {@link geometry.Shape#setMaterial} 
	 */
	public void setMaterial(Material material)
	{
		this.material = material;
	}
	
	/**
	 * Peut produire de très grosses chaîne de caractère.
	 */
	@Override
	public String toString()
	{
		String output = "";
		
		for(Triangle triangle : this.listeTriangle)
			output += triangle + System.lineSeparator();
		
		return output;
	}
}
