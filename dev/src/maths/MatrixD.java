package maths;

import exceptions.IncompatibleMatricesException;

/*
 * Permet de créer des matrices de taille variable à coefficients réels
 */
public class MatrixD 
{
	double matrix[][];
	
	int m;
	int n;
	
	/*
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
	
	/*
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
	
	/*
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
	
	/*
	 * Multiplie deux matrices
	 * 
	 * @param m1 Première matrice de taille m*p
	 * @param m2 Deuxième matrice de taille p*n
	 * 
	 * @return Retourne m1*m2 de taille m*n
	 * 
	 * @throws IncompatibleMatrixException Jète cette exception si le nombre de colonnes de m1 n'est pas égal au nombre de lignes de m2. Les matrices ne peuvent pas être multipliée dans l'ordre donné
	 */
	public static MatrixD mulMatrix(MatrixD m1, MatrixD m2)
	{
		if(m1.n != m2.m)
			throw new IncompatibleMatricesException("Les deux matrices passées en argument ne peuvent pas être multipliées?");
		
		double[][] newCoeffs = new double[m1.m][m2.n];
		for(int i = 0; i < m1.m; i++)
			for(int j = 0; j < m2.n; j++)
				for(int k = 0; k < m1.n; k++)
					newCoeffs[i][j] = m1.get(i, k) + m2.get(k, j);
		
		return new MatrixD(m1.m, m2.n, newCoeffs);
	}
	
	/*
	 * Change la base du point pointToConvert vers la base de la caméra
	 * 
	 * @param pointToConvert Point à convertir vers la base de l'esapace vectoriel de la caméra.
	 * 
	 * @param Crée un nouveau point dont les coordoonnées (initialement de pointToConvert) sont exprimées selon l'orientation de la caméra 
	 */
	public Point mulPoint(Point pointToConvert)
	{
		double[] pointToConvertCoords = new double[] {pointToConvert.getX(), pointToConvert.getY(), pointToConvert.getZ()};
		double[] convertedPointCoords = new double[] {0, 0, 0};
		
		for(int i = 0; i < 3 ; i++)
			for(int j = 0; j < 4; j++)
				convertedPointCoords[i] += pointToConvertCoords[i] * this.matrix[j][i];
		
		return new Point(convertedPointCoords[0], convertedPointCoords[1], convertedPointCoords[2]);
	}
}
