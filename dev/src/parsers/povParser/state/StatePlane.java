package parsers.povParser.state;

import geometry.Shape;
import geometry.shapes.Plane;
import materials.Material;
import maths.Vector;

/**
 * Classe decrivant l'etat d'un plan
 */
public class StatePlane extends StateSpherePlane
{
    /**
     * Methode permettant de renvoyer un objet plan parsee par le classe mere EtatSpherePlane
     * @param normal le vecteur normal au plan
     * @param distance la distance du plan
     * @param material les differents modificateurs de textures, couleurs, etc.
     * @return un objet de type Shape qui decrit le plan
     */
    @Override
    protected Shape createInstance(double[] normal, Double distance, Material material)
    {
        Plane plane = new Plane(new Vector(normal[0], normal[1], normal[2]), distance, material);
        return plane;
    }
}
