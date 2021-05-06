package maths;

/**
 * Permet de creer une matrice de rotation autour d'un axe donne du monde pour un certain angle de rotation
 * Les "axes du monde" sont supposes etre les axes standards. C'est a dire:
 * X = (1, 0, 0)
 * Y = (0, 1, 0)
 * Z = (0, 0, 1)
 */
public class RotationMatrix extends MatrixD 
{
	/**
	 * Constante permettant de specifier l'axe X du monde
	 */
	public static final int xAxis = 0;
	
	/**
	 * Constante permettant de specifier l'axe Y du monde
	 */
	public static final int yAxis = 1;
	
	/**
	 * Constante permettant de specifier l'axe Z du monde
	 */
	public static final int zAxis = 2;

	public RotationMatrix(int axis, double rotation)
	{
		super(4, 4);
		
		double rotationRad = Math.toRadians(rotation);
		
		switch (axis) 
		{
		case xAxis: 
		{
			super.matrix[0][0] = 1;
			super.matrix[1][0] = 0;
			super.matrix[2][0] = 0;
			super.matrix[0][1] = 0;
			super.matrix[0][2] = 0;
			
			super.matrix[1][1] = Math.cos(rotationRad);
			super.matrix[1][2] = Math.sin(rotationRad);
			super.matrix[2][1] = -Math.sin(rotationRad);
			super.matrix[2][2] = Math.cos(rotationRad);
			
			break;
		}
		case yAxis:
		{
			super.matrix[0][1] = 0;
			super.matrix[1][0] = 0;
			super.matrix[1][1] = 1;
			super.matrix[1][2] = 0;
			super.matrix[2][1] = 0;
			
			super.matrix[0][0] = Math.cos(rotationRad);
			super.matrix[0][2] = -Math.sin(rotationRad);
			super.matrix[2][0] = Math.sin(rotationRad);
			super.matrix[2][2] = Math.cos(rotationRad);
			
			break;
		}
		case zAxis:
		{
			super.matrix[2][0] = 0;
			super.matrix[2][1] = 0;
			super.matrix[2][2] = 1;
			super.matrix[0][2] = 0;
			super.matrix[1][2] = 0;
			
			super.matrix[0][0] = Math.cos(rotationRad);
			super.matrix[0][1] = Math.sin(rotationRad);
			super.matrix[1][0] = -Math.sin(rotationRad);
			super.matrix[1][1] = Math.cos(rotationRad);
			
			break;
		}
		default:
			throw new IllegalArgumentException("L'axe de rotation de la matrice est incorrect. Doit etre RotationMatrix.xAxis, RotationMatrix.yAxis ou RotationMatrix.zAxis");
		}
		
		super.matrix[0][3] = 0;
		super.matrix[1][3] = 0;
		super.matrix[2][3] = 0;
		super.matrix[3][3] = 1;
		super.matrix[3][0] = 0;
		super.matrix[3][1] = 0;
		super.matrix[3][2] = 0;
	}
	
	public RotationMatrix(Vector axis, double degreeAngle) 
	{
		super(4, 4);
		
		double radAngle = Math.toRadians(degreeAngle);
		
		double x = axis.getX();
		double y = axis.getY();
		double z = axis.getZ();
		
		double cosAngle = Math.cos(radAngle);
		double cosAngleMin1 = 1 - cosAngle;
		double sinAngle = Math.sin(radAngle);
		
		//Matrice de: https://en.wikipedia.org/wiki/Rotation_matrix#General_rotations
		double rotatMatrix[][] = new double[][] 
		{
			{cosAngle + x * x * cosAngleMin1, x * y * cosAngleMin1 - z * sinAngle, x * z * cosAngleMin1 + y * sinAngle, 0},
			{y * x * cosAngleMin1 + z * sinAngle, cosAngle + y * y * cosAngleMin1, y * z * cosAngleMin1 - x * sinAngle, 0},
			{z * x * cosAngleMin1 - y * sinAngle, z * y * cosAngleMin1 + x * sinAngle, cosAngle + z * z * cosAngleMin1, 0},
			{0, 0, 0, 1}
		};
		
		super.matrix = rotatMatrix;
	}
}
