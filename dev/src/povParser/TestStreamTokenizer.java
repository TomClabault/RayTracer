package povParser;

import javax.sound.midi.SysexMessage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

/*
TODO:

    1) énumération des figures possibles et autres entités (caméra, light): |
        -dès que le token correspond à une figure dans son contexte:        |  ?
            faire un switch sur l'énum et récupérer la bonne forme          |  ?
        -comme ça on ne prend pas en compte les figures non voulues	        |

    2) énumération sur les symboles syntaxiques différents ('(', '{', '<' ...)  |  ?

    3) mise en place du parser:
        -parcours le fichier sous forme de BufferedReader (autre choix déprécié) avec
        un streamTokenizer
        -vérification de l'état actuelle du jeton:
            -si le jeton est un mot et qu'il correspond à une figure que l'on peut traiter (constantes prédéfinies)
                -alors on appelle le thread parser correspondant à la bonne figure
                    -il parse jusqu'a la fermeture de la bonne accolade fermante qui termine le bloc de la figure (grâce à une stack)

    4) Un thread qui parse le texte un autre qui assigne un token à une classe en particulier
 */

public class TestStreamTokenizer
{    
    ArrayList<String> figureList;

    private String pathToPovFile;

    private final char NEW_LINE = '\n';
    private final String SINGLE_LINE_COMMENT = "//";
    private final String START_MULTILINE_COMMENT = "/*";
    private final String END_MULTILINE_COMMENT = "*/";

    private final char START_CURLY_BRACKET = '{';
    private final char END_CURLY_BRACKET = '}';
    private final char CROISILLON = '#';
    private final char START_CHEVRON = '<';
    private final char END_CHEVRON = '>';
    private final char UNDERSCORE = '_';

    public TestStreamTokenizer(String pathToPovFile)
    {
        this.pathToPovFile = pathToPovFile;
    }

    public String getPathToPovFile() {
        return pathToPovFile;
    }

    public void setPathToPovFile(String pathToPovFile) {
        this.pathToPovFile = pathToPovFile;
    }

    public void removeComments()
    {
        FileWriter povFile = null;
        try
        {
            povFile = new FileWriter("src/povParser/test.pov");
            File originalFile = new File(this.pathToPovFile);
            Scanner oldFileScanner = new Scanner(originalFile);
            BufferedWriter povFileWriter = new BufferedWriter(povFile);
            while (oldFileScanner.hasNextLine())
            {
                String currentLine = oldFileScanner.nextLine();
                currentLine = currentLine.replaceAll("//.*", "");
                currentLine = currentLine.replaceAll("/\\*.*\\*/", "");
                if(! currentLine.isBlank()) {
                    povFileWriter.write(currentLine);
                    povFileWriter.write("\n");
                }
            }
            povFileWriter.close();
        }
        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
        }
        catch (IOException e)
        {
            System.err.println(e.getMessage());
        }
        catch (Exception e)
        {
            System.err.println(e.getMessage());
        }
    }

    public static void main(String[] args)
    {
        String pathToTestFile = "src/povParser/test.pov";
        TestStreamTokenizer testFile = new TestStreamTokenizer("src/povParser/sphere.pov");
        testFile.removeComments();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(pathToTestFile));
            BufferedReader fileReader = new BufferedReader(inputStreamReader);

            StreamTokenizer streamTokenizer = new StreamTokenizer(fileReader);

            streamTokenizer.wordChars(testFile.UNDERSCORE, testFile.UNDERSCORE); //parse a variable name that contains an underscore (do not split it)
            streamTokenizer.commentChar(testFile.CROISILLON);

            int currentToken = streamTokenizer.nextToken();
            

            while(currentToken != StreamTokenizer.TT_EOF)
            {           
                Figure currentFigure = null;
                if(streamTokenizer.ttype == StreamTokenizer.TT_WORD)
                {

                    if(streamTokenizer.sval.equals(Figure.SPHERE.getFigureName()))
                    {
                        Stack<Character> bracketStack = new Stack<>();

                        String sphereContent = "";
                        currentToken = streamTokenizer.nextToken();
                        int lastToken = currentToken;
                        if((char) currentToken == '{')
                        {
                            System.out.println((char) currentToken);
                            bracketStack.push((char)currentToken);
                            /*TODO
                                appeler le thread SphereParser afin de parse la figure courante (sphere) sur place si possible
                                (afin d'éviter de le relire une deuxième fois pour créer une chaîne)
                             */
                            ArrayList<String> sphereAttributes = new ArrayList<>();
                            while(!bracketStack.isEmpty()) // figure content
                            {
                                currentToken = streamTokenizer.nextToken();
                                if ((char)currentToken == '{')
                                {
                                    bracketStack.push((char) currentToken);
                                }
                                else if((char) currentToken == '}')
                                {
                                    if(bracketStack.peek() == '{')
                                    {
                                        bracketStack.pop();
                                    }
                                }
                                else if(currentToken == '<') // ajout des coordonnées du centre et du rayon
                                {
                                    while((char) currentToken != '>')
                                    {
                                        if(streamTokenizer.ttype == StreamTokenizer.TT_NUMBER)
                                            sphereAttributes.add(String.valueOf(streamTokenizer.nval));
                                        currentToken = streamTokenizer.nextToken();
                                    }
                                    while(streamTokenizer.ttype != StreamTokenizer.TT_NUMBER)
                                    {
                                        currentToken = streamTokenizer.nextToken();
                                        if(streamTokenizer.ttype == StreamTokenizer.TT_NUMBER)
                                            sphereAttributes.add(String.valueOf(streamTokenizer.nval));
                                    }

                                }
                                /*else if (lastToken == '>') //rayon du cercle
                                {
                                    currentToken = streamTokenizer.nextToken();
                                    sphereAttributes.add(String.valueOf(currentToken));
                                }*/
                                else if(streamTokenizer.ttype == StreamTokenizer.TT_WORD)
                                {

                                }
                                else if(streamTokenizer.ttype == StreamTokenizer.TT_NUMBER)
                                {

                                }
                                System.out.println(sphereAttributes);
                            }
                        }
                    }
                }
                currentToken = streamTokenizer.nextToken();
            }
        }

        catch (FileNotFoundException e)
        {
            System.err.println(e.getMessage());
        }

        catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
