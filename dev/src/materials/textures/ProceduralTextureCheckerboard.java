package materials.textures;

import javafx.scene.paint.Color;
import maths.ColorOperations;
import maths.Point;

public class ProceduralTextureCheckerboard implements ProceduralTexture 
{
	private Color color1;
	private Color color2;
	
	private double size;
	
	/*
	 * Crée la texture procédurale d'un checkerboard à partir des deux couleurs de son damier. Les cases sont des carrés de 2x2 unité
	 * 
	 * @param color1 La première couleur qui sera utilisée pour le damier
	 * @param color2 La deuxième couleur qui sera utilisée pour le damier
	 */
	public ProceduralTextureCheckerboard(Color color1, Color color2) 
	{
		this(color1, color2, 2);
	}
	
	/*
	 * Crée la texture procédurale d'un checkerboard à partir des deux couleurs de son damier et de la taille des cases
	 * 
	 * @param color1 La première couleur qui sera utilisée pour le damier
	 * @param color2 La deuxième couleur qui sera utilisée pour le damier
	 * @param size 	 La taille des cases du damier
	 */
	public ProceduralTextureCheckerboard(Color color1, Color color2, double size) 
	{
		this.color1 = color1;
		this.color2 = color2;
		
		this.size = size;
	}

	/*
	 * @link{textures.ProceduralTexture#getColorAt}
	 */
	@Override
	public Color getColorAt(Point point) 
	{
		int xInt = (int)Math.floor(point.getX() * size);
		int yInt = (int)Math.floor(point.getY() * size);
		
		//Si la somme de u et v est impaire, on est sur une case de couleur 2 du damier
		//Sinon, couleur 1
		return ((xInt + yInt) & 0x1) == 1 ? this.color2 : this.color1;
	}
	
	/*
	 * Permet de redéfinir la première couleur des cases du damier
	 * 
	 * @param color La nouvelle couleur des première cases du damier
	 */
	public void setColor1(Color color)
	{
		this.color1 = color;
	}
	
	/*
	 * Permet de redéfinir la deuxième couleur des cases du damier
	 * 
	 * @param color La nouvelle couleur des deuxièmes cases du damier
	 */
	public void setColor2(Color color)
	{
		this.color2 = color;
	}
	
	@Override
	public String toString()
	{
		return String.format("[Checkerboard Procedural] Couleur 1: %s | Couleur 2: %s | Taille: %.3f", ColorOperations.colorToString(color1), ColorOperations.colorToString(color2), size);
	}
}
