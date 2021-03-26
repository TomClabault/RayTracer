package povParser.automat;

import java.io.*;

public class Automat
{
    private EnumFigure currentState;

    public Automat(EnumFigure currentState)
    {
        this.currentState = currentState;
        System.out.println("classe automat");
    }

    public static void main(String[] args)
    {
        String pathToPov = "src/povParser/test.pov";
        try
        {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(pathToPov));
            BufferedReader fileReader = new BufferedReader(inputStreamReader);
            StreamTokenizer streamTokenizer = new StreamTokenizer(fileReader);

            streamTokenizer.wordChars(testFile.UNDERSCORE, testFile.UNDERSCORE);
            streamTokenizer.commentChar(testFile.CROISILLON);

            int token = streamTokenizer.nextToken();

            Etat state = Etat.EXTERIEUR;

            while(token != StreamTokenizer.TT_EOF)
            {
                switch(state)
                {
                    case EXTERIEUR:
                    {
                        if (streamTokenizer.ttype == StreamTokenizer.TT_WORD)
                        {
                            if(streamTokenizer.sval == "sphere")
                            {
                                state = ;
                            }
                        }
                        break;
                    }
                    case Etat.SPHERE.SPHERE:
                    {
                        token = streamTokenizer.nextToken();
                        state = ;
                        break;
                    }

                    case EnumFigure.SPH

                }
            }
        }

        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
        }

        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
    }
}
