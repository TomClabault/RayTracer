package accelerationStructures.BVH;

import maths.Ray;
import maths.Vector;

public class BoundingVolume 
{
	private double[] dNear;
	private double[] dFar;
	
	private Double tNearIntersect;
	private Double tFarIntersect;
	
	public BoundingVolume()
	{
		this.dNear = new double[BVHAccelerationStructure.PLANE_SET_NORMAL_COUNT];
		this.dFar = new double[BVHAccelerationStructure.PLANE_SET_NORMAL_COUNT];
		
		this.tNearIntersect = null;
		this.tFarIntersect = null;
	}
	
	public boolean intersect(Ray ray, Vector outNormalAtInter)
	{
		this.tNearIntersect = null;
		this.tFarIntersect = null;
		
		for(int i = 0; i < BVHAccelerationStructure.PLANE_SET_NORMAL_COUNT; i++)
		{
			double normalDotOrigin = Vector.dotProduct(BVHAccelerationStructure.PLANE_SET_NORMALS[i], ray.getOrigin().toVector());
			double normalDotDirection = Vector.dotProduct(BVHAccelerationStructure.PLANE_SET_NORMALS[i], ray.getDirection());
			
			double currentDNear = this.dNear[i];
			double currentDFar = this.dFar[i];
			
			if(normalDotDirection < 0)
			{
				double temp = currentDNear;
				currentDNear = currentDFar;
				currentDFar = temp;
			}
			
			double tNear = (this.dNear[i] - normalDotOrigin) / normalDotDirection; 
			double tFar= (this.dFar[i] - normalDotOrigin) / normalDotDirection;
			
			if(this.tNearIntersect <  tNear) this.tNearIntersect = tNear;
			if(this.tFarIntersect > tFar) this.tFarIntersect = tFar;
			if(this.tNearIntersect > this.tFarIntersect)
			{
				this.tNearIntersect = null;
				this.tFarIntersect = null;
				
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * @return Le coefficient t du rayon correspondant à la deuxième intersection d'un rayon avec ce BoundingVolume.<br>
	 * La méthode intersect doit avoir préalablement été appelée. Dans le cas contraire, cette méthode retourne null.
	 */
	public Double getTFar()
	{
		return this.tFarIntersect;
	}
	
	/**
	 * @return Le coefficient t du rayon correspondant à la première intersection d'un rayon avec ce BoundingVolume.<br>
	 * La méthode intersect doit avoir préalablement été appelée. Dans le cas contraire, cette méthode retourne null.
	 */
	public Double getTNear()
	{
		return this.tNearIntersect;
	}
	
	public void setBounds(double near, double far, int index)
	{
		this.dNear[index] = near;
		this.dFar[index] = far;
	}
}
