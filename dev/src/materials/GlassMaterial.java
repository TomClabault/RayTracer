package materials;

import javafx.scene.paint.Color;

public class GlassMaterial extends Material{

    public GlassMaterial() {//1,474 /*indice de refraction du pirex selon wikipedia*/
        super(Color.WHITE, 0, 0, 0,  1,true, 1.474);
    }
}
