package rayTracer;

import javafx.scene.paint.Color;

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
	
	public Color[][] computeImage(Camera camera, Light lightSource)
	{
		return renderedPixels;
	}
	
	public Color[][] getRenderedPixels()
	{
		if(this.imageRendered == false)
			return null;
	}
}
