package accelerationStructures;

import geometry.Shape;
import maths.Point;
import maths.Ray;
import maths.Vector;

/**
 * Represente le volume constitue de 7 paires de plans paralleles encadrant au mieux un objet de la scene donne
 * Peut etre vu comme une BoundingBox plus evoluee
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
	 * Cree un bounding volume mais ne definit pas ses limites.
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
	 * Etend le bounding volume courant en fonction de celui passe en parametre. Si le bounding volume passe en parametre est plus
	 * 'gros' que le bouding volume courant, le volume courant sera etendu et sera, ares l'appel a la fonction, au moins
	 * aussi gros que 'extender'
	 * 
	 * @param extender Le volume qui va tenter d'etendre le volume courant (this)
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
	 * @return Le point de coordonnees (x, y, z) representant le centroïde du volume
	 */
	public Point getCentroid()
	{
		return new Point((this.dMin[0] + this.dMax[0]) / 2, (this.dMin[1] + this.dMax[1]) / 2,(this.dMin[2] + this.dMax[2]) / 2);
	}
	
	/**
	 * Permet d'obtenir le parametre dMin d'une paire de plan representant le BoundingVolume
	 * 
	 * @param index L'indice de la paire de plan dont le parametre d est souhaite. Entier entre 0 et 7
	 * 
	 * @return Le parametre dMin de la paire de plan numero 'index'
	 */
	public double getDMin(int index)
	{
		return this.dMin[index];
	}
	
	/**
	 * Permet d'obtenir le parametre dMax d'une paire de plan representant le BoundingVolume
	 * 
	 * @param index L'indice de la paire de plan dont le parametre d est souhaite. Entier entre 0 et 7
	 * 
	 * @return Le parametre dMax de la paire de plan numero 'index'
	 */
	public double getDMax(int index)
	{
		return this.dMax[index];
	}
	
	/**
	 * @return L'objet encadre par le bounding volume
	 */
	public Shape getEnclosedObject()
	{
		return this.enclosedObject;
	}
	
	/**
	 * Determine si le rayon 'ray' passe en parametre intersecte le BoundingVolume ou non
	 * 
	 * @param ray Le rayon qui doit etre teste contre le bounding volume
	 * 
	 * @return Le tableau contenant les distances entre l'origine du rayon et les deux points d'intersection avec le volume.
	 * [0] contient le point d'intersection le négatif (en coordonnées), [1] le plus positif.
	 * null s'il n'y a pas eu d'intersection
	 */
	public Double[] intersect(Ray ray)
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
				return null;
		}
		
		return new Double[] {tNearIntersect, tFarIntersect};
	}
	
	/**
	 * Redefini l'objet encadre par le bounding volume
	 * 
	 * @param enclosedObject L'objet encadre par le bounding volume
	 */
	public void setEnclosedObject(Shape enclosedObject)
	{
		this.enclosedObject = enclosedObject;
	}
	
	/**
	 * Permet de redefinir les parametres dMin et dMax d'une paire de plan du bouding volume
	 * 
	 * @param near 	Le nouveau parametre de dMin
	 * @param far 	Le nouveau parametre de dMax
	 * @param index	Le numero de la paire de plan dont les dMin dMax vont etre redefinis. Entier entre 0 et 7
	 */
	public void setBounds(double near, double far, int index)
	{
		this.dMin[index] = near;
		this.dMax[index] = far;
	}
}
