package povParser;

import geometry.shapes.SphereMaths;
import maths.Vector3D;

public class SphereParser implements Runnable
{

    public SphereParser()
    {
        System.out.println("sphere parser");
    }

    public void createSphere(double xCenter, double yCenter, double zCenter, double radius, String ... sphereModifiers)
    {
        Vector3D sphereCenter = new Vector3D(xCenter, yCenter, zCenter);
    }
    @Override
    public void run()
    {

    }
}
