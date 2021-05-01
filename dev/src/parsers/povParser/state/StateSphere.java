package parsers.povParser.state;

import geometry.Shape;
import geometry.shapes.Sphere;
import materials.Material;
import maths.Point;

/**
 * Classe décrivant l'état d'une sphère
 */
public class StateSphere extends StateSpherePlane
{
    /**
     * Méthode permettant de renvoyer un objet plan parsée par le classe mère EtatSpherePlane
     * @param center centre de la sphère
     * @param radius rayon de la sphère
     * @param material les différents modificateurs de textures, couleurs, etc.
     * @return un objet de type Shape qui décrit le plan
     */
    @Override
    protected Shape createInstance(double[] center, Double radius, Material material)
    {
        Sphere sphere = new Sphere(new Point(center[0], center[1], center[2]), radius, material);
        return sphere;
    }
}