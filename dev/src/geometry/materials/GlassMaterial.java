package geometry.materials;

import javafx.scene.paint.Color;

public class GlassMaterial extends Material{

    public GlassMaterial() {//1,474 /*indice de refraction du pirex selon wikipedia*/
        super(Color.WHITE,0.5 , 0.75, 0.2, 0,  1,true, 1.474);

    }
}
