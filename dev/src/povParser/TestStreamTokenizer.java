package povParser;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

enum Figure
{
    SPHERE,
    RECTANGLE
}

/*
TODO:
se mettre dans le répertoire dev pour:
compilation: javac -d "./bin" src\povParser\TestStreamTokenizer.java
éxécution: java -cp "./bin;" povParser.TestStreamTokenizer

1) énumération des figures possibles et autres entités (caméra, light):
	-dès que le token correspond à une figure dans son contexte:
		faire un switch sur l'énum et récupérer la bonne forme
	-comme ça on ne prend pas en compte les figures non voulues	

2) énumération sur les symboles syntaxiques différents ('(', '{', '<' ...)
	-


3) mise en place du parser:
	-parcours le fichier sous forme de BufferedReader (autre choix déprécié) avec
	un streamTokenizer
	-vérification de l'état actuelle du je

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
                Figure formeRencontre = null; 
                if(streamTokenizer.ttype == StreamTokenizer.TT_WORD)
                {
                    if(streamTokenizer.sval.equals("sphere"))
                    {
                        System.err.println("sphere");
                        formeRencontre = Figure.SPHERE;
                        switch(formeRencontre)
                        {
                            case SPHERE:
                                System.out.println("parser encountered a sphere ");
                                break;
                            case RECTANGLE:
                                System.out.println("parser encountered a rectangle");
                                break;
                            default:
                                System.out.println("default");
                                break; 
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
