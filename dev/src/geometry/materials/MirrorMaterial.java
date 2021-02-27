package geometry.materials;

import javafx.scene.paint.Color;

public class MirrorMaterial extends Material
{
	public MirrorMaterial(double reflectiveness)
	{
		//public Material(Color color, double ambientCoeff, double diffuseCoeff, double reflectiveCoeff, double specularCoeff, double shininess)
		super(Color.rgb(64, 64, 64), 0.5, 0.0, reflectiveness, 1*reflectiveness, 192);
	}
}
