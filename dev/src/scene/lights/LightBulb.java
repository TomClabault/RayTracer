package scene.lights;

import maths.Vector3D;

public class LightBulb implements Light 
{
	Vector3D center;
	
	double intensity;
	
	public LightBulb(Vector3D center, double lightIntensity)
	{
		this.center = center;
		
		this.intensity = lightIntensity;
	}
	
	public Vector3D getCenter()
	{
		return this.center;
	}
	
	/*
	 * Permet d'obtenir l'intensité de la lumière
	 * 
	 * @return Un réel entre 0 et 1 représentant l'intensité de la lumière
	 */
	public double getIntensity()
	{
		return this.intensity;
	}
}
