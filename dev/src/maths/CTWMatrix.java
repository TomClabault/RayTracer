package maths;

import scene.Camera;

/*
 * Cette classe représente la matrice permettant le changement de base des coordonnées des pixels en fonction de la position de la caméra et de ses angles de rotation
 */
public class CTWMatrix extends MatrixD
{
	public CTWMatrix(Camera camera, double angleHori, double angleVerti)
	{
		super(4, 4);
		
//		Point cameraPosition = camera.getPosition();
//		Point cameraDirectionPoint = Point.add(cameraPosition, new Point(0, 0, -1));
//		cameraDirectionPoint = MatrixD.mulPoint(cameraDirectionPoint, new RotationMatrix(1, angleHori));
//		cameraDirectionPoint = MatrixD.mulPoint(cameraDirectionPoint, new RotationMatrix(0, angleVerti));
//		
//		Vector magicVector = new Vector(0, 1, 0);
//
//		//Si jamais l'axe z de la caméra est colinéaire au magic vector, on ne va pas 
//		//pouvoir déterminer un vecteur perpendiculaire au deux qui nous donnerait l'axe x.
//		//Il faut donc qu'on modifie le magicVector en le rendant non colinéaire à l'axe de regard de la caméra
//		if(Vector.areColinear(magicVector, new Vector(cameraDirectionPoint, cameraPosition))) 
//			magicVector = new Vector(1, 1, 0);
//			
//		Vector zAxis = Vector.normalize(new Vector(cameraDirectionPoint, cameraPosition));
//		
//		//System.out.println(magicVector.toString() + zAxis.toString());
//		Vector xAxis = Vector.normalize(Vector.crossProduct(Vector.normalize(magicVector), zAxis));
//		Vector yAxis = Vector.crossProduct(zAxis, xAxis);

		MatrixD rotateMatrix = new MatrixD(3, 3, 
		new double[][] 
		{
			{1, 0, 0},
			{0, 1, 0},
			{0, 0, 1}		
		});
		
		RotationMatrix rotateHori = new RotationMatrix(1, angleHori);
		RotationMatrix rotateVerti = new RotationMatrix(0, angleVerti);
		
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
		rotateMatrix = MatrixD.mulMatrix(rotateMatrix, new MatrixD(3, 3, rotateVerti3x3));
		
		
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
//		super.matrix[0][0] = xAxis.getX();
//		super.matrix[0][1] = xAxis.getY();
//		super.matrix[0][2] = xAxis.getZ();
//		super.matrix[0][3] = 0;
//		
//		super.matrix[1][0] = yAxis.getX();
//		super.matrix[1][1] = yAxis.getY();
//		super.matrix[1][2] = yAxis.getZ();
//		super.matrix[1][3] = 0;
//		
//		super.matrix[2][0] = zAxis.getX();
//		super.matrix[2][1] = zAxis.getY();
//		super.matrix[2][2] = zAxis.getZ();
//		super.matrix[2][3] = 0;
//		
//		super.matrix[3][0] = cameraPosition.getX();
//		super.matrix[3][1] = cameraPosition.getY();
//		super.matrix[3][2] = cameraPosition.getZ();
//		super.matrix[3][3] = 1;
	}
}
