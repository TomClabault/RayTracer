package materials;

import javafx.scene.paint.Color;

/*
 * Défini des matériaux dont les réflexions sont floues
 */
public class RoughMaterial extends Material 
{
	public RoughMaterial(Color color, double roughness)
	{
		super(color, 1, 1, 0.5, 0.125, 3, false, 0, roughness);
	}
}
