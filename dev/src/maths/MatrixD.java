package maths;

import exceptions.IncompatibleMatricesException;

/**
 * Permet de creer des matrices de taille variable a coefficients reels
 */
public class MatrixD 
{
	protected double matrix[][];
	
	private int m;
	private int n;
	
	/**
	 * Permet de creer une matrice de m lignes et n colonnes dont tous les coefficients sont 0
	 * 
	 * @param m Nombre de lignes de la matrice
	 * @param n Nombre de colonnes de la matrice
	 */
	public MatrixD(int m, int n)
	{
		this.matrix = new double[m][n];
		this.m = m;
		this.n = n;
		
		for(int i = 0; i < m; i++)
			for(int j = 0; j < n; j++)
				this.matrix[i][j] = 0;
	}
	
	/**
	 * Permet de creer une matrice de taille m*n et de l'initialise avec le tableau de coefficients passe en argument
	 * 
	 * @param m Nombre de lignes de la matrice
	 * @param n Nombre de colonnes de la matrice
	 * @param coefficients Tableau de coefficients de taille m lignes et n colonnes utilise pour initialiser les coefficients de la matrice creee
	 * 
	 * @throws IllegalArgumentException Jete cette exception si la taille du tableau de coefficient passe n'est pas la meme que la taille de la matrice souhaitee (arguments m et n) 
	 */
	public MatrixD(int m, int n, double[][] coefficients)
	{
		if(coefficients.length == 0 || coefficients.length != m || coefficients[0].length != n)
			throw new IllegalArgumentException("La taille du tableau de coefficients passe en argument n'est pas la meme que la taille de la matrice souhaitee");
		
		
		this.matrix = new double[m][n];
		this.m = m;
		this.n = n;
		
		for(int i = 0; i < m; i++)
			for(int j = 0; j < n; j++)
				this.matrix[i][j] = coefficients[i][j];
	}
	
	/**
	 * Retourne le coefficient d'indice i, j de la matrice
	 * 
	 * @param i Ligne du coefficient que l'on veut
	 * @param j Colonne du coefficient que l'on veut
	 * 
	 * @return matrix[i][j]
	 */
	public double get(int i, int j)
	{
		return this.matrix[i][j];
	}
	
	/**
	 * Multiplie deux matrices
	 * 
	 * @param m1 Premiere matrice de taille m*p
	 * @param m2 Deuxieme matrice de taille p*n
	 * 
	 * @return Retourne m1*m2 de taille m*n
	 * 
	 * @throws exceptions.IncompatibleMatricesException Jete cette exception si le nombre de colonnes de m1 n'est pas egal au nombre de lignes de m2. Les matrices ne peuvent pas etre multipliee dans l'ordre donne
	 */
	public static MatrixD mulMatrix(MatrixD m1, MatrixD m2)
	{
		if(m1.n != m2.m)
			throw new IncompatibleMatricesException("Les deux matrices passees en argument ne peuvent pas etre multipliees");
		
		double[][] newCoeffs = new double[m1.m][m2.n];
		
		for(int i = 0; i < m1.m; i++)
			for(int j = 0; j < m2.n; j++)
				for(int k = 0; k < m1.n; k++)
					newCoeffs[i][j] += m1.get(i, k) * m2.get(k, j);
		
		return new MatrixD(m1.m, m2.n, newCoeffs);
	}
	
	protected static Point mulPoint(CoordinateObject objectToConvert, MatrixD transformMatrix)
	{
		double[] pointToConvertCoords = new double[] {objectToConvert.getX(), objectToConvert.getY(), objectToConvert.getZ(), 1};
		double[] convertedPointCoords = new double[] {0, 0, 0, 0};
		
		for(int i = 0; i < 4; i++)
			for(int j = 0; j < 4; j++)
				convertedPointCoords[i] += pointToConvertCoords[j] * transformMatrix.matrix[j][i];
		
		//On retourne le point de coordoonees exprimees dans la base de la matrice passee en parametre 
		return new Point(convertedPointCoords[0], convertedPointCoords[1], convertedPointCoords[2]);
	}
	
	/**
	 * Change la base de l'objet 'objectToConvert' vers la base de l'espace vectoriel de la camera
	 * 
	 * @param objectToConvert Object dont les coordonnees doivent etre converties vers la base de l'espace vectoriel de la camera.
	 * @param transformMatrix La matrice de transformation qui sera utilisee pour le changement de base
	 * 
	 * @return Cree un nouveau point dont les coordoonnees (initialement de objectToConvert) sont exprimees dans la base de l'espace vectoriel de la camera 
	 */
	public static Point mulPointP(CoordinateObject objectToConvert, MatrixD transformMatrix)
	{
		return mulPoint(objectToConvert, transformMatrix);
	}
	
	/**
	 * Change la base de l'objet 'objectToConvert' vers la base de l'espace vectoriel de la camera
	 * 
	 * @param objectToConvert Object dont les coordonnees doivent etre converties vers la base de l'espace vectoriel de la camera.
	 * @param transformMatrix La matrice de transformation qui sera utilisee pour le changement de base
	 * 
	 * @return Un nouveau vecteur dont les coordoonnees (initialement de objectToConvert) sont exprimees dans la base de l'espace vectoriel de la camera 
	 */
	public static Vector mulPointV(CoordinateObject objectToConvert, MatrixD transformMatrix)
	{
		Point pointMul = mulPoint(objectToConvert, transformMatrix);
		//On retourne le point de coordoonees exprimees dans la base de la matrice passee en parametre 
		return new Vector(pointMul.getX(), pointMul.getY(), pointMul.getZ());
	}
	
	/**
	 * Redefinition de la methode toString pour afficher la matrice comme suit (exemple avec la matrice identite):
	 * 
	 * 1.00 0.00 0.00 0.00
	 * 0.00 1.00 0.00 0.00
	 * 0.00 0.00 1.00 0.00
	 * 0.00 0.00 0.00 1.00
	 */
	public String toString()
	{
		String output = "";

		for(int i = 0; i < this.m; i++)
		{
			for(int j = 0; j < this.n; j++)
			{
				output += String.format("%5.02f ", this.matrix[i][j]);
			}
			output += "\n";
		}
		
		return output;
	}
}
