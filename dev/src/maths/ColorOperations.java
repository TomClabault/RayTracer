package maths;

import javafx.scene.paint.Color;

/**
 * Classe permettant d'effectuer des opérations sur les objets javafx.scene.paint.Color
 */
public class ColorOperations 
{
	/*
	 * Tables de transposition utilisées pour accélérer les calculs de correction de gamma
	 */
	public static final int[] sRGB2_2ToLinearTable = ColorOperations.computeSRGBToLinearTable(2.2);
	public static final int[] sRGB2_4ToLinearTable = ColorOperations.computeSRGBToLinearTable(2.4);
	
	public static final int[] linearTosRGB2_2Table = ColorOperations.computeLinearToSRGBTable(2.4);
	public static final int[] linearTosRGB2_4Table = ColorOperations.computeLinearToSRGBTable(2.4);
	
	/**
	 * Calcule la sRGBToLinearTable : table de transposition des valeurs sRGB de gamma 'gammaValue' vers RGB linéaire
	 * telle que sRGBToLinearTable[sRGBValue_gammaValue] = linearRGBValue avec:
	 * - sRGBValue_gammaValue la valeur d'intensité d'une composante sRGB dans l'espace de couleur sRGB corrigé avec un gamma de 'gammaValue'
	 * - _gammaValue indiquant avec quelle valeur de gamma la couleur sRGB a été encodée
	 * - linearRGBValue la valeur de sRGBValue_gammaValue mais dans l'espace de couleur RGB linéaire
	 * Pour le même gamma, cette table correspond à la table de transposition réciproque de linearTosRGBTable.
	 * 
	 * @param gammaValue Le valeur du gamma utilisé pour la conversion
	 * 
	 * @return Retourne la table de transposition appropriée et décrite ci-dessus
	 */
	protected static int[] computeSRGBToLinearTable(double gammaValue) throws IllegalArgumentException
	{
		int[] table = new int[256];
	
		if(gammaValue == 2.2)
		{
			for(int sRGB = 0; sRGB < 256; sRGB++)
			{
				table[sRGB] = (int)(Math.pow((double)sRGB/255.0, 2.2)*255);
				table[sRGB] = table[sRGB] > 255 ? 255 : table[sRGB];
			}
		}
		else if(gammaValue == 2.4)
		{
			//https://entropymine.com/imageworsener/srgbformula/
			
//			0 ≤ S ≤ 0.04045	L = S/12.92
//			0.04045 < S ≤ 1	L = ((S+0.055)/1.055)2.4
			
			for(int sRGB = 0; sRGB < 256; sRGB++)
			{
				double sRGBDouble = (double)sRGB/255.0;
				table[sRGB] = sRGBDouble <= 0.04045 ? (int)((sRGBDouble / 12.92)*255.0) : (int)(Math.pow(((sRGBDouble+0.055)/1.055), 2.4)*255.0);
				table[sRGB] = table[sRGB] > 255 ? 255 : table[sRGB];
			}
		}
			
		return table;
	}
	
	/**
	 * Calcule la linearToSRGBTable : table de transposition des valeurs RGB linéaire vers sRGB de gamma 'gammaValue'
	 * telle que linearToSRGBTable[linearRGBValue] = sRGBValue_gammaValue avec:
	 * - sRGBValue_gammaValue la valeur d'intensité d'une composante sRGB dans l'espace de couleur sRGB corrigé avec un gamma de 'gammaValue'
	 * - _gammaValue indiquant avec quelle valeur de gamma la couleur sRGB a été encodée
	 * - linearRGBValue la valeur de sRGBValue_gammaValue mais dans l'espace de couleur RGB linéaire
	 * Pour le même gamma, cette table correspond à la table de transposition réciproque de sRGBToLinearTable.
	 * 
	 * @param gammaValue Le valeur du gamma utilisé pour la conversion
	 * 
	 * @return Retourne la table de transposition appropriée et décrite ci-dessus
	 */
	protected static int[] computeLinearToSRGBTable(double gammaValue)
	{
		int[] table = new int[256];
		
		if(gammaValue == 2.2)
		{
			for(int linear = 0; linear < 256; linear++)
			{
				table[linear] = (int)(Math.pow((double)linear/255.0, 1.0/2.2)*255);
				table[linear] = table[linear] > 255 ? 255 : table[linear];
			}
		}
		else if(gammaValue == 2.4)
		{
			//https://entropymine.com/imageworsener/srgbformula/
			
//			0 ≤ L ≤ 0.0031308	S = L×12.92
//			0.0031308 < L ≤ 1	S = 1.055×L1/2.4 − 0.055
			
			for(int linear = 0; linear < 256; linear++)
			{
				double linearDouble = (double)linear/255.0;
				table[linear] = linearDouble <= 0.0031308 ? (int)((linearDouble * 12.92)*255.0) : (int)((1.055*Math.pow(linearDouble, 1.0/2.4) - 0.055)*255.0);
				table[linear] = table[linear] > 255 ? 255 : table[linear];
			}
		}
			
		return table;
	}
	
	/**
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
	
	/**
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
	
	/**
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
	
	/**
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
	
	/**
	 * Permet d'obtenir la représentation RGB d'une couleur
	 * 
	 * @param color La couleur dont on souhaite la représentation RGB sous forme de string
	 * 
	 * @return Retourne une chaîne de caractère représentant la couleur passé en argument sous la forme de ses 3 composantes
	 */
	public static String colorToString(Color color)
	{
		if(color == null)
			return "null";
		
		
		String output = "";
		
		output += "(";
		
		output += ((int)(color.getRed()*255) + ", ");
		output += ((int)(color.getGreen()*255) + ", ");
		output += ((int)(color.getBlue()*255));
		
		output += ")";
		
		return String.format("%-15s", output);
	}
	
	/**
	 * Retourne une nouvelle instance de Color représentant la même couleur que celle passée en argument
	 * 
	 * @param colorToCopy La couleur à copier
	 * 
	 * @return Une nouvelle instance de couleur dont les valeurs des composantes sont les même que colorToCopy 
	 */
	public static Color copy(Color colorToCopy) 
	{
		return Color.rgb((int)(colorToCopy.getRed()*255), (int)(colorToCopy.getGreen()*255), (int)(colorToCopy.getBlue()*255));
	}
	
	/**
	 * Permet de convertir une couleur dont les composantes sont utilisées linéairement en son équivalent sRGB avec une correction correction de gamma de paramètre 2.2
	 * 
	 * @param linearColor La couleur linéaire à convertir
	 * 
	 * @return Retourne la couleur donnée en entrée convertie en sRGB + gamma 2.2 
	 */
	public static Color linearTosRGBGamma2_2(Color linearColor)
	{
		int redInt = (int)(linearColor.getRed()*255);
		int greenInt = (int)(linearColor.getGreen()*255);
		int blueInt = (int)(linearColor.getBlue()*255);
		
		int newRed = ColorOperations.linearTosRGB2_2Table[redInt];
		int newGreen = ColorOperations.linearTosRGB2_2Table[greenInt];
		int newBlue = ColorOperations.linearTosRGB2_2Table[blueInt];
		
		return Color.rgb(newRed, newGreen, newBlue);//Gamma correction 2.2
	}
	
	/**
	 * Permet de convertir une couleur dont les composantes sont utilisées linéairement en son équivalent sRGB avec une correction correction de gamma de paramètre 2.4
	 * 
	 * @param linearColor La couleur linéaire à convertir
	 * 
	 * @return Retourne la couleur donnée en entrée convertie en sRGB + gamma 2.4 
	 */
	public static Color linearTosRGBGamma2_4(Color linearColor)
	{
		//Gamma correction 2.4
		int redInt = (int)(linearColor.getRed()*255);
		int greenInt = (int)(linearColor.getGreen()*255);
		int blueInt = (int)(linearColor.getBlue()*255);
		
		int newRed = ColorOperations.linearTosRGB2_4Table[redInt];
		int newGreen = ColorOperations.linearTosRGB2_4Table[greenInt];
		int newBlue = ColorOperations.linearTosRGB2_4Table[blueInt];
		
		return Color.rgb(newRed, newGreen, newBlue);
	}
	
	/**
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
	
	//TODO (tom) doc
	public static Color mulColorVector(Color colorToMul, Vector vector)
	{
		int newRed = (int)(colorToMul.getRed() * vector.getX() * 255); newRed = newRed > 255 ? 255 : newRed;
		int newGreen = (int)(colorToMul.getGreen() * vector.getY() * 255); newGreen = newGreen > 255 ? 255 : newGreen;
		int newBlue = (int)(colorToMul.getBlue() * vector.getZ()  * 255); newBlue = newBlue > 255 ? 255 : newBlue;
		
		return Color.rgb(newRed, newGreen, newBlue);
	}
	
	/**
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
	
	/**
	 * Permet de convertir une couleur de l'espace sRGB avec une courbe de Gamma de 2.2 vers l'espace RGB linéaire
	 * 
	 * @param sRGB2_2Color La couleur que l'on veut convertir dont les composantes ne sont pas linéaires
	 * 
	 * @return Retourne la couleur passée en paramètre mais convertie dans l'espace de couleur RGB linéaire
	 */
	public static Color sRGBGamma2_2ToLinear(Color sRGB2_2Color)
	{
		int intRed = (int)(sRGB2_2Color.getRed()*255);
		int intGreen = (int)(sRGB2_2Color.getGreen()*255);
		int intBlue = (int)(sRGB2_2Color.getBlue()*255);
		
		return Color.rgb(ColorOperations.sRGB2_2ToLinearTable[intRed], ColorOperations.sRGB2_2ToLinearTable[intGreen], ColorOperations.sRGB2_2ToLinearTable[intBlue]);
	}
	
	/**
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
