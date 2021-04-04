package scene.lights;

import maths.Vector3D;

public interface Light 
{
	public abstract Vector3D getCenter();
	
	public abstract double getIntensity();
}
