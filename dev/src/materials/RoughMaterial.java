package materials;

import javafx.scene.paint.Color;
import materials.textures.ProceduralTexture;

/*
 * Défini des matériaux dont les réflexions sont floues
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
	
	private static int computeSpecularSize(double roughness)
	{
		//Fonction obtenue par curve_fit avec scipy sur jupyter notebook et ajustée pour ramener toutes les valeurs < 1 à 1
		return (int)Math.round(0.481415*Math.exp(roughness*6.2106) - 47.7517) < 1 ? 1 : (int)Math.round(0.481415*Math.exp(roughness*6.2106) - 47.7517);
	}
	
	private static double computeSpecularIntensity(double roughness)
	{
		return Math.round(892*roughness - 637)/255.0;
	}
}
