package util;

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
	/*
	 * Permet de passer d'une image sRGB à composantes non-linéaires de gamma 2.2 à son équivalent RGB linéaire
	 * 
	 * @param sRGBImage L'image sRGB gamma 2.2 à convertir
	 * 
	 * @return Retourne l'image sRGBImage convertie dans l'espace de couleur RGB linéaire
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
	 * Code de: https://stackoverflow.com/questions/34194427/javafx-2-save-crisp-snapshot-of-scene-to-disk
	 * 
	 * Permet d'écrire le rendu d'une scène JavaFX sur le disque
	 * 
	 * @param javaFXScene Scène javaFX dont on veut faire un instantané à sauvegarder sur le disque
	 * @param outputFile Le fichier dans lequel sauvegarder l'instantané
	 */
	public static void writeSceneToDisk(Scene javaFXScene, String outputFile) throws IOException
	{
		try 
		{
			WritableImage wi = new WritableImage((int) javaFXScene.getWidth(), (int) javaFXScene.getHeight());
			WritableImage snapshot = javaFXScene.snapshot(wi);
			File output = new File(outputFile);
			ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
