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
	protected ArrayList<Triangle> triangleList;
	
	public ArbitraryTriangleShape()
	{
		this(new Material(Color.rgb(0, 0, 0), 0, 0, 0, 0, 0, false, 0, 0));
	}
	
	public ArbitraryTriangleShape(Material material)
	{
		this.material = material;
		this.triangleList = new ArrayList<>();
	}
	
	/**
	 * Permet d'ajouter un triangle à la liste de triangles de la forme
	 * 
	 * @param triangle Le triangle à ajouter
	 */
	public void addTriangle(Triangle triangle)
	{
		this.triangleList.add(triangle);
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
		
		for (int i = 0; i < triangleList.size(); i++)
		{
			Triangle triangle = triangleList.get(i);
			intersection = triangle.intersect(ray, null);
			if(intersection != null)
			{
				double distance = Point.distance(intersection, ray.getOrigin());
				if(distancemin == null || distance < distancemin )
				{
					distancemin = distance;
					
					closestInterPoint = intersection;
					intersectedTriangle = triangleList.get(i);
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
	 * @deprecated Utiliser 'outNormalAtInter' de {@link geometry.ArbitraryTriangleShape#intersect(Ray, Vector)} pour récupérer la normal 
	 * à un point d'intersection.
	 * Cette méthode n'est pas fonctionnelle
	 */
	public Vector getNormal(Point point)
	{
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
	 * @return La liste des triangles composant l'objet
	 */
	public ArrayList<Triangle> getTriangleList()
	{
		return this.triangleList;
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
		
		for(Triangle triangle : this.triangleList)
			output += triangle + System.lineSeparator();
		
		return output;
	}
}
