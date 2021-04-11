package povParser.automat;

import geometry.Shape;
import geometry.shapes.PlaneMaths;
import materials.Material;
import maths.Vector3D;

import java.util.ArrayList;

public class EtatPlane extends EtatSpherePlane
{
    @Override
    protected Shape createInstance(Vector3D normal, Double distance, Material material)
    {
        PlaneMaths plane = new PlaneMaths(normal, distance, material);
        return plane;
    }
}
