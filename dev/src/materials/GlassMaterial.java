package materials;

import javafx.scene.paint.Color;
import maths.Vector;

/**
 * Permet de créer un matériau en verre qui réfracte la lumière et la réfléchit en proportions données par la formule de Fresnel
 */
public class GlassMaterial extends Material
{
    public GlassMaterial() 
    {
    	//1,474 /*indice de refraction du pirex selon wikipedia*/
        super(Color.BLACK, 1, 0, 0, 0, 1, true, 1.474, 0);
    }
    
    public GlassMaterial(Vector absorption) 
    {
    	//1,474 /*indice de refraction du pirex selon wikipedia*/
        super(Color.BLACK, 1, 0, 0, 0, 1, true, 1.474, absorption, 0, null);
    }
}
