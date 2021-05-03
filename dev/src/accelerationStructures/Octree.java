package accelerationStructures;

import java.util.ArrayList;

import geometry.ObjectContainer;
import geometry.Shape;
import maths.Point;
import maths.Ray;
import maths.Vector;
import rayTracer.RayTracerStats;

public class Octree 
{
	private OctreeNode root;
	private ArrayList<BoundingVolume> shapesVolumes;
	private ArrayList<Shape> noVolumeShapes;//Shapes qui n'ont pas de bounding volume et doivent être traitées spécifiquement
	//par la strucutre. Les plans par exemple sont infinis, ils n'ont pas de bounding volume. Ces formes ne seront
	//pas insérées dans la hiérarchie
	
	private BoundingBox bounds;
	
	public Octree(ArrayList<Shape> noVolumeShapes, ArrayList<BoundingVolume> sceneObjectsVolumes, int maxDepth)
	{
		this.shapesVolumes = sceneObjectsVolumes;
		this.noVolumeShapes = noVolumeShapes;
		
		this.bounds = new BoundingBox(new Point(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY), new Point(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
		this.root = new OctreeNode(0);//Racine = nouveau noeud de profondeur 0
		
		computeOctreeBoundingBox();
		squareifyBoundingBox();
		
		//Ajout des bounding volumes à la hiérarchie
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
	
	public Double intersect(RayTracerStats interStats, Ray ray, Point outInterPoint, Vector outNormalAtInter, ObjectContainer objectContainer)
	{
		Shape intersectedObject = null;
		
		Double t = root.intersect(interStats, noVolumeShapes, ray, outInterPoint, outNormalAtInter, objectContainer);
		
		return t;
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
