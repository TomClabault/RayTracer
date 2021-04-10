package povParser.automat;


import java.io.*;

public class Automat
{
    /*TODO
    -parse les textures spéciales
    -parse cheker dans pigment
    -parse camera (attributs: locate, rotate, translate, look_at)
    -ajout de la javadoc
     */

    private EtatToken etatToken;
    private StreamTokenizer streamTokenizer;
    private State currentState;
    private int currentToken;

    public Automat(StreamTokenizer streamTokenizer, State currentState)
    {
        this.streamTokenizer = streamTokenizer;
        this.currentState = currentState;
    }

    public Automat(StreamTokenizer streamTokenizer)
    {
        this(streamTokenizer, State.OUTSIDE);
    }

    public void setState(EtatToken etatToken)
    {
        this.etatToken = etatToken;
    }
    public void action()
    {
        etatToken.action(this);
    }

    public StreamTokenizer getStreamTokenizer()
    {
        return this.streamTokenizer;
    }

    public boolean isCurrentTokenAWord()
    {
        return streamTokenizer.ttype == StreamTokenizer.TT_WORD;
    }

    public boolean currentWord(String word)
    {
        return streamTokenizer.sval.equals(word);
    }

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

    public void setStreamTokenizer(StreamTokenizer st)
    {
        this.streamTokenizer = st;
    }

    public double getNumberValue()
    {
        return this.streamTokenizer.nval;
    }

    public boolean isValidState()
    {
        int token = 0;
        try {
            token = this.streamTokenizer.nextToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(token == StreamTokenizer.TT_WORD)
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

    public boolean isFinished()
    {
        return (this.streamTokenizer.ttype == StreamTokenizer.TT_EOF);
    }

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

    public static void main(String[] args)
    {
        String pathToTestFile = "dev/src/povParser/sphere.pov";

        try {

            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(pathToTestFile));
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

                        case LIGHT_SOURCE:
                        {
                            System.out.println("LIGHT_SOURCE");
                            automat.setState(new EtatLightSource());
                            automat.action();
                            break;
                        }

                        case SPHERE:
                        {
                            System.out.println("SPHERE");
                            automat.setState(new EtatSphere());
                            automat.action();
                            break;
                        }

                        case TRIANGLE:
                        {
                            System.out.println("TRIANGLE");
                            automat.setState(new EtatTriangle());
                            automat.action();
                            break;
                        }

                        case BOX:
                        {
                            System.out.println("BOX");
                            automat.setState(new EtatBox());
                            automat.action();
                            break;
                        }

                        case PLANE:
                        {
                            System.out.println("PLANE");
                            automat.setState(new EtatPlane());
                            automat.action();
                            break;
                        }

                        case CAMERA:
                        {
                            System.out.println("CAMERA");
                            automat.setState(new EtatCamera());
                            automat.action();
                            break;
                        }

                        case OUTSIDE:
                        {
                            automat.setState(new EtatOutside());
                            automat.action();
                            break;
                        }
                        default:
                            System.out.println("not any state");
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

