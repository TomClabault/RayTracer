package parsers.povParser.state;


import maths.Point;
import parsers.povParser.PovAutomat;
import scene.lights.LightBulb;
import scene.lights.PositionnalLight;

/**
 * differents attributs que peut prendre la lumiere ainsi que les elements de syntaxe qui representent differents etats
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
 * Classe representant l'etat lumiere, c'est a dire que le jeton courant est le mot
 * light_source dans le fichier pov. C'est ici que tout le parsing de la lumiere est effectue.
 */
public class StateLightSource implements StateToken
{

    /**
     * @param context contexte courant de l'automate
     * Cette methode effectue le parsing d'un objet light_source (point light)
     * @return un objet de type positionnal light qui decrit la source de lumiere
     */
    @Override
    public PositionnalLight parse(PovAutomat context)
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
