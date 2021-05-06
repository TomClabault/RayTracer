package parsers.povParser.state;

import geometry.Shape;
import geometry.shapes.Sphere;
import materials.Material;
import maths.Point;

/**
 * Classe decrivant l'etat d'une sphere
 */
public class StateSphere extends StateSpherePlane
{
    /**
     * Methode permettant de renvoyer un objet plan parsee par le classe mere EtatSpherePlane
     * @param center centre de la sphere
     * @param radius rayon de la sphere
     * @param material les differents modificateurs de textures, couleurs, etc.
     * @return un objet de type Shape qui decrit le plan
     */
    @Override
    protected Shape createInstance(double[] center, Double radius, Material material)
    {
        Sphere sphere = new Sphere(new Point(center[0], center[1], center[2]), radius, material);
        return sphere;
    }
}