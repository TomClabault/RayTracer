package povParser;

import geometry.Shape;
import javafx.scene.paint.Color;
import materials.Material;
import maths.CoordinateObject;

import java.io.StreamTokenizer;
import java.util.ArrayList;

/**
 * différents états d'un objet de type sphère ou plan
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
 * Classe abstraire effectuant le parsing d'un objet de type sphère ou plan (car la syntaxe
 * est la même)
 */
public abstract class EtatSpherePlane extends EtatUtil implements EtatToken
{
    /**
     * Méthode permettant de renvoyer un objet plan ou sphère
     * @param coord vecteur normal dans le cas du plan, centre dans le cas de la sphère
     * @param dist distance dans le cas du plan, rayon dans le cas de la sphère
     * @param material les différents modificateurs de textures, couleurs, etc.
     * @return un objet de type Shape qui décrit un plan ou une sphère
     */
    protected abstract Shape createInstance(double[] coord, Double dist, Material material);

    /**
     * Méthode permettant de parser une sphère ou un plan
     * @param context contexte courant de l'automate
     * @return Un objet de type shape contenant les coordonnées parsées etc.
     */
    @Override
    public Shape action(Automat context)
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
