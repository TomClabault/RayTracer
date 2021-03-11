package povParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
/*
class mainly used as a debug purpose

 */
public class FileUtility {

    public static String getFileContent(String pathToFile) {
        String fileContent = "";
        File povFile = new File(pathToFile);
        try {
            Scanner fileScanner = new Scanner(povFile);
            while (fileScanner.hasNextLine()) {
                fileContent += (fileScanner.nextLine());
                fileContent += "\n";
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return fileContent;
    }


    public static void printFileStdout(String pathToFile) {
        System.out.println(FileUtility.getFileContent(pathToFile));
    }

    public static boolean isFileEmpty(String pathToFile) {
        return (FileUtility.getFileContent(pathToFile).equals("")) ? true : false;
    }
}
