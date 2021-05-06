package parsers.povParser.state;

import geometry.Shape;
import javafx.scene.paint.Color;
import materials.Material;
import maths.CoordinateObject;
import parsers.povParser.PovAutomat;

import java.io.StreamTokenizer;
import java.util.ArrayList;

/**
 * differents etats d'un objet de type sphere ou plan
 */
enum SpherePlanecontent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    OPENING_CHEVRON,
    ENDING_CHEVRON,
    OUTSIDE,
    ATTRIBUTE
}

/**
 * Classe abstraire effectuant le parsing d'un objet de type sphere ou plan (car la syntaxe
 * est la meme)
 */
public abstract class StateSpherePlane extends StateUtil implements StateToken
{
    /**
     * Methode permettant de renvoyer un objet plan ou sphere
     * @param coord vecteur normal dans le cas du plan, centre dans le cas de la sphere
     * @param dist distance dans le cas du plan, rayon dans le cas de la sphere
     * @param material les differents modificateurs de textures, couleurs, etc.
     * @return un objet de type Shape qui decrit un plan ou une sphere
     */
    protected abstract Shape createInstance(double[] coord, Double dist, Material material);

    /**
     * Methode permettant de parser une sphere ou un plan
     * @param context contexte courant de l'automate
     * @return Un objet de type shape contenant les coordonnees parsees etc.
     */
    @Override
    public Shape parse(PovAutomat context)
    {
        ArrayList<String> list = new ArrayList<>();

        StreamTokenizer st = context.getStreamTokenizer();
        context.callNextToken();
        int bracketNb = 0;
        boolean sphereCoord = false;
        boolean color = false;
        boolean coord = false;
        int nextToken = 0;
        double[] coordArray = {0};
        Material material = new Material(Color.rgb(0, 0, 0), 0, 0, 0, 0, 0, false, 0, 0);
        CoordinateObject vect = null;

        Double dist = null;

        SpherePlanecontent state = SpherePlanecontent.OPENING_BRACKET;

        while(state != SpherePlanecontent.OUTSIDE) {

            switch (state) {
                case OPENING_BRACKET: {
                    context.callNextToken();

                    state = SpherePlanecontent.OPENING_CHEVRON;
                    bracketNb++;
                    coord = true;
                    break;
                }
                case ENDING_BRACKET: {
                    nextToken = context.callNextToken(); //skip '}'
                    if (context.isCurrentTokenAWord()) {
                        state = SpherePlanecontent.ATTRIBUTE;
                    } else {
                        bracketNb--;
                        if (bracketNb == 0) {
                            state = SpherePlanecontent.OUTSIDE;
                        }
                    }
                    break;
                }
                case OPENING_CHEVRON: {
                    coordArray = new double[3];
                    if (coord) {
                        for (int i = 0; i < 3; i++) {
                            context.callNextToken(); // la virgule
                            context.callNextToken();
                            coordArray[i] = context.getNumberValue();
                        }
                    }
                    //coord = false;
                    state = SpherePlanecontent.ENDING_CHEVRON;
                    break;
                }
                case ENDING_CHEVRON: {
                    nextToken = context.callNextToken(); //skip le chevron fermant
                    if (context.isCurrentTokenAWord()) {
                        state = SpherePlanecontent.ATTRIBUTE;
                    } else {
                        if ((char) nextToken == ',') {
                            context.callNextToken();
                            dist = context.getNumberValue();
                            context.callNextToken();
                            if (context.isCurrentTokenAWord()) {
                                state = SpherePlanecontent.ATTRIBUTE;
                            } else
                                state = SpherePlanecontent.ENDING_BRACKET;
                        } else if ((char) nextToken == '}') {
                            state = SpherePlanecontent.ENDING_BRACKET;
                        }
                    }
                    break;
                }
                case ATTRIBUTE: {
                    material = super.parseAttributes(context);
                    state = SpherePlanecontent.OUTSIDE;
                    break;
                }
            }
        }
        return createInstance(coordArray, dist, material);
    }
}
