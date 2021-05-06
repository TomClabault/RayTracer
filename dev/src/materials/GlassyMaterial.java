package materials;

import javafx.scene.paint.Color;

/**
 * Defini des materiaux legerement reflechissant dote de reflets de Fresnel. Le materiau est en effet d'autant plus reflechissant que les rayons incidents sont perpendiculaire a la normale de la surface
 */
public class GlassyMaterial extends Material 
{
	public GlassyMaterial(Color color)
	{
		//L'indice de refraction de 1.5 est arbitraire
		super(color, 1, 1, 0.03, 1, 256, false, 1.5, 0);
	}
}
