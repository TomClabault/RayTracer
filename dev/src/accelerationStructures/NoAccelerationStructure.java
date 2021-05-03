package accelerationStructures;

import java.util.ArrayList;

import geometry.ObjectContainer;
import geometry.Shape;
import maths.Point;
import maths.Ray;
import maths.Vector;
import rayTracer.RayTracerStats;

public class NoAccelerationStructure implements AccelerationStructure
{
	private ArrayList<Shape> shapes;
	
	public NoAccelerationStructure(ArrayList<Shape> sceneShapes)
	{
		this.shapes = sceneShapes;
	}

	@Override
	public Double intersect(RayTracerStats interStats, Ray ray, Point outInterPoint, Vector outNormalAtInter, ObjectContainer objectContainer) 
	{
		Point closestInterPoint = null;
		Vector normalAtClosestInterPoint = null;
		
		Double tMin = null;
		
		for(Shape shape : shapes)
		{
			Point tempInterPoint = new Point(0, 0, 0);
			Vector tempNormalAtInter = new Vector(0, 0, 0);
				
			interStats.incrementIntersectionTestsBy(shape.getSubObjectCount());
			Double t = shape.intersect(ray, tempInterPoint, tempNormalAtInter);
			if(t != null)//Si on a trouvé une intersection
			{
				if(tMin == null || t < tMin)
				{
					tMin = t;
					
					closestInterPoint = tempInterPoint;
					normalAtClosestInterPoint = tempNormalAtInter;
					objectContainer.setContainedShape(shape);
				}
			}
		}
		
		if(tMin != null)//Si on a bel et bien trouvé un point d'intersection entre le rayon et un objet de la scène
		{
			if(outInterPoint != null)
				outInterPoint.copyIn(closestInterPoint);
			if(outNormalAtInter != null)
				outNormalAtInter.copyIn(normalAtClosestInterPoint);
			
			return tMin;
		}
		
		return null;//Aucune intersection trouvée
	}
}
