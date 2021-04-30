package geometry.shapes;

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
	private Vector planeNormal;//Vecteur normal du plan formé par les 3 points du triangle

	private Material material;
	private TriangleIntersectionStrategy interStrategy;
	
	public Triangle(Point A, Point B, Point C, Material material)
	{
		this(A, B, C, material, new TriangleMollerTrumboreStrategy());
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

	public Point getA() {return this.A;}
	public Point getB() {return this.B;}
	public Point getC() {return this.C;}

	public Vector getNormal(Point point)
	{
		return this.planeNormal;
	}
		
	/**
	 * Permet de redéfinir le point A du triangle
	 * 
	 * @param A Le nouveau point A du triangle
	 */
	public void setA(Point A)
	{
		this.A = A;
	}
	
	/**
	 * Analogue à @link{geometry.shapes.Triangle#setA}
	 */
	public void setB(Point B)
	{
		this.B = B;
	}
	
	/**
	 * Analogue à @link{geometry.shapes.Triangle#setA}
	 */
	public void setC(Point C)
	{
		this.C = C;
	}
	
	public String toString()
	{
		return String.format("Shape triangle: A = %s | B = %s | C = %s", A, B, C);
	}

	@Override
	public Material getMaterial() 
	{
		return this.material;
	}

	@Override
	public Point getUVCoords(Point point) { return null; }

	@Override
	public Point intersect(Ray ray, Vector outNormalAtInter) 
	{
		return this.interStrategy.intersect(this, ray, outNormalAtInter);
	}

	@Override
	public void setMaterial(Material newMaterial) 
	{
		this.material = newMaterial;
	}
}
