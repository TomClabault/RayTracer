package materials.textures;

import javafx.scene.paint.Color;
import maths.Point;

/**
 * Les classes implementant cette interface definiront des textures dites procedurales. La couleur de ces textures ne depend pas d'une image mais plutot de mathematiques.
 * Elles se comportent cependant comme des images en ce que, pour acceder a la couleur de la texture, nous aurons besoin des coordonnees u et v du point sur la texture dont on veut la couleur.
 */
public interface ProceduralTexture 
{
	/**
	 * Permet de recuperer la couleur de la texture en des coordonees 2D (u, v) donnees
	 * 
	 * @param UVPoint Vector3D (x, y, z) contenant les coordonnees de texture (u, v) du point dont on veut la couleur tel que: x = u, y = v. z est insignifiant 
	 * 
	 * @return Retourne la couleur de la texture au point de coordonnees de texture (u, v) donne
	 */
	public Color getColorAt(Point UVPoint);
}
