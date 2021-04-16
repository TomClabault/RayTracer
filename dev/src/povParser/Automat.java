package povParser;


import geometry.Shape;
import scene.Camera;
import scene.RayTracingScene;
import scene.lights.PositionnalLight;

import java.io.*;

public class Automat
{
    /*TODO
        -ajout de la javadoc
     */

    /*
    ajout d'un élément de syntaxe size pov dans un contexte checker pour ajouter une taille au damier:
        pigment {checker color1, color2, size}
     */

    /**
     * état courant
     */
    private EtatToken etatToken;

    /**
     * instance de StreamTokenizer utilisée pour effectuer le parsing
     */
    private StreamTokenizer streamTokenizer;

    /**
     * état de figure courant, c'est une énumération de type State
     */
    private State currentState;

    /**
     * jeton courant du StreamTokenizer, c'est un entier qui peut être cast en char pour récupérer des charactères (comme '<', '{')
     */
    private int currentToken;

    /**
     * Constructeur complet d'Automat
     * @param streamTokenizer Une instance de classe StreamTokenizer qui va servir de base à l'automat
     * @param currentState état courant de Figure
     */
    public Automat(StreamTokenizer streamTokenizer, State currentState)
    {
        this.streamTokenizer = streamTokenizer;
        this.currentState = currentState;
    }

    /**
     * Constructeur d'Automat qui omet l'état courant et qui l'initialise à OUTSIDE
     * @param streamTokenizer streamTokenizer servant à parser le fichier
     */
    public Automat(StreamTokenizer streamTokenizer)
    {
        this(streamTokenizer, State.OUTSIDE);
    }

    /**
     * Mutateur servant à fixer la valeur de l'état courant (qui est une sous classe de EtatToken) qui va être prochainement parsé
     * @param etatToken Classe d'état qui implémente EtatToken
     */
    public void setState(EtatToken etatToken)
    {
        this.etatToken = etatToken;
    }

    /**
     * Méthode servant à lancer le parsing de la figure courante, c'est à dire d'appeler la méthode action de la classe d'état courante
     * @return Elle retourne l'objet résultant du parsing, ensuite casté en une figure, lumière etc. (polymorphisme)
     */
    public Object action()
    {
        return etatToken.action(this);
    }

    /**
     * Accesseur donnant la valeur du streamTokenizer
     * @return Retourne l'attribut streamTokenizer
     */
    public StreamTokenizer getStreamTokenizer()
    {
        return this.streamTokenizer;
    }

    /**
     * Cette méthode teste si le type du streamTokenizer est un mot
     * @return retourne true si c'est un mot, false sinon
     */
    public boolean isCurrentTokenAWord()
    {
        return streamTokenizer.ttype == StreamTokenizer.TT_WORD;
    }

    /**
     * Cette méthode teste si un mot donné est égal au jeton courant du streamTokenizer
     * @param word le mot à tester
     * @return retourne true si le mot passé en argument est égal au jeton courant, false sinon
     */
    public boolean currentWord(String word)
    {
        return streamTokenizer.sval.equals(word);
    }

    /**
     * Méthode appelant simplement le prochain jeton dans le fichier
     * @return retourne l'entier correspondant au jeton
     */
    public int callNextToken()
    {
        try {
            this.currentToken = this.streamTokenizer.nextToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            return this.currentToken;
        }
    }

    /**
     * Mutateur fixant la valeur de l'attribut streamTokenizer
     * @param st
     */
    public void setStreamTokenizer(StreamTokenizer st)
    {
        this.streamTokenizer = st;
    }

    /**
     * Cette méthode renvoit la valeur du nombre parsée
     * @return nombre parsé
     */
    public double getNumberValue()
    {
        return this.streamTokenizer.nval;
    }

    /**
     * Cette méthode teste si le jeton courant est une figure valide
     * @return true si la figure est valide, false sinon
     */
    public boolean isValidState()
    {
        try {
            this.currentToken = this.streamTokenizer.nextToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(this.currentToken == StreamTokenizer.TT_WORD)
        {
            if(this.streamTokenizer.sval.equals("sphere") ||
                    this.streamTokenizer.sval.equals("triangle") ||
                    this.streamTokenizer.sval.equals("box") ||
                    this.streamTokenizer.sval.equals("plane") ||
                    this.streamTokenizer.sval.equals("camera") ||
                    this.streamTokenizer.sval.equals("light_source")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Teste si on a atteint la fin du fichier
     * @return true si on a fini le parsing, false sinon
     */
    public boolean isFinished()
    {
        return (this.streamTokenizer.ttype == StreamTokenizer.TT_EOF);
    }

    /**
     * Acceseur donnant la valeur de l'attribut currentToken
     * @return
     */
    public int getCurrentToken()
    {
        return this.currentToken;
    }

    /**
     * Méthode donnant l'état courant (figure, light...) que l'on va parser
     * @return une constante de l'énumération State
     */
    public State getState()
    {
        if(this.streamTokenizer.sval.equals("sphere"))
        {
            return State.SPHERE;
        }
        else if(this.streamTokenizer.sval.equals("light_source"))
        {
            return State.LIGHT_SOURCE;
        }
        else if(this.streamTokenizer.sval.equals("triangle"))
        {
            return State.TRIANGLE;
        }
        else if(this.streamTokenizer.sval.equals("box"))
        {
            return State.BOX;
        }
        else if(this.streamTokenizer.sval.equals("plane"))
        {
            return State.PLANE;
        }
        else if(this.streamTokenizer.sval.equals("camera"))
        {
            return State.CAMERA;
        }
        return State.OUTSIDE;
    }

    public static RayTracingScene parsePov(String pathToFile)
    {
        RayTracingScene scene = new RayTracingScene();

        try {

            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(pathToFile));
            BufferedReader fileReader = new BufferedReader(inputStreamReader);

            StreamTokenizer streamTokenizer = new StreamTokenizer(fileReader);
            streamTokenizer.wordChars('_', '_'); //permet d'avoir des noms contenant un underscore
            streamTokenizer.commentChar('#');
            streamTokenizer.slashStarComments(true);

            Automat automat = new Automat(streamTokenizer);

            while(! automat.isFinished())
            {
                if(automat.isValidState())
                {
                    State currentState = automat.getState();

                    switch (currentState) {

                        case LIGHT_SOURCE: {
                            automat.setState(new EtatLightSource());
                            PositionnalLight light = (PositionnalLight) automat.action();
                            scene.addLight(light);
                            break;
                        }

                        case SPHERE: {
                            automat.setState(new EtatSphere());
                            Shape sphere = (Shape) automat.action();
                            scene.addShape(sphere);
                            break;
                        }

                        case TRIANGLE: {
                            automat.setState(new EtatTriangle());
                            Shape triangle = (Shape) automat.action();
                            scene.addShape(triangle);
                            break;
                        }

                        case BOX: {
                            automat.setState(new EtatBox());
                            Shape rectangle = (Shape) automat.action();
                            scene.addShape(rectangle);
                            break;
                        }

                        case PLANE: {
                            automat.setState(new EtatPlane());
                            Shape plane = (Shape) automat.action();
                            scene.addShape(plane);
                            break;
                        }

                        case CAMERA: {
                            automat.setState(new EtatCamera());
                            Camera camera = (Camera) automat.action();
                            scene.setCamera(camera);
                            break;
                        }

                        case OUTSIDE: {
                            automat.setState(new EtatOutside());
                            automat.action();
                            break;
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return scene;
    }
}

