package tests;

import maths.MatrixD;
import maths.Point;
import maths.RotationMatrix;

public class RotationMatricesTests 
{
	public static void testRotate(int axis, double angle, Point toRotate, Point expected)
	{
		RotationMatrix rotM = new RotationMatrix(axis, angle);
		Point rotatedPoint = MatrixD.mulPoint(toRotate, rotM);
		
		System.out.println("Point rotaté: " + rotatedPoint + " | Attendu: " + expected);
	}
	
	public static void main(String[] args)
	{
		double[][] random1 = new double[][]
		{
			{4, 1, 0},
			{2, 0, 3},
			{1, 8, 0}
		};
		double[][] random2 = new double[][]
		{
			{1, 2, 3},
			{6, 5, 0},
			{1, 0, 3}
		};
		double[][] identity = new double[][] { {1, 0, 0}, {0, 1, 0}, {0, 0, 1} };
		
		MatrixD id = new MatrixD(3, 3, identity); 
		MatrixD random1M = new MatrixD(3, 3, random1);
		MatrixD random2M = new MatrixD(3, 3, random2);
		
		MatrixD r1r2 = MatrixD.mulMatrix(random1M, random2M);
		MatrixD r2r1 = MatrixD.mulMatrix(random2M, random1M);
		MatrixD r1id = MatrixD.mulMatrix(random1M, id);
		MatrixD r2id = MatrixD.mulMatrix(random2M, id); 
		
		System.out.println(r1r2);
		System.out.println();
		System.out.println(r2r1);
		System.out.println();
		System.out.println(r1id);
		System.out.println();
		System.out.println(r2id);
		
		
		
		
		System.out.println("\n\nLa comparaison de points en nombre flottants n'est pas précise. A vous de comparer les résultats :D\n\n");
		
		testRotate(RotationMatrix.xAxis, 90, new Point(0, 0, 1), new Point(0, -1, 0));
		testRotate(RotationMatrix.xAxis, 45, new Point(0, 1, 0), new Point(0, 0.707, 0.707));
		testRotate(RotationMatrix.xAxis, 45, new Point(1, 0, 0), new Point(1, 0, 0));
		System.out.println();
		
		testRotate(RotationMatrix.yAxis, 90, new Point(0, 1, 0), new Point(0, 1, 0));
		testRotate(RotationMatrix.yAxis, 45, new Point(1, 0, 0), new Point(0.707, 0, -0.707));
		testRotate(RotationMatrix.yAxis, -90, new Point(1, 0, 0), new Point(0, 0, 1));
		testRotate(RotationMatrix.yAxis, 90, new Point(0, 1, 0), new Point(0, 1, 0));
		testRotate(RotationMatrix.yAxis, 90, new Point(1, 1, 0), new Point(0, 1, -1));
		System.out.println();
		
		testRotate(RotationMatrix.zAxis, 90, new Point(1, 0, 0), new Point(0, 1, 0));
		testRotate(RotationMatrix.zAxis, 45, new Point(1, 0, 0), new Point(0.707, 0.707, 0));
		testRotate(RotationMatrix.zAxis, -90, new Point(1, 0, 0), new Point(0, -1, 0));
		testRotate(RotationMatrix.zAxis, 90, new Point(0, 1, 0), new Point(-1, 0, 0));
		testRotate(RotationMatrix.zAxis, 90, new Point(1, 1, 0), new Point(-1, 1, 0));
		testRotate(RotationMatrix.zAxis, 90, new Point(0, 0, 1), new Point(0, 0, 1));
	}
}
