package maths;

/*
 * Cette classe représente la matrice permettant le changement de base des coordonnées des pixels en fonction de la position de la caméra et de sa direction
 */
public class CTWMatrix extends MatrixD
{
	public CTWMatrix(Point cameraOrigin, double angleX, double angleY)
	{
		super(4, 4);
		
		Point cameraDirectionPoint = new Point(0, 0, -1);
		
		Vector magicVector = new Vector(0, 1, 0);
		
		//Si jamais l'axe z de la caméra est colinéaire au magic vector, on ne va pas 
		//pouvoir déterminer un vecteur perpendiculaire au deux qui nous donnerait l'axe x.
		//Il faut donc qu'on modifie le magicVector en le rendant non colinéaire à l'axe de regard de la caméra
		if(Vector.areColinear(magicVector, new Vector(cameraDirection, cameraOrigin))) 
			magicVector = new Vector(1, 1, 0);
			
		Vector zAxis = Vector.normalize(new Vector(cameraDirection, cameraOrigin));
		
		//System.out.println(magicVector.toString() + zAxis.toString());
		Vector xAxis = Vector.normalize(Vector.crossProduct(Vector.normalize(magicVector), zAxis));
		Vector yAxis = Vector.crossProduct(zAxis, xAxis);

		
		
		
		super.matrix[0][0] = xAxis.getX();
		super.matrix[0][1] = xAxis.getY();
		super.matrix[0][2] = xAxis.getZ();
		super.matrix[0][3] = 0;
		
		super.matrix[1][0] = yAxis.getX();
		super.matrix[1][1] = yAxis.getY();
		super.matrix[1][2] = yAxis.getZ();
		super.matrix[1][3] = 0;
		
		super.matrix[2][0] = zAxis.getX();
		super.matrix[2][1] = zAxis.getY();
		super.matrix[2][2] = zAxis.getZ();
		super.matrix[2][3] = 0;
		
		super.matrix[3][0] = cameraOrigin.getX();
		super.matrix[3][1] = cameraOrigin.getY();
		super.matrix[3][2] = cameraOrigin.getZ();
		super.matrix[3][3] = 1;
	}
}
