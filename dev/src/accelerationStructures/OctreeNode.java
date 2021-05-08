package accelerationStructures;

import java.util.ArrayList;
import java.util.PriorityQueue;

import geometry.Shape;
import materials.Material;
import maths.Point;
import maths.Ray;
import maths.Vector;
import rayTracer.RayTracerStats;

public class OctreeNode 
{
	private int depth;//Profondeur dans l'octree du noeud actuel
	private boolean isLeaf;//Le noeud est une feuille de l'arbre ?
	
	private BoundingVolume nodeVolume;
	private ArrayList<BoundingVolume> boundingVolumes;//Bounding volumes contenant les objets hierarchies par l'octree
	private OctreeNode[] children;
	
	/**
	 * Permet de stocker l'objet intersecte par les rayons. Sans cette classe, nous ne pourrions pas ET retourner le coefficient
	 * t du rayon ET l'objet intersecte. Cette classe est donc utilisee pour contenir l'objet intersecte et agir comme un
	 * pointeur sur l'objet intersecte. On peut donc passer cette classe en
	 * parametre de {@link accelerationStructures.OctreeNode#intersect(Ray, Point, Vector, ObjectContainer)} et ainsi obtenir
	 * l'objet intersecte
	 */
	private final class ObjectContainer
	{
		private Shape containedShape;
		
		public ObjectContainer() 
		{
			this.containedShape = null;
		}
		
		public Shape getContainedShape() 
		{
			return containedShape;
		}
		
		public void setContainedShape(Shape containedShape) 
		{
			this.containedShape = containedShape;
		}
	}
	
	public OctreeNode(int depth)
	{
		this.boundingVolumes = new ArrayList<>();
		this.children = new OctreeNode[8];
		
		this.isLeaf = true;
		this.depth = depth;
	}
	
	/**
	 * Recalcule les dimensions de la bounding box d'un octant
	 * 
	 * @param octantIndex 		Indice de l'octant. Entre 0 et 7
	 * @param currentMinBound	La limite inferieure de la bounding box du noeud courrant	
	 * @param currentMaxBound	La limite superieure de la bounding box du noeud courrant
	 * @param outMinBound		La limite inferieure de la bounding box de l'octant sera placee dans ce parametre
	 * @param outMaxBound		La limite superieure de la bounding box de l'octant sera placee dans ce parametre
	 */
	private void computeNewBounds(int octantIndex, Point currentMinBound, Point currentMaxBound, Point minBound, Point maxBound)
	{
		if((octantIndex & 1) == 0)
		{
			minBound.setZ(currentMinBound.getZ());
			maxBound.setZ((currentMinBound.getZ() + currentMaxBound.getZ()) * 0.5);
		}
		else
		{
			minBound.setZ((currentMinBound.getZ() + currentMaxBound.getZ()) * 0.5);
			maxBound.setZ(currentMaxBound.getZ());
		}
		
		if((octantIndex & 2) == 0)
		{
			minBound.setY(currentMinBound.getY());
			maxBound.setY((currentMinBound.getY() + currentMaxBound.getY()) * 0.5);
		}
		else
		{
			minBound.setY((currentMinBound.getY() + currentMaxBound.getY()) * 0.5);
			maxBound.setY(currentMaxBound.getY());
		}
		
		if((octantIndex & 4) == 0)
		{
			minBound.setX(currentMinBound.getX());
			maxBound.setX((currentMinBound.getX() + currentMaxBound.getX()) * 0.5);
		}
		else
		{
			minBound.setX((currentMinBound.getX() + currentMaxBound.getX()) * 0.5);
			maxBound.setX(currentMaxBound.getX());
		}
	}
	
	public void computeBoundingVolume()
	{
		this.nodeVolume = new BoundingVolume();
		
		if(isLeaf)
		{
			for(BoundingVolume childVolume : this.boundingVolumes)
				this.nodeVolume.extendsBy(childVolume);
		}
		else
		{
			for(OctreeNode child : this.children)
			{
				if(child != null)
				{
					child.computeBoundingVolume();
					this.nodeVolume.extendsBy(child.getBoundingVolume());
				}
			}
		}
	}
	
	public BoundingVolume getBoundingVolume()
	{
		return this.nodeVolume;
	}
	
	public void insert(BoundingVolume volume, Point minNodeBound, Point maxNodeBound, int maxDepth)
	{
		//Un noeud est une feuille s'il n'y pas encore de volume insere dedans où si le noeud se trouve a la profondeur
		//maximale autorisee de l'arbre. Dans ce cas, on va ajouter le volume au noeud
		if(isLeaf)
		{
			//On autorise l'ajout du noeud si le noeud contient moins de 9 volumes ou alors si le noeud est a la profondeur
			//maximale. Dans ce cas, on va juste stacker tous les volumes dans le noeud puisque de toute façon
			//on a pas le droit de construire l'arbre plus loin, on est a la profondeur maximale
			if(this.boundingVolumes.size() <= 7 || this.depth == maxDepth)
				boundingVolumes.add(volume);
			else//S'il y avait deja un volume dans le noeud et qu'on est pas a la profondeur maximale
			{
				//Le noeud n'est plus une feuille
				this.isLeaf = false;
				
				//Maintenant que le noeud n'est plus une feuille, on va reinserer tous les volumes du noeud dans le noeud actuel
				//cela va avoir pour effet de rentrer dans le 'else' (!isLeaf) et donc d'inserer les volumes un niveau plus bas dans 
				//l'arbre, dans les octants qui conviennent 
				int volumesCount = this.boundingVolumes.size();
				while(volumesCount != 0)
				{
					BoundingVolume volumeToInsert = this.boundingVolumes.get(0);
					
					this.insert(volumeToInsert, minNodeBound, maxNodeBound, maxDepth);
					
					this.boundingVolumes.remove(0);
					volumesCount = this.boundingVolumes.size();
				}
				
				this.insert(volume, minNodeBound, maxNodeBound, maxDepth);//On insert egalement le volume actuel qu'on voulait inserer depuis
				//le debut
			}
		}
		//On va inserer le volume dans le bon octant du noeud courant
		else
		{
			Point volumeCentroid = volume.getCentroid();
			Point nodeCentroid = Point.scalarMul(0.5, Point.add(minNodeBound, maxNodeBound));

			//On determine dans quel octant on va placer le volume courant en fonction de la position du centroïde
			//du volume par rapport au centroïde de la bounding box du noeud courant 
			int octantIndex = 0;
			if(volumeCentroid.getX() > nodeCentroid.getX()) octantIndex += 4;
			if(volumeCentroid.getY() > nodeCentroid.getY()) octantIndex += 2;
			if(volumeCentroid.getZ() > nodeCentroid.getZ()) octantIndex += 1;
			
			//Recalcul des limites inferieures et superieures du nouvel octant
			Point newMinBounds = new Point(0, 0, 0);
			Point newMaxBounds = new Point(0, 0, 0);
			computeNewBounds(octantIndex, minNodeBound, maxNodeBound, newMinBounds, newMaxBounds);
			
			if(this.children[octantIndex] == null)//Si l'octant n'a pas deja ete cree
				this.children[octantIndex] = new OctreeNode(depth + 1);//On le cree
			this.children[octantIndex].insert(volume, newMinBounds, newMaxBounds, maxDepth);//Et on ajoute le volume a l'octant
		}
	}
	
	public Shape intersect(RayTracerStats interStats, ArrayList<Shape> noVolumeShapes, Ray ray, Point outInterPoint, Vector outNormalAtInter)
	{
		ObjectContainer intersectedObject = new ObjectContainer();
		
		//On intersecte d'abord toutes les formes de la hierarchie
		Double tMin = this.intersect(interStats, ray, outInterPoint, outNormalAtInter, intersectedObject, new PriorityQueue<PriorityNode>());
		
		//Puis on intersecte les formes qui n'ont pas de bounding volume
		for(Shape shape : noVolumeShapes)
		{
			Point tempInterPoint = new Point(0, 0, 0);
			Vector tempNormalInter = new Vector(0, 0, 0);
			
			interStats.incrementIntersectionTestsDone();
			Double t = shape.intersect(ray, tempInterPoint, tempNormalInter);
			if(t != null)
			{
				if(tMin == null || t < tMin)
				{
					tMin = t;
					
					intersectedObject.setContainedShape(shape);
					outInterPoint.copyIn(tempInterPoint);
					outNormalAtInter.copyIn(tempNormalInter);
				}
			}
		}
		
		return intersectedObject.getContainedShape();
	}
	
	private Double intersect(RayTracerStats interStats, Ray ray, Point outInterPoint, Vector outNormalAtInter, 
							 ObjectContainer outIntersectedObject, PriorityQueue<PriorityNode> distanceQueue)
	{
		Double tMin = null;
		
		if(this.nodeVolume.intersect(ray) == null)
			return null;
		
		distanceQueue.add(new PriorityNode(this, 0));
		
		while(distanceQueue.size() > 0 && (tMin == null || tMin > distanceQueue.peek().getTDistance()))
		{
			OctreeNode nodeToIntersect = distanceQueue.poll().getNode();
			
			if(nodeToIntersect.isLeaf)
			{
				for(BoundingVolume volume : nodeToIntersect.boundingVolumes)
				{
					if(volume.intersect(ray) != null)
					{
						Point tempInterPoint = new Point(0, 0, 0);
						Vector tempNormalAtInter = new Vector(0, 0, 0);
						
						interStats.incrementIntersectionTestsBy(volume.getEnclosedObject().getSubObjectCount());
						Double t = volume.getEnclosedObject().intersect(ray, tempInterPoint, tempNormalAtInter);
						
						if(t != null)//On a trouve une intersection
						{
							if(tMin == null || tMin > t)
							{
								tMin = t;
								
								outIntersectedObject.setContainedShape(volume.getEnclosedObject());
								outInterPoint.copyIn(tempInterPoint);
								outNormalAtInter.copyIn(tempNormalAtInter);
							}
						}
					}
				}
			}
			else
			{
				for(OctreeNode child : nodeToIntersect.children)
				{
					if(child != null)
					{
						Double[] t = child.getBoundingVolume().intersect(ray);
						if(t != null)
						{
							Double nearestT = (t[0] < 0 && t[1] >= 0) ? t[1] : t[0];
							
							distanceQueue.add(new PriorityNode(child, nearestT));
						}
					}
				}
			}
		}
		
		return tMin;
			
	}
}
