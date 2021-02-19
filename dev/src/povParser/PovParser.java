package povParser;
import java.io.File;
import java.util.Iterator;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class PovParser
{
    private String pathName;
    private final char NEW_LINE = '\n';
    private final String SINGLE_LINE_COMMENT = "//";
    private final String START_MULTILINE_COMMENT = "/*";
    private final String END_MULTILINE_COMMENT = "*/";

    public PovParser(String povFile)
    {
        this.pathName = pathName;
    }

    public File removeComments()
    {
        String regexStartSingleLineComment = new String ("^" + SINGLE_LINE_COMMENT); //regex to find "//" at the beginning of a line
        File tempFile = new File("src/povParser/temp.pov");
        if(tempFile.exists())
        {
            try {
                Scanner fileScanner = new Scanner(tempFile);

                while(fileScanner.hasNextLine())
                {
                    String currentLine = fileScanner.nextLine();
                    if (!currentLine.contains(regexStartSingleLineComment))
                    {

                    }
                }
            }
            catch (FileNotFoundException e)
            {
                e.getMessage();
            }
        }
        File oldFile = new File("src/povParser/test.pov");
        try
        {
            Scanner povReader = new Scanner(oldFile);
            while (povReader.hasNextLine())
            {
                String currentLine = povReader.nextLine();
                if (! currentLine.matches(regexStartSingleLineComment))
                {
                    //TODO: a finir
                }
                System.out.println(currentLine);
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
        return null;//TODO: return new file without comments
    }

    public void printFile()
    {
        try
        {
            Scanner povReader = new Scanner(new File("src/povParser/test.pov"));
            while (povReader.hasNextLine())
            {
                String currentLine = povReader.nextLine();
                System.out.println(currentLine);
            }
        }
        catch(FileNotFoundException e)
        {
            System.out.println(e.getMessage());
        }
    }
    public static void main(String[] args)
    {
        final String testFile = "src/povParser/test.pov";
        PovParser povFile= new PovParser(testFile);
    }
}
