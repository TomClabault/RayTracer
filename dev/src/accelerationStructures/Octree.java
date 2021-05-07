package accelerationStructures;

import java.util.ArrayList;

import geometry.Shape;
import geometry.shapes.Parallelepiped;
import javafx.scene.paint.Color;
import materials.MatteMaterial;
import maths.Point;
import maths.Ray;
import maths.Vector;
import rayTracer.RayTracerStats;

public class Octree 
{
	private OctreeNode root;
	private ArrayList<BoundingVolume> shapesVolumes;
	private ArrayList<Shape> noVolumeShapes;//Shapes qui n'ont pas de bounding volume et doivent etre traitees specifiquement
	//par la strucutre. Les plans par exemple sont infinis, ils n'ont pas de bounding volume. Ces formes ne seront
	//pas inserees dans la hierarchie
	
	private BoundingBox bounds;
	
	public Octree(ArrayList<Shape> noVolumeShapes, ArrayList<BoundingVolume> sceneObjectsVolumes, int maxDepth)
	{
		this.shapesVolumes = sceneObjectsVolumes;
		this.noVolumeShapes = noVolumeShapes;
		
		this.bounds = new BoundingBox(new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
		this.root = new OctreeNode(0);//Racine = nouveau noeud de profondeur 0
		
		computeOctreeBoundingBox();
		squareifyBoundingBox();
		
		//Ajout des bounding volumes a la hierarchie
		for(int i = 0; i < shapesVolumes.size(); i++)
			root.insert(sceneObjectsVolumes.get(i), bounds.getBounds(0), bounds.getBounds(1), maxDepth);
		
		this.computeNodesBoundingVolumes();
	}
	
	private void computeNodesBoundingVolumes()
	{
		root.computeBoundingVolume();
	}
	
	private void computeOctreeBoundingBox() 
	{
		for(BoundingVolume volume : this.shapesVolumes)
			this.bounds.extendBy(volume);
	}
	
	public Shape intersect(RayTracerStats interStats, Ray ray, Point outInterPoint, Vector outNormalAtInter)
	{
		Shape intersectedObject = null;
		
		intersectedObject = root.intersect(interStats, noVolumeShapes, ray, outInterPoint, outNormalAtInter);
		
		return intersectedObject;
	}
	
	private void squareifyBoundingBox()
	{
		//Determine quelle dimension de la bounding box est la plus grande. C'est a partie de cette plus grande dimension qu'on
		//pourra faire le cube de l'octree, la bounding box de l'octree
		double lengthX = this.bounds.getBounds(1).getX() - this.bounds.getBounds(0).getX();
		double lengthY = this.bounds.getBounds(1).getY() - this.bounds.getBounds(0).getY();
		double lengthZ = this.bounds.getBounds(1).getZ() - this.bounds.getBounds(0).getZ();
		double maxLength = Math.max(lengthX, Math.max(lengthY, lengthZ));
		
		//De combien la bounding box a ete aggrandie en X, Y et Z. On va vouloir recentrer le cube autour de tous les objets
		Point miniMaxi = Point.add(this.bounds.getBounds(0), this.bounds.getBounds(1));
		Point maxLengthPoint = new Point(maxLength, maxLength, maxLength);
		
		this.bounds.setBound(Point.scalarMul(0.5, Point.sub(miniMaxi, maxLengthPoint)), 0);
		this.bounds.setBound(Point.scalarMul(0.5, Point.add(miniMaxi, maxLengthPoint)), 1);
	}
}
