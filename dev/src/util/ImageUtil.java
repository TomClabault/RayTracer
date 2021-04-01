package util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

public class ImageUtil 
{
	/*
	 * Constante de gamma utilisée pour les conversion en sRGB non-linéaire et RGB linéaire
	 */
	public static final double GAMMA = 2.2;
	
	private static final int[] sRGBToLinearTable = ImageUtil.computesRGBToLinearTable();
	/*
	 * Permet de passer d'une image sRGB de gamma ImageUtil.GAMMA à son équivalent en RGB linéaire
	 * 
	 * @param sRGBImage L'image sRGB à convertir
	 * @param gamma		Valeur du gamma utilisée pour la courbe sRGB de l'image
	 * 
	 * @return Retourne l'image sRGBImage dans l'espace de couleur RGB linéaire
	 */
	public static Image sRGBImageToLinear(Image sRGBImage, double gamma)
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
				
				int rIntLinear = sRGBToLinearTable[rIntsRGB];
				int gIntLinear = sRGBToLinearTable[gIntsRGB];
				int bIntLinear = sRGBToLinearTable[bIntsRGB];
				
				Color outputLinearColor = Color.rgb(rIntLinear, gIntLinear, bIntLinear);
				
				outputPixelWriter.setColor(x, y, outputLinearColor);
			}
		}
		
		return outputLinear;
	}
	
	protected static int[] computesRGBToLinearTable()
	{
		int[] table = new int[256];
		
		for(int i = 0; i < 256; i++)
		{
			table[i] = (int)(Math.pow((double)i/256.0, 2.2)*256);
			table[i] = table[i] > 255 ? 255 : table[i];
		}
			
		return table;
	}
}
