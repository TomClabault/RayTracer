package accelerationStructures;

import java.util.ArrayList;

import geometry.Shape;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class NoAccelerationStructure implements AccelerationStructure
{
	private ArrayList<Shape> shapes;
	
	public NoAccelerationStructure(ArrayList<Shape> sceneShapes)
	{
		this.shapes = sceneShapes;
	}

	@Override
	public Shape intersect(Ray ray, Point outInterPoint, Vector outNormalAtInter) 
	{
		Shape closestIntersectedObject = null;
		Point closestInterPoint = null;
		Vector normalAtClosestsInterPoint = null;
		
		Double tMin = null;
		
		for(Shape shape : shapes)
		{
			Point tempInterPoint = new Point(0, 0, 0);
			Vector tempNormalAtInter = new Vector(0, 0, 0);
				
			Double t = shape.intersect(ray, tempInterPoint, tempNormalAtInter);
			if(t != null && t > 0)//Si on a trouvé une intersection
			{
				if(tMin == null || t < tMin)
				{
					tMin = t;
					
					closestInterPoint = tempInterPoint;
					normalAtClosestsInterPoint = tempNormalAtInter;
					closestIntersectedObject = shape;
				}
			}
		}
		
		if(closestIntersectedObject != null)//Si on a bel et bien trouvé un point d'intersection entre le rayon et un objet de la scène
		{
			if(outInterPoint != null)
				outInterPoint.copyIn(closestInterPoint);
			if(outNormalAtInter != null)
				outNormalAtInter.copyIn(normalAtClosestsInterPoint);
			
			return closestIntersectedObject;
		}
		
		return null;
	}
}
