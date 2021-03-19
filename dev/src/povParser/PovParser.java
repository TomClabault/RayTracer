package povParser;
import java.io.*;
import java.util.Iterator;
import java.util.Scanner;

public class PovParser
{
    private String pathName;
    private StreamTokenizer streamTokenizer;    

    public PovParser(String pathName)
    {
        this.pathName = pathName;
    }

    public File removeComments()
    {
        File povFile = new File("src/povParser/test2.pov");
        File originalFile = new File(this.pathName);
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

    public static void printFile(File file)
    {
        try
        {
            Scanner povReader = new Scanner(file);
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
        final String testFile = "src/povParser/temp.pov";
        PovParser povFile= new PovParser(testFile);
        File noComments = povFile.removeComments();
    }
}
