package scene.lights;

import maths.Point;

public interface PositionnalLight 
{
	/**
	 * Permet d'obtenir la position de la lumiere
	 * 
	 *  @return Retourne le point de coordonnees (x, y, z) representant le centre de la source de lumiere
	 */
	public abstract Point getCenter();
	
	/**
	 * Permet de recuperer l'intensite lumineuse de la source de lumiere
	 * 
	 * @return Un reel entre 0 et 1 representant l'intensite lumineuse de la source lumineuse 
	 */
	public abstract double getIntensity();
}
