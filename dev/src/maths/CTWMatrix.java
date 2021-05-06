package maths;

import scene.Camera;

/**
 * Cette classe represente la matrice permettant le changement de base des coordonnees des pixels en fonction de la position de la camera et de ses angles de rotation
 */
public class CTWMatrix extends MatrixD
{
	public CTWMatrix(Camera camera, double angleHori, double angleVerti)
	{
		super(4, 4);
		
		MatrixD rotateMatrix = new MatrixD(3, 3, 
		new double[][] 
		{
			{1, 0, 0},
			{0, 1, 0},
			{0, 0, 1}		
		});
		
		RotationMatrix rotateVerti = new RotationMatrix(0, angleVerti);//Creation de la matrice de rotation pour l'axe vertical
		RotationMatrix rotateHori = new RotationMatrix(1, angleHori);//Creation de la matrice de rotation pour l'axe horizontal 
		
		double rotateHori3x3[][] = new double[3][3];
		double rotateVerti3x3[][] = new double[3][3];
		for(int line = 0; line < 3; line++)
		{
			for(int column = 0; column < 3; column++)
			{
				rotateHori3x3[line][column] = rotateHori.get(line, column);
				rotateVerti3x3[line][column] = rotateVerti.get(line, column);
			}
		}
		
		
		rotateMatrix = MatrixD.mulMatrix(rotateMatrix, new MatrixD(3, 3, rotateHori3x3));
		rotateMatrix = MatrixD.mulMatrix(new MatrixD(3, 3, rotateVerti3x3), rotateMatrix);
		
		
		for(int line = 0; line < 3; line++)
		{
			for(int column = 0; column < 3; column++)
				super.matrix[line][column] = rotateMatrix.get(line, column);
			super.matrix[line][3] = 0;
		}
		super.matrix[3][3] = 1;	
		
		super.matrix[3][0] = camera.getPosition().getX();
		super.matrix[3][1] = camera.getPosition().getY();
		super.matrix[3][2] = camera.getPosition().getZ();
	}
}
