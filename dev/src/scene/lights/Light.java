package scene.lights;

import geometry.Point;

public interface Light 
{
	public abstract Point getCenter();
	
	public abstract double getIntensity();
}
