package povParser;

import java.io.*;
import java.util.Scanner;

public class TestStreamTokenizer
{
    private String pathToPovFile;
    private final char NEW_LINE = '\n';
    private final String SINGLE_LINE_COMMENT = "//";
    private final String START_MULTILINE_COMMENT = "/*";
    private final String END_MULTILINE_COMMENT = "*/";

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

    public File removeComments()
    {
        File povFile = new File("src/povParser/test.pov");
        File originalFile = new File(this.pathToPovFile);
        try
        {
            Scanner oldFileScanner = new Scanner(originalFile);
            BufferedWriter povFileWriter = new BufferedWriter(new FileWriter(povFile));
            while (oldFileScanner.hasNextLine())
            {
                String currentLine = oldFileScanner.nextLine();
                currentLine = currentLine.replaceAll("//.*", "");
                currentLine = currentLine.replaceAll("/\\*.*\\*/", "");
                if(! currentLine.isBlank())
                    povFileWriter.write(currentLine);
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
        return povFile;
    }


    public static void main(String[] args)
    {
        TestStreamTokenizer testStreamTokenizer = new TestStreamTokenizer("src/povParser/subsurface.pov");
        File witoutComments = testStreamTokenizer.removeComments();
        FileUtility.printFileStdout(witoutComments.getAbsolutePath());
    }
}
