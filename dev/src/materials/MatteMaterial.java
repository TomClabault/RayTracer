package materials;

import javafx.scene.paint.Color;
import textures.ProceduralTexture;

public class MatteMaterial extends Material
{
	/*
	 * Crée un matériau mat d'une certaine couleur
	 * 
	 * @param color Couleur du matériau
	 */
	public MatteMaterial(Color color)
	{
		super(color, 0.75, 0, 0.05, 1);
	}
	
	/*
	 * Crée un matériau mat d'une certaine couleur ayant une certaine texture procédurale
	 * 
	 * @param color Couleur du matériau
	 * @param proceduralTexture Texture procédurale du matériau @link{textures.ProceduralTexture}
	 */
	public MatteMaterial(Color color, ProceduralTexture proceduralTexture)
	{
		super(color, 0.75, 0, 0.05, 1, proceduralTexture);
	}
}
