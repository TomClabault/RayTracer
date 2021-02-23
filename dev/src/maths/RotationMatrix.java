package maths;

public class RotationMatrix extends MatrixD 
{
	public static final int xAxis = 0;
	public static final int yAxis = 1;
	public static final int zAxis = 2;
	/*
	 * Crée une matrice de rotation autour d'un axe donné pour un certain angle de rotation
	 * 
	 * @param axis L'axe autour duquel on va effectuer la rotatio. 0 pour l'axe x, 1 pour y et 2 pour z. Utilisation des constantes définies dans la classe possible
	 * @param rotation L'angle de rotation en degré 
	 * 
	 * @throws IllegalArgumentException Jète cette exception si l'axe de rotation donné en argument n'est ni 'x' ni 'y' ni 'z'
	 */
	public RotationMatrix(int axis, double rotation)
	{
		super(3, 3);
		
		double rotationRad = Math.toRadians(rotation);
		
		switch (axis) 
		{
		case zAxis: 
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
		case xAxis:
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
			throw new IllegalArgumentException("L'axe de rotation de la matrice est incorrect. Doit être RotationMatrix.xAxis, RotationMatrix.yAxis ou RotationMatrix.zAxis");
		}
	}
}
