				
package accelerationStructures;

import java.util.ArrayList;

import geometry.Shape;
import geometry.shapes.BoundingBox;

public class Octree 
{
	private OctreeNode root;
	private ArrayList<Shape> shapes;

	private BoundingVolume boundingVolume;
	private BoundingBox boundingBox;
	
	public Octree(ArrayList<Shape> shapes)
	{
		this.boundingBox = new BoundingBox();
		
		this.shapes = shapes;
		
		computeBounds();
	}
	
	private void computeBounds() 
	{
		for()
	}
}
