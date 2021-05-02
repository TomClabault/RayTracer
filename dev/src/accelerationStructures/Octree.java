				
package accelerationStructures;

import java.util.ArrayList;

import geometry.Shape;
import maths.Point;

public class Octree 
{
	private OctreeNode root;
	private ArrayList<Shape> shapes;

	private Point centroid;
	private BoundingVolume boundingVolume;
	private BoundingBox bounds;
	
	public Octree(ArrayList<Shape> shapes)
	{
		this.shapes = shapes;
		this.bounds = new BoundingBox(new Point(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE), new Point(Double.MIN_VALUE, Double.MIN_VALUE, Double.MIN_VALUE));
		
		computeShapesBoundingBox();
		squareifyBoundingBox();
		computeCentroid();
	}
	
	private void computeCentroid()
	{
		this.centroid = Point.scalarMul(0.5, Point.add(bounds.getBounds(0), bounds.getBounds(1)));
	}
	
	private void computeShapesBoundingBox() 
	{
		for(Shape shape : shapes)
		{
			BoundingBox shapeBoundingBox = shape.getBoundingBox();
			if(shapeBoundingBox != null)
				this.bounds.extendBy(shapeBoundingBox);
		}
	}
	
	private void squareifyBoundingBox()
	{
		//Détermine quelle dimension de la bounding box est la plus grande. C'est à partie de cette plus grande dimension qu'on
		//pourra faire le cube de l'octree, la bounding box de l'octree
		double lengthX = this.bounds.getBounds(1).getX() - this.bounds.getBounds(0).getX();
		double lengthY = this.bounds.getBounds(1).getY() - this.bounds.getBounds(0).getY();
		double lengthZ = this.bounds.getBounds(1).getZ() - this.bounds.getBounds(0).getZ();
		double maxLength = Math.max(lengthX, Math.max(lengthY, lengthZ));
		
		//De combien la bounding box a été aggrandie en X, Y et Z. On va vouloir recentrer le cube autour de tous les objets
		double diffX = maxLength - lengthX;
		double diffY = maxLength - lengthY;
		double diffZ = maxLength - lengthZ;

		//En déplaçant le cube négativement de diffX/2, diffY/2 et diffZ/2, on va recentrer le cube autour de tous les objets
		///le cube n'était jusqu'alors pas centré
		Point diffPoint = new Point(-diffX/2, -diffY/2, -diffZ/2);
		
		Point bound0 = this.bounds.getBounds(0);
		Point bound1 = this.bounds.getBounds(1);
		
		this.bounds.setBound(Point.add(bound0, diffPoint), 0);
		this.bounds.setBound(Point.add(bound1, diffPoint), 1);
	}
}
