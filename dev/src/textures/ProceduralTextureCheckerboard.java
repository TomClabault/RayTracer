package textures;

import javafx.scene.paint.Color;
import maths.Point;

public class ProceduralTextureCheckerboard implements ProceduralTexture 
{
	private Color color1;
	private Color color2;
	/*
	 * Crée la texture procédurale d'un checkerboard à partir des deux couleurs de son damier
	 * 
	 * @param color1 La première couleur qui sera utilisée pour le damier
	 * @param color2 La deuxième couleur qui sera utilisée pour le damier
	 */
	public ProceduralTextureCheckerboard(Color color1, Color color2) 
	{
		this.color1 = color1;
		this.color2 = color2;
	}

	/*
	 * @link{textures.ProceduralTexture#getColorAt}
	 */
	@Override
	public Color getColorAt(Point point) 
	{
		int xInt = (int)Math.floor(point.getX());
		int yInt = (int)Math.floor(point.getY());
		
		//Si la somme de u et v est impaire, on est sur une case de couleur 2 du damier
		//Sinon, couleur 1
		return ((xInt + yInt) & 0x1) == 1 ? this.color2 : this.color1;
	}
}
