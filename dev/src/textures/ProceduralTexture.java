package textures;

import javafx.scene.paint.Color;
import maths.Point;

/*
 * Les classes implémentant cette interface définiront des textures dites procédurales. La couleur de ces textures ne dépend pas d'une image mais plutôt de mathématiques.
 * Elles se comportent cependant comme des images en ce que, pour accéder à la couleur de la texture, nous aurons besoin des coordonnées u et v du point sur la texture dont on veut la couleur.
 */
public interface ProceduralTexture 
{
	/*
	 * Énumération définissant les différentes textures procédurales implémentées
	 */
	public static enum TEXTURES 
	{
		TEXTURE_NO_TEXTURE,
		TEXTURE_CHECKERBOARD
	};
	
	/*
	 * Permet de récupérer la couleur de la texture en des coordonées 2D (u, v) données
	 * 
	 * @param UVPoint Point (x, y, z) contenant les coordonnées de texture (u, v) du point dont on veut la couleur tel que: x = u, y = v. z est insignifiant 
	 * 
	 * @return Retourne la couleur de la texture au point de coordonnées de texture (u, v) donné
	 */
	public Color getColorAt(Point UVPoint);
	
	/*
	 * Permet d'obtenir la taille du pattern de la texture
	 * 
	 * @return Retourne la taille du motif (pattern) de la texture
	 */
	public double getSize();
}
