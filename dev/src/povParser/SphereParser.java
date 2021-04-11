package povParser;

import geometry.shapes.Sphere;
import maths.Point;
import maths.Vector;

public class SphereParser implements Runnable
{

    public SphereParser()
    {
        System.out.println("sphere parser");
    }

    public void createSphere(double xCenter, double yCenter, double zCenter, double radius, String ... sphereModifiers)
    {
        Point sphereCenter = new Point(xCenter, yCenter, zCenter);
    }
    @Override
    public void run()
    {

    }
}
