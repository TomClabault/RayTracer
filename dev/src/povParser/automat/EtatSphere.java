package povParser.automat;

import geometry.Shape;
import geometry.shapes.SphereMaths;
import materials.Material;
import maths.Vector3D;

import java.util.ArrayList;

public class EtatSphere extends EtatSpherePlane
{
    @Override
    protected Shape createInstance(Vector3D center, Double radius, Material material)
    {
        SphereMaths sphere = new SphereMaths(center, radius, material);
        return sphere;
    }
}