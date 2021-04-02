package maths;

import javafx.scene.paint.Color;

/*
 * Classe permettant d'effectuer des opérations sur les objets javafx.scene.paint.Color
 */
public class ColorOperations 
{
	/*
	 * Ajoute deux couleurs terme à terme
	 * 
	 * @param col1 Première couleur
	 * @param col2 Deuxième couleur
	 * 
	 * @return Un objet Color représentant la couleur obtenue à partir de l'addition terme à terme des deux couleurs passées en argument.
	 * Les composantes dépassant 255 lors de l'addition sont ramenées à 255 
	 */
	public static Color addColors(Color col1, Color col2)
	{
		int newRed = (int)((col1.getRed() + col2.getRed()) * 255); newRed = newRed > 255 ? 255 : newRed;
		int newGreen = (int)((col1.getGreen() + col2.getGreen()) * 255); newGreen = newGreen > 255 ? 255 : newGreen;
		int newBlue = (int)((col1.getBlue() + col2.getBlue()) * 255); newBlue = newBlue > 255 ? 255 : newBlue;
		
		return Color.rgb(newRed, newGreen, newBlue);
	}
	
	/*
	 * Ajoute un entier à la couleur composante par composante
	 * 
	 * @param col La couleur à laquelle on souhaite ajouter une constante
	 * @param addend La constante que l'on souhaite ajouter. Entier positif
	 * 
	 * @return Un objet Color.rgb() résultant de l'addition de chacune des composante de la couleur de départ avec la constante passée en argument.
	 * Les composantes 
	 * 
	 * @throws IllegalArgumentException Jète cette exception si la constante passée en argument est négative
	 */
	public static Color addToColor(Color col, int addend)
	{
		if(addend < 0)
			throw new IllegalArgumentException(String.format("La constante passée à addToColor est négative. addend = %d", addend));
		
		int newRed = (int)(col.getRed() * 255) + addend; newRed = newRed > 255 ? 255 : newRed;
		int newGreen = (int)(col.getGreen() * 255) + addend; newGreen = newGreen > 255 ? 255 : newGreen;
		int newBlue = (int)(col.getBlue() * 255) + addend; newBlue = newBlue > 255 ? 255 : newBlue;
		
		return Color.rgb(newRed, newGreen, newBlue);
	}
	
	/*
	 * Ajoute un entier à la couleur composante par composante
	 * 
	 * @param col La couleur à laquelle on souhaite ajouter une constante
	 * @param addend La constante que l'on souhaite ajouter. Réel entre 0 et 1
	 * 
	 * @return Un objet Color.rgb() résultant de l'addition de chacune des composante de la couleur de départ avec la constante passée en argument.
	 * Les composantes 
	 * 
	 * @throws IllegalArgumentException Jète cette exception si la constante passée en argument est négative
	 */
	public static Color addToColor(Color col, double addend)
	{
		if(addend < 0)
			throw new IllegalArgumentException(String.format("La constante passée à addToColor n'est pas dans l'intervalle demandé. addend = %.3f", addend));
		
		int newRed = (int)((col.getRed() + addend) * 255); newRed = newRed > 255 ? 255 : newRed;
		int newGreen = (int)((col.getGreen() + addend) * 255); newGreen = newGreen > 255 ? 255 : newGreen;
		int newBlue = (int)((col.getBlue() + addend) * 255); newBlue = newBlue > 255 ? 255 : newBlue;
		
		return Color.rgb(newRed, newGreen, newBlue);
	}
	
	/*
	 * Cette méthode converti la couleur passée en argument en entier 32 bits. Chaque composante de aRGB est codée sur 8 bits. 
	 * Du MSB au LSB: alpha ; red ; green ; blue
	 * 
	 * @param color La couleur que l'on souhaite convertir en entier
	 * 
	 * @return Retourne un entier 32 bits dont la signification des bits est (du MSB au LSB):
	 * 0-8: alpha
	 * 8-16: red
	 * 16-24: green
	 * 24-32: blue
	 */
	public static int aRGB2Int(Color color)
	{
		return  ((int)(color.getOpacity() * 255) << 24)  |
				((int)(color.getRed()     * 255) << 16) |
				((int)(color.getGreen()   * 255) << 8)  |
				((int)(color.getBlue()    * 255));
	}
	
	/*
	 * Retourne une nouvelle instance de Color représentant la même couleur que celle passée en argument
	 * 
	 * @param La couleur à copier
	 * 
	 * @return Une nouvelle instance de couleur dont les valeurs des composantes sont les même que colorToCopy 
	 */
	public static Color copy(Color colorToCopy) 
	{
		return Color.rgb((int)(colorToCopy.getRed()*255), (int)(colorToCopy.getGreen()*255), (int)(colorToCopy.getBlue()*255));
	}
	
	/*
	 * Multiplie une couleur composante par composante par un scalaire
	 * 
	 * @param col Couleur à multiplier
	 * @param scalar Le scalaire par lequel la couleur va être multipliée. Entre 0 et 1
	 * 
	 * @return Un objet Color.rgb() représentant la couleur obtenue à partir des multiplications des composantes de la couleur par le scalaire passé en argument
	 * Les composantes dépassant 255 lors de la multiplication sont ramenées à 255
	 * 
	 * @throws IllegalArgumentException Jète cette exception si le scalaire passé en argument est inférieur à 0
	 */
	public static Color mulColor(Color col, double scalar)
	{
		if(scalar < 0)
			throw new IllegalArgumentException(String.format("Le scalaire passé en argument de mulColors n'est pas dans l'intervalle demandé. scalar = %.3f", scalar));
		
		int newRed = (int)(col.getRed() * scalar * 255); newRed = newRed > 255 ? 255 : newRed;
		int newGreen = (int)(col.getGreen() * scalar * 255); newGreen = newGreen > 255 ? 255 : newGreen;
		int newBlue = (int)(col.getBlue() * scalar * 255); newBlue = newBlue > 255 ? 255 : newBlue;
		
		return Color.rgb(newRed, newGreen, newBlue);
	}
	
	/*
	 * Permet d'élever toutes les composantes d'une couleur à une certaine puissance
	 * 
	 * @param col 	La couleur à élever à une certaine puissance
	 * @param power L'exposant
	 * 
	 * @return Retourne une nouvelle couleur de composante (r^power, g^power, b^power) où ^ dénote l'exponentiation
	 */
	public static Color powColor(Color col, double power)
	{
		int newRed = (int)(Math.pow(col.getRed(), power)*255);
		int newGreen = (int)(Math.pow(col.getGreen(), power)*255);
		int newBlue = (int)(Math.pow(col.getBlue(), power)*255);
		
		return Color.rgb(newRed, newGreen, newBlue);
	}
	
	/*
	 * Soustrait deux couleurs terme à terme
	 * 
	 * @param col1 Première couleur
	 * @param col2 Deuxième couleur
	 * 
	 * @return Un objet Color.rgb() représentant la couleur obtenue à partir de la soustraction terme à terme des deux couleurs passées en argument.
	 * Les composantes inférieures à 0 lors de l'addition sont ramenées à 0
	 */
	public static Color subColors(Color col1, Color col2)
	{
		int newRed = (int)(col1.getRed() - col2.getRed()); newRed = newRed < 0 ? 0 : newRed;
		int newGreen = (int)(col1.getGreen() - col2.getGreen()); newGreen = newGreen < 0 ? 0 : newGreen;
		int newBlue = (int)(col1.getBlue() - col2.getBlue()); newBlue = newBlue < 0 ? 0 : newBlue;
		
		return Color.rgb(newRed, newGreen, newBlue);
	}
}
