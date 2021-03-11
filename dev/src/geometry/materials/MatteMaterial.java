package geometry.materials;

import javafx.scene.paint.Color;

public class MatteMaterial extends Material
{
	/*
	 * Crée un matériau mat d'une certaine couleur
	 * 
	 * @param color Couleur du matériau
	 */
	public MatteMaterial(Color color)
	{
		super(color, 1, 0.75, 0, 0.05, 1);
	}
}