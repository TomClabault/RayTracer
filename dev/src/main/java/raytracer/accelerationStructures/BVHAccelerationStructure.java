package raytracer.accelerationStructures;

import java.util.ArrayList;

import raytracer.geometry.Shape;
import raytracer.maths.Point;
import raytracer.maths.Ray;
import raytracer.maths.Vector;
import raytracer.rayTracer.RayTracerStats;

public class BVHAccelerationStructure implements AccelerationStructure 
{
	private ArrayList<Shape> sceneShapes;
	private ArrayList<Shape> noVolumeShapes;//Liste des formes qui n'ont pas de bounding volume
	private ArrayList<BoundingVolume> boundingVolumes;
	
	private Octree octree;
	
	public BVHAccelerationStructure(ArrayList<Shape> sceneShapes, int maxDepth)
	{
		this.sceneShapes = sceneShapes;
		
		this.noVolumeShapes = new ArrayList<>();
		this.boundingVolumes = new ArrayList<>();
		
		constructBoundingVolumes();
		
		this.octree = new Octree(this.noVolumeShapes, this.boundingVolumes, maxDepth);
	}

	private void constructBoundingVolumes()
	{
		for(Shape shape : sceneShapes)
		{
			BoundingVolume volume = shape.getBoundingVolume();
			
			if(volume != null)
				this.boundingVolumes.add(volume);
			else
				this.noVolumeShapes.add(shape);
		}
	}
	
	@Override
	public Shape intersect(RayTracerStats interStats, Ray ray, Point outInterPoint, Vector outNormalAtInter) 
	{
		return this.octree.intersect(interStats, ray, outInterPoint, outNormalAtInter);
	}
}
