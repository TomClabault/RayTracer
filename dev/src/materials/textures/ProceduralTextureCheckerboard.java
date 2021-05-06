package materials.textures;

import javafx.scene.paint.Color;
import maths.ColorOperations;
import maths.Point;

public class ProceduralTextureCheckerboard implements ProceduralTexture 
{
	private Color color1;
	private Color color2;
	
	private double size;
	
	/**
	 * Cree la texture procedurale d'un checkerboard a partir des deux couleurs de son damier. Les cases sont des carres de 2x2 unite
	 * 
	 * @param color1 La premiere couleur qui sera utilisee pour le damier
	 * @param color2 La deuxieme couleur qui sera utilisee pour le damier
	 */
	public ProceduralTextureCheckerboard(Color color1, Color color2) 
	{
		this(color1, color2, 2);
	}
	
	/**
	 * Cree la texture procedurale d'un checkerboard a partir des deux couleurs de son damier et de la taille des cases
	 * 
	 * @param color1 La premiere couleur qui sera utilisee pour le damier
	 * @param color2 La deuxieme couleur qui sera utilisee pour le damier
	 * @param size 	 La taille des cases du damier
	 */
	public ProceduralTextureCheckerboard(Color color1, Color color2, double size) 
	{
		this.color1 = color1;
		this.color2 = color2;
		
		this.size = size;
	}

	/**
	 * {@link materials.textures.ProceduralTexture#getColorAt}
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
	
	/**
	 * Permet de redefinir la premiere couleur des cases du damier
	 * 
	 * @param color La nouvelle couleur des premiere cases du damier
	 */
	public void setColor1(Color color)
	{
		this.color1 = color;
	}
	
	/**
	 * Permet de redefinir la deuxieme couleur des cases du damier
	 * 
	 * @param color La nouvelle couleur des deuxiemes cases du damier
	 */
	public void setColor2(Color color)
	{
		this.color2 = color;
	}

	public void setSize(double newSize)
	{
		this.size = newSize;
	}
	
	@Override
	public String toString()
	{
		return String.format("[Checkerboard Procedural] Couleur 1: %s | Couleur 2: %s | Taille: %.3f", ColorOperations.colorToString(color1), ColorOperations.colorToString(color2), size);
	}
}
