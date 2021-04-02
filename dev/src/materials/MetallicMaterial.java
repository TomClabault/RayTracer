package materials;

import javafx.scene.paint.Color;

public class MetallicMaterial extends Material
{
	public MetallicMaterial(Color color)
	{
		super(color, 0.75, 0.4, 1, 256, false, 0);
	}
}
