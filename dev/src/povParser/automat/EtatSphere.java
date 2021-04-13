package povParser.automat;

import geometry.Shape;
import geometry.shapes.Sphere;
import materials.Material;
import maths.Point;

import java.util.ArrayList;

public class EtatSphere extends EtatSpherePlane
{
    @Override
    protected Shape createInstance(double[] center, Double radius, Material material)
    {
        Sphere sphere = new Sphere(new Point(center[0], center[1], center[2]), radius, material);
        return sphere;
    }
}