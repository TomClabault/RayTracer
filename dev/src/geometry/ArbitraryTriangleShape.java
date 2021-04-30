package geometry;

import java.util.ArrayList;

import accelerationStructures.BVH.BVHAccelerationStructure;
import accelerationStructures.BVH.BoundingVolume;
import geometry.shapes.Plane;
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
	
	public BoundingVolume computeBoundingVolume()
	{
		BoundingVolume volume = new BoundingVolume();
		
		for(int i = 0; i < BVHAccelerationStructure.PLANE_SET_NORMAL_COUNT; i++)
		{
			double dNear = Double.MAX_VALUE;
			double dFar = Double.MIN_VALUE;
			
			for(Triangle triangle : this.triangleList)
			{
				for(int vertexIndex = 0; vertexIndex < 3; vertexIndex++)
				{
					Point vertex = vertexIndex == 0 ? triangle.getA() : vertexIndex == 1 ? triangle.getB() : triangle.getC();
					
					double d = Vector.dotProduct(BVHAccelerationStructure.PLANE_SET_NORMALS[i], vertex.toVector());
					
					dNear = Math.min(dNear, d);
					dFar = Math.max(dFar, d);
				}
			}
			
			volume.setBounds(dNear, dFar, i);
		}
		
		return volume;
	}
	
	/**
	 * {@link geometry.Shape#intersect} 
	 */
	public Double intersect(Ray ray, Point outInterPoint, Vector outNormalAtInter)
	{
		Double tMin = null;
		
		Triangle intersectedTriangle = null;
		
		Point intersectionPoint = null;
		Point closestInterPoint = null;
		
		for (int i = 0; i < triangleList.size(); i++)
		{
			Triangle triangle = triangleList.get(i);
			Double t = triangle.intersect(ray, intersectionPoint, null);
			if(t != null)
			{
				if(tMin == null || t < tMin )
				{
					tMin = t;
					
					closestInterPoint = intersectionPoint;
					intersectedTriangle = triangleList.get(i);
				}
			}
		}
		
		
		if (outNormalAtInter != null)
			if (intersectedTriangle != null)
				outNormalAtInter.copyIn(intersectedTriangle.getNormal(null));
		
		outInterPoint.copyIn(closestInterPoint);
		return tMin;
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
