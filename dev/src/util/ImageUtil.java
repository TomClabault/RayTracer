package util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import maths.ColorOperations;

public class ImageUtil 
{
	/**
	 * Permet de passer d'une image sRGB dont les composantes des pixels sont non-lineaires de gamma 2.2 a son equivalent RGB lineaire
	 * 
	 * @param sRGBImage L'image sRGB gamma 2.2 a convertir
	 * 
	 * @return Retourne l'image sRGBImage convertie dans l'espace de couleur RGB lineaire
	 */
	public static Image sRGBImageToLinear(Image sRGBImage)
	{
		int width = (int)sRGBImage.getWidth();
		int height = (int)sRGBImage.getHeight();
		
		WritableImage outputLinear = new WritableImage(width, height);
		PixelWriter outputPixelWriter = outputLinear.getPixelWriter();
		PixelReader inputPixelReader = sRGBImage.getPixelReader();
		
		for(int y = 0; y < height; y++)
		{
			for(int x = 0; x < width; x++)
			{
				Color inputsRGBColor = inputPixelReader.getColor(x, y);
				
				int rIntsRGB = (int)(inputsRGBColor.getRed()*255);
				int gIntsRGB = (int)(inputsRGBColor.getGreen()*255);
				int bIntsRGB = (int)(inputsRGBColor.getBlue()*255);
				
				int rIntLinear = ColorOperations.sRGB2_2ToLinearTable[rIntsRGB];
				int gIntLinear = ColorOperations.sRGB2_2ToLinearTable[gIntsRGB];
				int bIntLinear = ColorOperations.sRGB2_2ToLinearTable[bIntsRGB];
				
				Color outputLinearColor = Color.rgb(rIntLinear, gIntLinear, bIntLinear);
				
				outputPixelWriter.setColor(x, y, outputLinearColor);
			}
		}
		
		return outputLinear;
	}
	
	/*
	 * Code de: https://stackoverflow.com/questions/47211852/using-filechooser-to-save-a-writableimage-image
	 */
	public static void writeWritableImageToDisk(WritableImage writableImage, File outputFile) throws IOException
	{
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(writableImage, null);
        ImageIO.write(bufferedImage, "png", outputFile);
	}
	
	/**
	 * Permet d'ecrire le rendu d'une scene JavaFX sur le disque
	 * 
	 * @param javaFXScene Scene javaFX dont on veut faire un instantane a sauvegarder sur le disque
	 * @param outputPath Le fichier dans lequel sauvegarder l'instantane
	 */
	public static void writeSceneToDiskPath(Scene javaFXScene, String outputPath) throws IOException
	{
		File output = new File(outputPath);
		writeSceneToDiskFile(javaFXScene, output);
	}
	
	/**
	 * Code de: https://stackoverflow.com/questions/34194427/javafx-2-save-crisp-snapshot-of-scene-to-disk
	 * 
	 * Permet d'ecrire le rendu d'une scene JavaFX sur le disque
	 * 
	 * @param javaFXScene Scene javaFX dont on veut faire un instantane a sauvegarder sur le disque
	 * @param output Le fichier dans lequel sauvegarder l'instantane
	 */
	public static void writeSceneToDiskFile(Scene javaFXScene, File output) throws IOException
	{
		try 
		{
			WritableImage wi = new WritableImage((int) javaFXScene.getWidth(), (int) javaFXScene.getHeight());
			WritableImage snapshot = javaFXScene.snapshot(wi);
			ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
