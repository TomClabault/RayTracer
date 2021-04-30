package accelerationStructures.BVH;

import java.util.ArrayList;

import accelerationStructures.AccelerationStructure;
import geometry.Shape;
import maths.Point;
import maths.Ray;
import maths.Vector;
import scene.RayTracingScene;

public class BVHAccelerationStructure implements AccelerationStructure 
{
	public static final int PLANE_SET_NORMAL_COUNT = 7;
	
	public static final Vector[] PLANE_SET_NORMALS = new Vector[] 
	{
		new Vector(1, 0, 1),
		new Vector(0, 1, 0),
		new Vector(0, 0, 1),
		new Vector( Math.sqrt(3) / 3.f,  Math.sqrt(3) / 3.f, Math.sqrt(3) / 3.f), 
	    new Vector(-Math.sqrt(3) / 3.f,  Math.sqrt(3) / 3.f, Math.sqrt(3) / 3.f), 
	    new Vector(-Math.sqrt(3) / 3.f, -Math.sqrt(3) / 3.f, Math.sqrt(3) / 3.f), 
	    new Vector( Math.sqrt(3) / 3.f, -Math.sqrt(3) / 3.f, Math.sqrt(3) / 3.f)
	};
	
	private ArrayList<Shape> sceneShapes;
	private ArrayList<BoundingVolume> boundingVolumes;
	
	public BVHAccelerationStructure(ArrayList<Shape> sceneShapes)
	{
		this.sceneShapes = sceneShapes;
		
		this.boundingVolumes = new ArrayList<>();
		
		computeBoundingVolumes();
	}

	private void computeBoundingVolumes()
	{
		for(Shape shape : sceneShapes)
			this.boundingVolumes.add(shape.computeBoundingVolume());
	}
	
	@Override
	public Shape intersect(Ray ray, Point outInterPoint, Vector outNormalAtInter) 
	{
		Shape closestIntersectedObject = null;
		Point closestInterPoint = null;
		Vector normalAtClosestsInterPoint = null;
		
		Double tMin = null;
		
		for(int boundingIndex = 0; boundingIndex < this.boundingVolumes.size(); boundingIndex++)
		{
			BoundingVolume boundingVolume = this.boundingVolumes.get(boundingIndex);
			
			if(boundingVolume == null || boundingVolume.intersect(ray, outNormalAtInter))
			{
				//Si le bounding volume est null, un des objets de la scène a renvoyé null pour son bounding volumes lors de la 
				//construction des bounding volumes de la scène. Cela signifie probablement qu'intersecter l'objet directement
				//est moins coûteux que d'intersecter un bounding volume, on va donc intersecter l'objet directement plutôt
				//que son bounding volume.
				//Si une intersection avec le bounding volume a été trouvée, on récupère l'objet correspondant au bounding volume
				//et on l'intersecte
				Shape tempInterObject = this.sceneShapes.get(boundingIndex);
				Point tempInterPoint = new Point(0, 0, 0);
				Vector tempNormalAtInter = new Vector(0, 0, 0);
				
				Double t = tempInterObject.intersect(ray, tempInterPoint, tempNormalAtInter);
				if(tMin == null || t < tMin)
				{
					tMin = t;
					
					closestInterPoint = tempInterPoint;
					normalAtClosestsInterPoint = tempNormalAtInter;
					closestIntersectedObject = tempInterObject;
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
