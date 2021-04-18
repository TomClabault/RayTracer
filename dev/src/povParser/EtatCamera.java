package povParser;

import maths.Point;
import scene.Camera;

/**
 * différents attributs que peut prendre la caméra ainsi que les éléments de syntaxe qui représentent différents états
 */
enum CameraContent
{
    OUTSIDE,
    OPENING_BRACKET,
    ENDING_BRACKET,
    LOCATION, //position
    DIRECTION,
    LOOK_AT, // orientation de la caméra
    ANGLE, // angle horizontal: fov (champ de vision)

}

/**
 * Classe représentant l'état camera, c'est à dire que le jeton courant est le mot
 * camera dans le fichier pov. C'est ici que tout le parsing de la camera est effectué.
 */
public class EtatCamera implements EtatToken
{

    /**
     * Cette fonction sert à parser une coordonnée de type c1, c2, c3 et retourne
     * le tableau des 3 composantes
     * @param context Le contexte courant de l'automate
     * @return Le tableau contenant les coordonnées parsées
     */
    public double[] parseAndGetCoord(Automat context)
    {
        double[] coordArray = new double[3];
        for (int i = 0; i < 3; i++)
        {
            context.callNextToken();
            coordArray[i] = context.getNumberValue();
            context.callNextToken();
        }
        context.callNextToken();
        return coordArray;
    }

    /**
     * méthode servant à donner le prochain état du jeton (OUTSIDE si aucun mot connue)
     * @param context
     * @return retourne l'état suivant du jeton
     */
    public CameraContent getNextAttribute(Automat context)
    {
        CameraContent cameraContent = CameraContent.ENDING_BRACKET;
        if(context.isCurrentTokenAWord())
        {
            if(context.currentWord("location"))
            {
                cameraContent = CameraContent.LOCATION;
            }
            else if(context.currentWord("direction"))
            {
                cameraContent = CameraContent.DIRECTION;
            }
            else if(context.currentWord("look_at"))
            {
                cameraContent = CameraContent.LOOK_AT;
            }
            else if(context.currentWord("angle"))
            {
                cameraContent = CameraContent.ANGLE;
            }
        }
        return cameraContent;
    }

    /**
     * Cette méthode effectue le parsing d'un objet camera
     * @param context contexte courant de l'automate
     */
    @Override
    public Camera action(Automat context)
    {
        Camera camera = new Camera();
        Point position = null;
        Point orientation = null;
        double angle = 0;
        int nbBracket = 0;

        context.callNextToken(); // skip "camera"
        CameraContent state = CameraContent.OPENING_BRACKET;

        while (state != CameraContent.OUTSIDE)
        {
            switch (state)
            {
                case OPENING_BRACKET:
                {
                    nbBracket++;
                    context.callNextToken(); //skip '{'
                    state = this.getNextAttribute(context);
                    break;
                }

                case ENDING_BRACKET:
                {
                    nbBracket--;
                    if (nbBracket == 0)
                        state = CameraContent.OUTSIDE;
                    else
                        state = this.getNextAttribute(context);
                    break;
                }

                case ANGLE:
                {
                    context.callNextToken(); // skip "angle"
                    angle = context.getNumberValue();
                    context.callNextToken();
                    state = this.getNextAttribute(context);
                    break;
                }

                case LOCATION:
                {
                    context.callNextToken(); // skip "location"
                    double[] coordArray = this.parseAndGetCoord(context);
                    state = this.getNextAttribute(context);
                    position = new Point(coordArray[0], coordArray[1], coordArray[2]);
                    break;
                }

                case LOOK_AT:
                {
                    context.callNextToken(); // skip "look_at"
                    double [] coordArray = this.parseAndGetCoord(context);
                    state = this.getNextAttribute(context);
                    orientation = new Point(coordArray[0], coordArray[1], coordArray[2]);
                    break;
                }

                case DIRECTION:
                {
                    context.callNextToken(); // skip "direction"
                    double [] coordArray = this.parseAndGetCoord(context);
                    state = this.getNextAttribute(context);
                    break;
                }
            }
        }
        
        return new Camera(position, orientation, angle);
    }

}
