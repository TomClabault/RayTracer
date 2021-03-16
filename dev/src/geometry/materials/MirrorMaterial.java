package geometry.materials;

import javafx.scene.paint.Color;

public class MirrorMaterial extends Material
{
	/*
	 * Crée un matériau réflechissant
	 *
	 * @param reflectiveness Poucentage de réflexion du matériau. Le matériau est tout noir à 0 puisqu'il sera équivalent à un miroir réfléchissant 0% de la lumière --> rien
	 */
	public MirrorMaterial(double reflectiveness)
	{
		super(Color.rgb(32, 32, 32), 0.5, 0.0, reflectiveness, 1*reflectiveness, 192, false, 666);
	}
}
