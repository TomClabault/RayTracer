package geometry;

import java.util.ArrayList;

import accelerationStructures.BoundingBox;
import accelerationStructures.BoundingVolume;
import geometry.shapes.Triangle;
import materials.Material;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class ArbitraryTriangleShape implements Shape
{
	protected Material material;
	protected ArrayList<Triangle> triangleList;
	
	public ArbitraryTriangleShape(Material material)
	{
		this.material = material;
		this.triangleList = new ArrayList<>();
	}
	
	/**
	 * Permet d'ajouter un triangle a la liste de triangles de la forme
	 * 
	 * @param triangle Le triangle a ajouter
	 */
	public void addTriangle(Triangle triangle)
	{
		this.triangleList.add(triangle);
	}
	
	/**
	 * {@link geometry.Shape#getBoundingBox()}
	 */
	@Override
	public BoundingBox getBoundingBox() 
	{
		BoundingBox boundingBox = new BoundingBox(new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY),
												  new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
		
		for(Triangle triangle : triangleList)
			boundingBox.extendBy(triangle.getBoundingBox());
		
		return boundingBox;
	}
	
	/**
	 * {@link geometry.Shape#getBoundingVolume()}
	 */
	@Override
	public BoundingVolume getBoundingVolume()
	{
		BoundingVolume volume = new BoundingVolume();
		
		for(int i = 0; i < BoundingVolume.PLANE_SET_NORMAL_COUNT; i++)
		{
			double dMin = Double.POSITIVE_INFINITY;
			double dMax = Double.NEGATIVE_INFINITY;
			
			for(Triangle triangle : this.triangleList)
			{
				for(int vertexIndex = 0; vertexIndex < 3; vertexIndex++)
				{
					Point vertex = vertexIndex == 0 ? triangle.getA() : vertexIndex == 1 ? triangle.getB() : triangle.getC();
					
					double d = Vector.dotProduct(BoundingVolume.PLANE_SET_NORMALS[i], vertex.toVector());
					
					dMin = Math.min(dMin, d);
					dMax = Math.max(dMax, d);
				}
			}
			
			volume.setBounds(dMin, dMax, i);
		}
		volume.setEnclosedObject(this);
		
		return volume;
	}
	
	/**
	 * {@link geometry.Shape#intersect} 
	 */
	public Double intersect(Ray ray, Point outInterPoint, Vector outNormalAtInter)
	{
		Double tMin = null;
		
		Triangle intersectedTriangle = null;
		
		Point intersectionPoint = new Point(0, 0, 0);
		Point closestInterPoint = new Point(0, 0, 0);
		
		for (int i = 0; i < triangleList.size(); i++)
		{
			Triangle triangle = triangleList.get(i);
			Double t = triangle.intersect(ray, intersectionPoint, null);
			if(t != null)
			{
				if(tMin == null || t < tMin )
				{
					tMin = t;
					
					closestInterPoint.copyIn(intersectionPoint);
					intersectedTriangle = triangleList.get(i);
				}
			}
		}
		
		
		if (outNormalAtInter != null)
			if (intersectedTriangle != null)
				outNormalAtInter.copyIn(intersectedTriangle.getNormal(null));
		
		if(closestInterPoint != null && outInterPoint != null)//Si on a trouve une intersection et que le point de sortie n'est pas null et qu'il
		//peut donc accueillir le nouveau point d'intersection
			outInterPoint.copyIn(closestInterPoint);
		
		return tMin;
	}
	
	/**
	 * {@link geometry.Shape#getNormal} 
	 * @deprecated Utiliser 'outNormalAtInter' de {@link geometry.ArbitraryTriangleShape#intersect(Ray, Vector)} pour recuperer la normal 
	 * a un point d'intersection.
	 * Cette methode n'est pas fonctionnelle
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
	 * {@link geometry.Shape#getSubObjectCount()}
	 * 
	 * @return Le nombre de triangles dont est composee la forme
	 */
	@Override
	public int getSubObjectCount() 
	{
		return this.triangleList.size();
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
	 * Peut produire de tres grosses chaîne de caractere.
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
