package povParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
/*
class mainly used as a debug purpose

 */
public class FileUtility
{

    public static String getFileContent(String pathToFile)
    {
        String FileContent = "";
        File povFile = new File(pathToFile);
        try
        {
            Scanner fileScanner = new Scanner(povFile);
            while (fileScanner.hasNextLine())
            {
                FileContent.concat(fileScanner.nextLine());
            }
        }
        catch(FileNotFoundException e)
        {
            System.err.println(e.getMessage());
        }
        return FileContent;
    }


    public static void printFileStdout(String pathToFile)
    {
        System.out.println(FileUtility.getFileContent(pathToFile));
    }

    public static void isFileEmpty(String pathToFile)
    {
        File povFile = new File(pathToFile);

    }

    public static void main(String[] args)
    {
        String fileContent = FileUtility.getFileContent("src/povParser/subsurface.pov");
        FileUtility.printFileStdout(fileContent);
    }
}
