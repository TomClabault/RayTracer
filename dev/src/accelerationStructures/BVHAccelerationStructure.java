package accelerationStructures;

import java.util.ArrayList;

import geometry.ArbitraryTriangleShape;
import geometry.Shape;
import maths.Point;
import maths.Ray;
import maths.Vector;
import rayTracer.RayTracerStats;
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
		
		constructBoundingVolumes();
	}

	private void constructBoundingVolumes()
	{
		for(Shape shape : sceneShapes)
			this.boundingVolumes.add(shape.computeBoundingVolume());
	}
	
	@Override
	public Shape intersect(RayTracerStats interStats, Ray ray, Point outInterPoint, Vector outNormalAtInter) 
	{
		Shape closestIntersectedObject = null;
		Point closestInterPoint = null;
		Vector normalAtClosestsInterPoint = null;
		
		Double tMin = null;
		
		//TODO (tom) compter le nombre d'intersections calculées
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
				Shape tempInterObject = boundingVolume == null ? this.sceneShapes.get(boundingIndex) : boundingVolume.getEnclosedObject();
				Point tempInterPoint = new Point(0, 0, 0);
				Vector tempNormalAtInter = new Vector(0, 0, 0);
				
				Double t = tempInterObject.intersect(ray, tempInterPoint, tempNormalAtInter);
				if(tempInterObject instanceof ArbitraryTriangleShape)//Si l'objet est une composition de triangles
					//On augmente le nombre d'intersection testées par le nombre de triangle de l'objet
					interStats.incrementIntersectionTestsBy(((ArbitraryTriangleShape)tempInterObject).getTriangleList().size());
				else
					//Sinon on augment de 1 puisqu'on ne vas tester l'intersection qu'avec l'objet lui même, 1 seul objet
					interStats.incrementIntersectionTestsDone();
				
				if(t != null)//Si on a trouvé une intersection
				{
					if(tMin == null || t < tMin)
					{
						tMin = t;
						
						closestInterPoint = tempInterPoint;
						normalAtClosestsInterPoint = tempNormalAtInter;
						closestIntersectedObject = tempInterObject;
					}
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
