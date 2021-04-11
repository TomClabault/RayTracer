package materials;

import javafx.scene.paint.Color;

/*
 * Défini des matériaux dont les réflexions sont floues
 */
public class GlossyMaterial extends Material 
{
	public GlossyMaterial(Color color, double glossiness)
	{
		super(Color.rgb(0, 0, 0), 1, 1.0, 0.75, 0.75, 192, false, 0, glossiness);
		//super(color, 1, 1, 0.2, 1, 256, false, 0, glossiness);
	}
}
