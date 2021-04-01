package util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import maths.ColorOperations;

public class ImageUtil 
{
	/*
	 * Permet de passer d'une image sRGB à son équivalent en RGB linéaire
	 * 
	 * @param sRGBImage L'image sRGB à convertir
	 * @param gamma		Valeur du gamma utilisée pour la courbe sRGB de l'image
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
				Color outputLinearColor = ColorOperations.powColor(inputsRGBColor, gamma);
				
				outputPixelWriter.setColor(x, y, outputLinearColor);
			}
		}
		
		return outputLinear;
	}
}
