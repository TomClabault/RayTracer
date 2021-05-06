package materials;

import javafx.scene.paint.Color;
import materials.textures.ProceduralTexture;

/**
 * Defini des materiaux dont les reflexions sont floues
 */
public class RoughMaterial extends Material 
{
	public RoughMaterial(Color color, double roughness)
	{
		super(color, 1, 1, 0.5, RoughMaterial.computeSpecularIntensity(roughness), RoughMaterial.computeSpecularSize(roughness), false, 0, roughness);
	}
	
	public RoughMaterial(Color color, double roughness, ProceduralTexture proceduralTexture)
	{
		super(color, 1, 1, 0.5, RoughMaterial.computeSpecularIntensity(roughness), RoughMaterial.computeSpecularSize(roughness), false, 0, roughness, proceduralTexture);
		
	}
	
	public static int computeSpecularSize(double roughness)
	{
		//Fonction obtenue par curve_fit avec scipy sur jupyter notebook et ajustee pour ramener toutes les valeurs < 1 a 1
		return (int)Math.round(0.481415*Math.exp(roughness*6.2106) - 47.7517) < 1 ? 1 : (int)Math.round(0.481415*Math.exp(roughness*6.2106) - 47.7517);
	}
	
	public static double computeSpecularIntensity(double roughness)
	{
		return Math.round(892*roughness - 637)/255.0 < 0 ? 0 : Math.round(892*roughness - 637)/255.0;
	}
}
