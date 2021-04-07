package materials;

import javafx.scene.paint.Color;
import materials.textures.*;

public class MetallicMaterial extends Material
{
	/*
	 * Crée un matériau d'une certaine couleur ayant des reflets métalliques
	 * 
	 * @param color La couleur du matériau métallique
	 */
	public MetallicMaterial(Color color)
	{
		super(color, 1, 0.03, 1, 200, false, 0);
	}
	
	/*
	 * Crée un matériau d'une certaine couleur ayant des reflets métalliques et une certaine texture procédurale
	 * 
	 * @param color La couleur du matériau métallique
	 * @param proceduralTexture Texture procédurale du matériau @link{textures.ProceduralTexture}
	 */
	public MetallicMaterial(Color color, ProceduralTexture proceduralTexture)
	{
		super(color, 1, 0.03, 1, 200, false, 0, proceduralTexture);
	}
}
