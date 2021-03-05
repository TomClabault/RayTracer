package geometry.materials;

import javafx.scene.paint.Color;

public class MetallicMaterial extends Material
{
	public MetallicMaterial(Color color)
	{
		//public SphereMaths(Point center, double radius, Color sphereColor, int shininess, double ambientCoeff, double specularCoeff, double diffuseCoeff, double reflectiveCoeff)
		//public Material(Color color, double ambientCoeff, double diffuseCoeff, double reflectiveCoeff, double specularCoeff, int shininess)
		super(color, 0.5, 0.75, 0.4, 1, 256);
	}
	//Color.rgb(204, 0, 0), 10, 0.5, 0.3, 0.75, 0.25
}
