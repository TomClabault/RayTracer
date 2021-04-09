package povParser.automat;

import maths.Vector3D;
import scene.lights.LightBulb;

import java.io.StreamTokenizer;

enum LightContent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    CENTER,
    INTENSITY,
    OUTSIDE,
}


public class EtatLightSource implements EtatToken
{
    //vecteur couleur non implémenté
    @Override
    public void action(Automat context)
    {
        LightBulb light = null;
        Vector3D center = null;
        double lightIntensity = 0;

        context.callNextToken(); //skip light_source

        LightContent state = LightContent.OPENING_BRACKET;

        while (state != LightContent.OUTSIDE)
        {
            switch (state)
            {
                case OPENING_BRACKET:
                {
                    context.callNextToken();
                    state = LightContent.CENTER;
                    break;
                }
                case CENTER:
                {
                    context.callNextToken(); // skip '<'

                    double[] coordArray = new double[3];
                    for (int i = 0; i < 3; i++) {
                        coordArray[i] = context.getNumberValue();
                        context.callNextToken();
                        context.callNextToken();
                    }
                    context.callNextToken(); // skip ','
                    center = new Vector3D(coordArray[0], coordArray[1], coordArray[2]);
                    state = LightContent.INTENSITY;
                    break;
                }
                case INTENSITY:
                {
                    lightIntensity = context.getNumberValue();
                    state = LightContent.ENDING_BRACKET;
                    break;
                }
                case ENDING_BRACKET:
                {
                    context.callNextToken(); // skip '}'
                    state = LightContent.OUTSIDE;
                    break;
                }
            }
        }
        light = new LightBulb(center, lightIntensity);
    }
}
