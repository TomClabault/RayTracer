package parsers.povParser.state;

import geometry.Shape;
import geometry.shapes.Parallelepiped;
import javafx.scene.paint.Color;
import materials.Material;
import maths.Point;
import parsers.povParser.PovAutomat;

import java.io.StreamTokenizer;

/**
 * enumeration des differents etats d'un objet box
 */
enum Boxcontent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    OPENING_CHEVRON,
    ENDING_CHEVRON,
    OUTSIDE,
    ATTRIBUTE,
}

/**
 * Classe permettant de parser une figure de type box
 */
public class StateBox extends StateUtil implements StateToken
{
    /**
     * Methode permettant de parser la box
     * @param context contexte courant de l'automate
     * @return Un objet de type shape contenant les coordonnees parsees etc.
     */
    @Override
    public Shape parse(PovAutomat context)
    {
        int nextToken = context.callNextToken();

        StreamTokenizer st = context.getStreamTokenizer();
        context.callNextToken();
        int bracketNb = 0;
        int coordNb = 0;
        Material material = new Material(Color.rgb(0, 0, 0), 0, 0, 0, 0, 0, false, 0, 0);
        Parallelepiped rectangle = null;
        Point vector1 = null;
        Point vector2 = null;

        Boxcontent state = Boxcontent.OPENING_BRACKET;

        while(state != Boxcontent.OUTSIDE)
        {
            switch (state)
            {
                case OPENING_BRACKET:
                {
                    context.callNextToken();

                    state = Boxcontent.OPENING_CHEVRON;
                    bracketNb++;
                    break;
                }
                case OPENING_CHEVRON:
                {
                    double[] coordArray = new double[3];
                    for(int i = 0; i < 3; i++)
                    {
                        context.callNextToken(); // la virgule
                        context.callNextToken();
                        coordArray[i] = context.getNumberValue();
                    }
                    if(vector1 == null)
                        vector1 = new Point(coordArray[0], coordArray[1], coordArray[2]);
                    else
                        vector2 = new Point(coordArray[0], coordArray[1], coordArray[2]);
                    state = Boxcontent.ENDING_CHEVRON;
                    coordNb++;

                    break;
                }
                case ENDING_CHEVRON:
                {
                    context.callNextToken(); //passe le chevron fermant
                    if(coordNb == 2)
                    {
                        state = Boxcontent.ATTRIBUTE;
                    }
                    else
                    {
                        state = Boxcontent.OPENING_CHEVRON;
                    }
                    break;
                }
                case ENDING_BRACKET:
                {
                    context.callNextToken();
                    if(context.isCurrentTokenAWord())
                    {
                        state = Boxcontent.ATTRIBUTE;
                    }
                    bracketNb--;
                    if(bracketNb == 0)
                    {
                        state = Boxcontent.OUTSIDE;
                    }
                    break;
                }

                case ATTRIBUTE:
                {
                    material = super.parseAttributes(context);
                    state = Boxcontent.OUTSIDE;
                    break;
                }
            }
        }
        rectangle = new Parallelepiped(vector1, vector2, material);
        return rectangle;
    }
}
