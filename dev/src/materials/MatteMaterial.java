package materials;

import javafx.scene.paint.Color;
import materials.textures.*;

public class MatteMaterial extends Material
{
	/**
	 * Cree un materiau mat d'une certaine couleur
	 * 
	 * @param color Couleur du materiau
	 */
	public MatteMaterial(Color color)
	{
		super(color, 1, 0.75, 0, 0.05, 1, false, 0, 0);
	}
	
	/**
	 * Cree un materiau mat d'une certaine couleur ayant une certaine texture procedurale
	 * 
	 * @param color Couleur du materiau
	 * @param proceduralTexture Texture procedurale du materiau @link{textures.ProceduralTexture}
	 */
	public MatteMaterial(Color color, ProceduralTexture proceduralTexture)
	{
		super(color, 1, 0.75, 0, 0.05, 1, false, 0, 0, proceduralTexture);
	}
}
