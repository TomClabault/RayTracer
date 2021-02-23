package scene.lights;

import maths.Point;

public interface Light 
{
	public abstract Point getCenter();
	
	public abstract double getIntensity();
}
