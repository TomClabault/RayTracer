package materials;

import javafx.scene.paint.Color;

/*
 * Défini des matériaux légèrement réfléchissant doté de reflets de Fresnel. Le matériau est en effet d'autant plus réfléchissant que les rayons incidents sont perpendiculaire à la normale de la surface
 */
public class GlassyMaterial extends Material 
{
	public GlassyMaterial(Color color)
	{
		//L'indice de réfraction de 1.5 est arbitraire
		super(color, 1, 1, 0.03, 1, 256, false, 1.5, 0);
	}
}
