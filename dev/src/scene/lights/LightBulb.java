package scene.lights;

import maths.Point;

public class LightBulb implements PositionnalLight 
{
	private Point center;
	
	private double intensity;
	
	public LightBulb(Point center, double lightIntensity)
	{
		this.center = center;
		
		this.intensity = lightIntensity;
	}
	
	public Point getCenter()
	{
		return this.center;
	}
	
	/**
	 * Permet d'obtenir l'intensite de la lumiere
	 * 
	 * @return Un reel entre 0 et 1 representant l'intensite de la lumiere
	 */
	public double getIntensity()
	{
		return this.intensity;
	}
	
	@Override
	public String toString()
	{
		return String.format("Centre: %s | Intensity: %.3f", center, intensity);
	}
}
