package scene.lights;

import maths.Point;

public interface PositionnalLight 
{
	/*
	 * Permet d'obtenir la position de la lumière
	 * 
	 *  @return Retourne le point de coordonnées (x, y, z) représentant le centre de la source de lumière
	 */
	public abstract Point getCenter();
	
	/*
	 * Permet de récupérer l'intensité lumineuse de la source de lumière
	 * 
	 * @return Un réel entre 0 et 1 représentant l'intensité lumineuse de la source lumineuse 
	 */
	public abstract double getIntensity();
}
