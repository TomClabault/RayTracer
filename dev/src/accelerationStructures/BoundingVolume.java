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
	public static final int PLANE_SET_NORMAL_COUNT = 7;
	
	public static final Vector[] PLANE_SET_NORMALS = new Vector[] 
	{
		new Vector(1, 0, 0),
		new Vector(0, 1, 0),
		new Vector(0, 0, 1),
		new Vector( Math.sqrt(3) / 3,  Math.sqrt(3) / 3, Math.sqrt(3) / 3), 
	    new Vector(-Math.sqrt(3) / 3,  Math.sqrt(3) / 3, Math.sqrt(3) / 3), 
	    new Vector(-Math.sqrt(3) / 3, -Math.sqrt(3) / 3, Math.sqrt(3) / 3), 
	    new Vector( Math.sqrt(3) / 3, -Math.sqrt(3) / 3, Math.sqrt(3) / 3)
	};
	
	private double[] dMin;
	private double[] dMax;
	
	private Shape enclosedObject;
	
	/**
	 * Crée un bounding volume mais ne définit pas ses limites.
	 */
	public BoundingVolume()
	{
		this.dMin = new double[BoundingVolume.PLANE_SET_NORMAL_COUNT];
		this.dMax = new double[BoundingVolume.PLANE_SET_NORMAL_COUNT];
		
		for(int i = 0; i < BoundingVolume.PLANE_SET_NORMAL_COUNT; i++)
		{
			this.dMin[i] = Double.POSITIVE_INFINITY;
			this.dMax[i] = Double.NEGATIVE_INFINITY;
		}
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
		for(int i = 0; i < BoundingVolume.PLANE_SET_NORMAL_COUNT; i++)
		{
			if(extender.getDMin(i) < this.getDMin(i)) this.dMin[i] = extender.getDMin(i);
			if(extender.getDMax(i) > this.getDMax(i)) this.dMax[i] = extender.getDMax(i);
		}
	}

	/**
	 * @return Le point de coordonnées (x, y, z) représentant le centroïde du volume
	 */
	public Point getCentroid()
	{
		return new Point((this.dMin[0] + this.dMax[0]) / 2, (this.dMin[1] + this.dMax[1]) / 2,(this.dMin[2] + this.dMax[2]) / 2);
	}
	
	/**
	 * Permet d'obtenir le paramètre dMin d'une paire de plan représentant le BoundingVolume
	 * 
	 * @param index L'indice de la paire de plan dont le paramètre d est souhaité. Entier entre 0 et 7
	 * 
	 * @return Le paramètre dMin de la paire de plan numéro 'index'
	 */
	public double getDMin(int index)
	{
		return this.dMin[index];
	}
	
	/**
	 * Permet d'obtenir le paramètre dMax d'une paire de plan représentant le BoundingVolume
	 * 
	 * @param index L'indice de la paire de plan dont le paramètre d est souhaité. Entier entre 0 et 7
	 * 
	 * @return Le paramètre dMax de la paire de plan numéro 'index'
	 */
	public double getDMax(int index)
	{
		return this.dMax[index];
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
		
		for(int i = 0; i < BoundingVolume.PLANE_SET_NORMAL_COUNT; i++)
		{
			double normalDotOrigin = Vector.dotProduct(BoundingVolume.PLANE_SET_NORMALS[i], ray.getOrigin().toVector());
			double normalDotDirection = Vector.dotProduct(BoundingVolume.PLANE_SET_NORMALS[i], ray.getDirection());
			
			double currentdMin = this.dMin[i];
			double currentDMax = this.dMax[i];
			
			if(normalDotDirection < 0)
			{
				double temp = currentdMin;
				currentdMin = currentDMax;
				currentDMax = temp;
			}
			
			double tNear = (currentdMin - normalDotOrigin) / normalDotDirection; 
			double tFar= (currentDMax - normalDotOrigin) / normalDotDirection;
			
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
	 * Redéfini l'objet encadré par le bounding volume
	 * 
	 * @param enclosedObject L'objet encadré par le bounding volume
	 */
	public void setEnclosedObject(Shape enclosedObject)
	{
		this.enclosedObject = enclosedObject;
	}
	
	/**
	 * Permet de redéfinir les paramètres dMin et dMax d'une paire de plan du bouding volume
	 * 
	 * @param near 	Le nouveau paramètre de dMin
	 * @param far 	Le nouveau paramètre de dMax
	 * @param index	Le numéro de la paire de plan dont les dMin dMax vont être redéfinis. Entier entre 0 et 7
	 */
	public void setBounds(double near, double far, int index)
	{
		this.dMin[index] = near;
		this.dMax[index] = far;
	}
}
