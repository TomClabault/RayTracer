package povParser;

import javax.swing.plaf.synth.SynthTextAreaUI;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

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
        TestStreamTokenizer testFile = new TestStreamTokenizer("src/povParser/subsurface.pov");
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
                if(streamTokenizer.ttype == testFile.START_CURLY_BRACKET)
                {
                    System.out.println("{ encountered");
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
