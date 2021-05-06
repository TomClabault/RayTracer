package materials;

import javafx.scene.paint.Color;
import materials.textures.*;

public class MetallicMaterial extends Material
{
	/**
	 * Cree un materiau d'une certaine couleur ayant des reflets metalliques
	 * 
	 * @param color La couleur du materiau metallique
	 */
	public MetallicMaterial(Color color)
	{
		super(color, 1, 1, 0.06, 1, 200, false, 0, 0);
	}
	
	/**
	 * Cree un materiau d'une certaine couleur ayant des reflets metalliques et une certaine texture procedurale
	 * 
	 * @param color La couleur du materiau metallique
	 * @param proceduralTexture Texture procedurale du materiau @link{textures.ProceduralTexture}
	 */
	public MetallicMaterial(Color color, ProceduralTexture proceduralTexture)
	{
		super(color, 1, 1, 0.06, 1, 200, false, 0, 0, proceduralTexture);
	}
}
