package povParser.automat;


import maths.Point;
import scene.lights.LightBulb;
import scene.lights.PositionnalLight;

/**
 * différents attributs que peut prendre la lumière ainsi que les éléments de syntaxe qui représentent différents états
 */
enum LightContent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    CENTER,
    INTENSITY,
    OUTSIDE,
}

/**
 * Classe représentant l'état lumière, c'est à dire que le jeton courant est le mot
 * light_source dans le fichier pov. C'est ici que tout le parsing de la lumière est effectué.
 */
public class EtatLightSource implements EtatToken
{

    /**
     * @param context contexte courant de l'automate
     * Cette méthode effectue le parsing d'un objet light_source (point light)
     */
    @Override
    public PositionnalLight action(Automat context)
    {
        LightBulb light = null;
        Point center = null;
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
                    center = new Point(coordArray[0], coordArray[1], coordArray[2]);
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
        return light;
    }
}
