package povParser;

import geometry.Shape;
import geometry.shapes.Plane;
import materials.Material;
import maths.Vector;


import java.util.ArrayList;

public class EtatPlane extends EtatSpherePlane
{
    @Override
    protected Shape createInstance(double[] normal, Double distance, Material material)
    {
        Plane plane = new Plane(new Vector(normal[0], normal[1], normal[2]), distance, material);
        return plane;
    }
}
