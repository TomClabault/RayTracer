package scene.lights;

import geometry.Point;

public class LightBulb implements Light 
{
	Point center;
	
	public LightBulb(Point center)
	{
		this.center = center;
	}
	
	public Point getCenter()
	{
		return this.center;
	}
}
