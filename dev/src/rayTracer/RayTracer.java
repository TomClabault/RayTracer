package rayTracer;

import javafx.scene.paint.Color;
import scene.Scene;

/*
 * Une instance de RayTracer créée à partir de la largeur et de la hauteur du rendu voulu. Permet de générer les 
 */
public class RayTracer 
{
	private int renderHeight;
	private int renderLength;
	
	private boolean imageRendered;
	
	Color[][] renderedPixels;
	
	public RayTracer(int height, int length)
	{
		this.renderLength = length;
		this.renderHeight = height;
		
		renderedPixels = new Color[height][length];
	}
	
	/*
	 * Calcule tous les pixels de la scène donnée en argument et retourne un tableau de couleur RGB correspondant aux pixels
	 * 
	 * @param renderScene La scène de rendu contenant les informations pour rendre l'image
	 * 
	 * @return Un tableau de Color.RGB(r, g, b) de dimension renderHeight*renderLength
	 */
	public Color[][] computeImage(Scene renderScene)
	{
		return renderedPixels;
	}
	
	public Color[][] getRenderedPixels()
	{
		if(this.imageRendered == false)
			return null;
	}
}
