package maths;

/*
 * Cette classe représente la matrice permettant le changement de base des coordonnées des pixels en fonction de la position de la caméra et de sa direction
 */
public class CTWMatrix extends MatrixD
{
	public CTWMatrix(Point cameraOrigin, Point cameraDirection)
	{
		super(4, 3);
		
		Vector magicVector = new Vector(new Point(0, 0, 0), new Point(0, 1, 0));
		Vector zAxis = Vector.normalize(new Vector(cameraDirection, cameraOrigin));
		Vector xAxis = Vector.normalize(Vector.crossProduct(magicVector, zAxis));
		Vector yAxis = Vector.crossProduct(zAxis, xAxis);

		
		
		
		super.matrix[0][0] = xAxis.getX();
		super.matrix[0][1] = xAxis.getY();
		super.matrix[0][2] = xAxis.getZ();
		
		super.matrix[1][0] = yAxis.getX();
		super.matrix[1][1] = yAxis.getY();
		super.matrix[1][2] = yAxis.getZ();
		
		super.matrix[2][0] = zAxis.getX();
		super.matrix[2][1] = zAxis.getY();
		super.matrix[2][2] = zAxis.getZ();
		
		super.matrix[3][0] = cameraOrigin.getX();
		super.matrix[3][1] = cameraOrigin.getY();
		super.matrix[3][2] = cameraOrigin.getZ();
	}
}
