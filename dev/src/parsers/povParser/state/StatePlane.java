package parsers.povParser.state;

import geometry.Shape;
import geometry.shapes.Plane;
import materials.Material;
import maths.Vector;

/**
 * Classe décrivant l'état d'un plan
 */
public class StatePlane extends StateSpherePlane
{
    /**
     * Méthode permettant de renvoyer un objet plan parsée par le classe mère EtatSpherePlane
     * @param normal le vecteur normal au plan
     * @param distance la distance du plan
     * @param material les différents modificateurs de textures, couleurs, etc.
     * @return un objet de type Shape qui décrit le plan
     */
    @Override
    protected Shape createInstance(double[] normal, Double distance, Material material)
    {
        Plane plane = new Plane(new Vector(normal[0], normal[1], normal[2]), distance, material);
        return plane;
    }
}
