package accelerationStructures;

import java.util.ArrayList;

import maths.Point;

public class OctreeNode 
{
	private int depth;//Profondeur dans l'octree du noeud actuel
	private boolean isLeaf;//Le noeud est une feuille de l'arbre ?
	
	private Point centroid;
	
	private BoundingVolume nodeVolume;
	private ArrayList<BoundingVolume> boundingVolumes;
	private ArrayList<OctreeNode> children;
	
	public OctreeNode(int depth)
	{
		this.boundingVolumes = new ArrayList<>();
		this.children = new ArrayList<>();
		
		this.isLeaf = true;
		this.depth = depth;
	}
	
//	private void computeCentroid()
//	{
//		this.centroid = Point.scalarMul(0.5, Point.add(no.getBounds(0), bounds.getBounds(1)));
//	}
	
	public void insert(BoundingVolume volume, Point minNodeBound, Point maxNodeBound, int depth)
	{
		if(isLeaf)
		{
			
		}
	}
}
