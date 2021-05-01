package accelerationStructures;

import geometry.Shape;
import maths.Point;
import maths.Ray;
import maths.Vector;

/**
 * Représente le volume constitué de 7 paires de plans parallèles encadrant au mieux un objet de la scène donné
 * Peut être vu comme une BoundingBox plus évoluée
 */
public class BoundingVolume 
{
	private double[] dNear;
	private double[] dFar;
	
	private Shape enclosedObject;
	
	public BoundingVolume(Shape enclosedObject)
	{
		this.dNear = new double[BVHAccelerationStructure.PLANE_SET_NORMAL_COUNT];
		this.dFar = new double[BVHAccelerationStructure.PLANE_SET_NORMAL_COUNT];
		
		this.enclosedObject = enclosedObject;
	}
	
	/**
	 * Etend le bounding volume courant en fonction de celui passé en paramètre. Si le bounding volume passé en paramètre est plus
	 * 'gros' que le bouding volume courant, le volume courant sera étendu et sera, arès l'appel à la fonction, au moins
	 * aussi gros que 'extender'
	 * 
	 * @param extender Le volume qui va tenter d'étendre le volume courant (this)
	 */
	public void extendsBy(BoundingVolume extender)
	{
		for(int i = 0; i < BVHAccelerationStructure.PLANE_SET_NORMAL_COUNT; i++)
		{
			if(extender.getDNear(i) < this.getDNear(i)) this.dNear[i] = extender.getDNear(i);
			if(extender.getDFar(i) > this.getDFar(i)) this.dFar[i] = extender.getDFar(i);
		}
	}

	/**
	 * @return Le point de coordonnées (x, y, z) représentant le centroïde du volume
	 */
	public Point getCentroid()
	{
		return new Point((this.dNear[0] + this.dFar[0]) / 2, (this.dNear[1] + this.dFar[1]) / 2,(this.dNear[2] + this.dFar[2]) / 2);
	}
	
	/**
	 * Permet d'obtenir le paramètre dNear d'une paire de plan représentant le BoundingVolume
	 * 
	 * @param index L'indice de la paire de plan dont le paramètre d est souhaité. Entier entre 0 et 7
	 * 
	 * @return Le paramètre dNear de la paire de plan numéro 'index'
	 */
	public double getDNear(int index)
	{
		return this.dNear[index];
	}
	
	/**
	 * Permet d'obtenir le paramètre dFar d'une paire de plan représentant le BoundingVolume
	 * 
	 * @param index L'indice de la paire de plan dont le paramètre d est souhaité. Entier entre 0 et 7
	 * 
	 * @return Le paramètre dFar de la paire de plan numéro 'index'
	 */
	public double getDFar(int index)
	{
		return this.dFar[index];
	}
	
	/**
	 * @return L'objet encadré par le bounding volume
	 */
	public Shape getEnclosedObject()
	{
		return this.enclosedObject;
	}
	
	/**
	 * Détermine si le rayon 'ray' passé en paramètre intersecte le BoundingVolume ou non
	 * 
	 * @param ray Le rayon qui doit être testé contre le bounding volume
	 * 
	 * @return True si le rayon a au moins une intersection avec le bounding volume, false sinon
	 */
	public boolean intersect(Ray ray)
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
	
	/**
	 * Permet de redéfinir les paramètres dNear et dFar d'une paire de plan du bouding volume
	 * 
	 * @param near 	Le nouveau paramètre de dNear
	 * @param far 	Le nouveau paramètre de dFar
	 * @param index	Le numéro de la paire de plan dont les dNear dFar vont être redéfinis. Entier entre 0 et 7
	 */
	public void setBounds(double near, double far, int index)
	{
		this.dNear[index] = near;
		this.dFar[index] = far;
	}
}
