package parsers.povParser.state;

import geometry.Shape;
import geometry.shapes.Triangle;
import javafx.scene.paint.Color;
import materials.Material;
import maths.Point;
import parsers.povParser.PovAutomat;

import java.io.StreamTokenizer;


/**
 * enumeration decrivant les differents etats d'un objet triangle
 */
enum Trianglecontent
{
    OPENING_BRACKET,
    ENDING_BRACKET,
    OPENING_CHEVRON,
    ENDING_CHEVRON,
    OUTSIDE,
    ATTRIBUTE;
}

/**
 * Classe representant l'etat Triangle
 */
public class StateTriangle extends StateUtil implements StateToken
{
    /**
     * Cette methode effectue le parsing d'un objet triangle
     * @param context contexte courant de l'automate
     * @return Un objet Shape decrivant le triangle
     */
    @Override
    public Shape parse(PovAutomat context)
    {
        int nextToken = context.callNextToken(); //accolade ouvrante apres le mot triangle

        StreamTokenizer st = context.getStreamTokenizer();
        context.callNextToken();
        int bracketNb = 0;
        int coordNb = 0;
        boolean color = false;
        boolean triangleCoord = false;
        Material material = new Material(Color.rgb(0, 0, 0), 0, 0, 0, 0, 0, false, 0, 0);
        Triangle triangle = null;
        Point vectA = null;
        Point vectB = null;
        Point vectC = null;

        Trianglecontent state = Trianglecontent.OPENING_BRACKET;

        while(state != Trianglecontent.OUTSIDE)
        {

            switch(state)
            {
                case OPENING_BRACKET:
                {
                    state = Trianglecontent.OPENING_CHEVRON;
                    triangleCoord = true;
                    bracketNb++;
                    break;
                }

                case OPENING_CHEVRON:
                {
                    context.callNextToken(); //skip '<'
                    double[][] coordArray = new double[3][3];
                    if(triangleCoord)
                    {
                        for(int point = 0; point < 3; point++)
                        {
                            if(point > 0)
                                context.callNextToken();
                            for (int coord = 0; coord < 3; coord++)
                            {
                                coordArray[point][coord] = context.getNumberValue();

                                context.callNextToken();


                                if(point < 2 || coord < 2)
                                    context.callNextToken();


                            }
                            if (point < 2)
                            {
                                context.callNextToken();
                            }
                        }
                        vectA = new Point(coordArray[0][0], coordArray[0][1], coordArray[0][2]);
                        vectB = new Point(coordArray[1][0], coordArray[1][1], coordArray[1][2]);
                        vectC = new Point(coordArray[2][0], coordArray[2][1], coordArray[2][2]);

                        triangleCoord = false;
                    }

                    coordNb++;
                    state = Trianglecontent.ENDING_CHEVRON;
                    break;
                }
                case ENDING_CHEVRON:
                {
                    context.callNextToken(); //passe le chevron fermant
                    if(context.isCurrentTokenAWord())
                    {
                        state = Trianglecontent.ATTRIBUTE;
                    }
                    else
                    {
                        state = Trianglecontent.ENDING_BRACKET;
                    }
                    break;
                }
                case ENDING_BRACKET:
                {
                    bracketNb--;
                    if(bracketNb == 0)
                    {
                        state = Trianglecontent.OUTSIDE;
                        break;
                    }
                    context.callNextToken();
                    if(context.isCurrentTokenAWord())
                    {
                        state = Trianglecontent.ATTRIBUTE;
                    }
                    break;
                }


                case ATTRIBUTE:
                {
                    material = super.parseAttributes(context);
                    state = Trianglecontent.OUTSIDE;
                    break;
                }
            }
        }
        triangle = new Triangle(vectA, vectB, vectC, material);
        return triangle;
    }
}
