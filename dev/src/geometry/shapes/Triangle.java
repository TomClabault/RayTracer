package geometry.shapes;

import accelerationStructures.BVHAccelerationStructure;
import accelerationStructures.BoundingBox;
import accelerationStructures.BoundingVolume;
import geometry.Shape;
import geometry.shapes.strategies.TriangleIntersectionStrategy;
import geometry.shapes.strategies.TriangleMollerTrumboreStrategy;
import geometry.shapes.strategies.TriangleNaiveStrategy;
import materials.Material;
import maths.Point;
import maths.Ray;
import maths.Vector;

public class Triangle implements Shape
{
	private Point A, B, C;
	private Vector planeNormal;//Vecteur normal du plan forme par les 3 points du triangle

	private Material material;
	private TriangleIntersectionStrategy interStrategy;
	
	public Triangle(Point A, Point B, Point C, Material material)
	{
		this(A, B, C, material, new TriangleNaiveStrategy());
	}
	
	public Triangle(Point A, Point B, Point C, Material material, TriangleIntersectionStrategy intersectionStrategy)
	{
		this.A = A;
		this.B = B;
		this.C = C;
		this.planeNormal = Vector.normalizeV(Vector.crossProduct(new Vector(A, B), new Vector(A, C)));
		
		this.material = material;
		this.interStrategy = intersectionStrategy;
	}

	/**
	 * {@link geometry.Shape#getBoundingBox()}
	 */
	@Override
	public BoundingBox getBoundingBox() 
	{
		Point minPoint = new Point(Math.min(A.getX(), Math.min(B.getX(), C.getX())), 
								   Math.min(A.getY(), Math.min(B.getY(), C.getY())), 
								   Math.min(A.getZ(), Math.min(B.getZ(), C.getZ())));
		Point maxPoint = new Point(Math.max(A.getX(), Math.max(B.getX(), C.getX())), 
				   				   Math.max(A.getY(), Math.max(B.getY(), C.getY())), 
				   				   Math.max(A.getZ(), Math.max(B.getZ(), C.getZ())));
		
		return new BoundingBox(minPoint, maxPoint);
	}
	
	/**
	 * {@link geometry.Shape#getBoundingVolume()}
	 */
	public BoundingVolume getBoundingVolume()
	{
		BoundingVolume volume = new BoundingVolume();
		
		for(int i = 0; i < BoundingVolume.PLANE_SET_NORMAL_COUNT; i++)
		{
			double dMin = Double.POSITIVE_INFINITY;
			double dMax = Double.NEGATIVE_INFINITY;
			
			for(int vertexIndex = 0; vertexIndex < 3; vertexIndex++)
			{
				Point vertex = vertexIndex == 0 ? this.getA() : vertexIndex == 1 ? this.getB() : this.getC();
				
				double d = Vector.dotProduct(BoundingVolume.PLANE_SET_NORMALS[i], vertex.toVector());
				
				dMin = Math.min(dMin, d);
				dMax = Math.max(dMax, d);
			}
			
			volume.setBounds(dMin, dMax, i);
		}
		volume.setEnclosedObject(this);
		
		return volume;
	}
	
	public Point getA() {return this.A;}
	public Point getB() {return this.B;}
	public Point getC() {return this.C;}

	public Vector getNormal(Point point)
	{
		return this.planeNormal;
	}
		
	/**
	 * Permet de redefinir le point A du triangle
	 * 
	 * @param A Le nouveau point A du triangle
	 */
	public void setA(Point A)
	{
		this.A = A;
	}
	
	/**
	 * Analogue a @link{geometry.shapes.Triangle#setA}
	 */
	public void setB(Point B)
	{
		this.B = B;
	}
	
	/**
	 * Analogue a @link{geometry.shapes.Triangle#setA}
	 */
	public void setC(Point C)
	{
		this.C = C;
	}

	/**
	 * {@link geometry.Shape#getMaterial()}
	 */
	@Override
	public Material getMaterial() 
	{
		return this.material;
	}

	/**
	 * {@link geometry.Shape#getSubObjectCount()}
	 */
	@Override
	public int getSubObjectCount() 
	{
		return 1;
	}
	
	/**
	 * {@link geometry.Shape#getUVCoords(Point)}
	 */
	@Override
	public Point getUVCoords(Point point) { return null; }

	/**
	 * {@link geometry.Shape#intersect(Ray, Point, Vector)}
	 */
	@Override
	public Double intersect(Ray ray, Point outInterPoint, Vector outNormalAtInter) 
	{
		return this.interStrategy.intersect(this, ray, outInterPoint, outNormalAtInter);
	}

	/**
	 * {@link geometry.Shape#setMaterial(Material)}
	 */
	@Override
	public void setMaterial(Material newMaterial) 
	{
		this.material = newMaterial;
	}
	
	public String toString()
	{
		return String.format("Shape triangle: A = %s | B = %s | C = %s", A, B, C);
	}
}
