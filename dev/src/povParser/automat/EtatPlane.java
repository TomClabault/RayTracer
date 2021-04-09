package povParser.automat;

import geometry.shapes.PlaneMaths;
import materials.Material;
import maths.Vector3D;

import java.util.ArrayList;

public class EtatPlane extends EtatSpherePlane
{
    @Override
    protected void createInstance(Vector3D normal, Double distance, Material material)
    {
        PlaneMaths plane = new PlaneMaths(normal, distance, material);
    }
}
