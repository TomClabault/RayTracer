package geometry.materials;

import javafx.scene.paint.Color;

public class MetallicMaterial extends Material
{
	public MetallicMaterial(Color color)
	{
		//public Material(Color color, double ambientCoeff, double diffuseCoeff, double reflectiveCoeff, double specularCoeff, int shininess)
		super(color, 0.5, 0.75, 0.4, 1, 256);
	}
}
