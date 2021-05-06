package materials;

import javafx.scene.paint.Color;

public class MirrorMaterial extends Material
{
	/**
	 * Cree un materiau reflechissant
	 * 
	 * @param reflectiveness Poucentage de reflexion du materiau. Le materiau est tout noir a 0 puisqu'il sera equivalent a un miroir reflechissant 0% de la lumiere --> rien
	 */
	public MirrorMaterial(double reflectiveness)
	{
		super(Color.rgb(0, 0, 0), 1, 1.0, reflectiveness, reflectiveness, 192, false, 0, 0);
	}
}
