package maths;

import exceptions.IncompatibleMatricesException;

/**
 * Permet de créer des matrices de taille variable à coefficients réels
 */
public class MatrixD 
{
	private double matrix[][];
	
	private int m;
	private int n;
	
	/**
	 * Permet de créer une matrice de m lignes et n colonnes dont tous les coefficients sont 0
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
	 * Permet de créer une matrice de taille m*n et de l'initialisé avec le tableau de coefficients passé en argument
	 * 
	 * @param m Nombre de lignes de la matrice
	 * @param n Nombre de colonnes de la matrice
	 * @param coefficients Tableau de coefficients de taille m lignes et n colonnes utilisé pour initialiser les coefficients de la matrice créée
	 * 
	 * @throws IllegalArgumentException Jète cette exception si la taille du tableau de coefficient passé n'est pas la même que la taille de la matrice souhaitée (arguments m et n) 
	 */
	public MatrixD(int m, int n, double[][] coefficients)
	{
		if(coefficients.length == 0 || coefficients.length != m || coefficients[0].length != n)
			throw new IllegalArgumentException("La taille du tableau de coefficients passé en argument n'est pas la même que la taille de la matrice souhaitée");
		
		
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
	 * @param m1 Première matrice de taille m*p
	 * @param m2 Deuxième matrice de taille p*n
	 * 
	 * @return Retourne m1*m2 de taille m*n
	 * 
	 * @throws exceptions.IncompatibleMatricesException Jète cette exception si le nombre de colonnes de m1 n'est pas égal au nombre de lignes de m2. Les matrices ne peuvent pas être multipliée dans l'ordre donné
	 */
	public static MatrixD mulMatrix(MatrixD m1, MatrixD m2)
	{
		if(m1.n != m2.m)
			throw new IncompatibleMatricesException("Les deux matrices passées en argument ne peuvent pas être multipliées");
		
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
		
		//On retourne le point de coordoonées exprimées dans la base de la matrice passée en paramètre 
		return new Point(convertedPointCoords[0], convertedPointCoords[1], convertedPointCoords[2]);
	}
	
	/**
	 * Change la base de l'objet 'objectToConvert' vers la base de l'espace vectoriel de la caméra
	 * 
	 * @param objectToConvert Object dont les coordonnées doivent être converties vers la base de l'espace vectoriel de la caméra.
	 * @param transformMatrix La matrice de transformation qui sera utilisée pour le changement de base
	 * 
	 * @return Crée un nouveau point dont les coordoonnées (initialement de objectToConvert) sont exprimées dans la base de l'espace vectoriel de la caméra 
	 */
	public static Point mulPointP(CoordinateObject objectToConvert, MatrixD transformMatrix)
	{
		return mulPoint(objectToConvert, transformMatrix);
	}
	
	/**
	 * Change la base de l'objet 'objectToConvert' vers la base de l'espace vectoriel de la caméra
	 * 
	 * @param objectToConvert Object dont les coordonnées doivent être converties vers la base de l'espace vectoriel de la caméra.
	 * @param transformMatrix La matrice de transformation qui sera utilisée pour le changement de base
	 * 
	 * @return Un nouveau vecteur dont les coordoonnées (initialement de objectToConvert) sont exprimées dans la base de l'espace vectoriel de la caméra 
	 */
	public static Vector mulPointV(CoordinateObject objectToConvert, MatrixD transformMatrix)
	{
		Point pointMul = mulPoint(objectToConvert, transformMatrix);
		//On retourne le point de coordoonées exprimées dans la base de la matrice passée en paramètre 
		return new Vector(pointMul.getX(), pointMul.getY(), pointMul.getZ());
	}
	
	/**
	 * Redéfinition de la méthode toString pour afficher la matrice comme suit (exemple avec la matrice identité):
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
