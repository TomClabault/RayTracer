package accelerationStructures;

import maths.Ray;
import maths.Vector;

public class BoundingVolume 
{
	private double[] dNear;
	private double[] dFar;
	
	public BoundingVolume()
	{
		this.dNear = new double[BVHAccelerationStructure.PLANE_SET_NORMAL_COUNT];
		this.dFar = new double[BVHAccelerationStructure.PLANE_SET_NORMAL_COUNT];
	}
	
	public boolean intersect(Ray ray, Vector outNormalAtInter)
	{
		Double tNearIntersect = null;
		Double tFarIntersect = null;
		
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
			
			double tNear = (currentDNear - normalDotOrigin) / normalDotDirection; 
			double tFar= (currentDFar - normalDotOrigin) / normalDotDirection;
			
			if(tNearIntersect == null || tNearIntersect < tNear) 
				tNearIntersect = tNear;
			if(tFarIntersect == null || tFarIntersect > tFar) 
				tFarIntersect = tFar;
			
			if(tNearIntersect > tFarIntersect)
			{
				tNearIntersect = null;
				tFarIntersect = null;
				
				return false;
			}
		}
		
		return true;
	}
	
	public void setBounds(double near, double far, int index)
	{
		this.dNear[index] = near;
		this.dFar[index] = far;
	}
}
