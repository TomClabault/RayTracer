package tests;

import maths.MatrixD;
import maths.Vector3D;
import maths.RotationMatrix;

public class RotationMatricesTests
{
	public static void testRotate(int axis, double angle, Vector3D toRotate, Vector3D expected)
	{
		RotationMatrix rotM = new RotationMatrix(axis, angle);
		Vector3D rotatedPoint = MatrixD.mulPoint(toRotate, rotM);

		System.out.println("Vector3D rotaté: " + rotatedPoint + " | Attendu: " + expected);
	}

	public static void main(String[] args)
	{
		testRotate(RotationMatrix.xAxis, 90, new Vector3D(0, 0, 1), new Vector3D(0, -1, 0));
		testRotate(RotationMatrix.xAxis, 45, new Vector3D(0, 1, 0), new Vector3D(0, 0.707, 0.707));
		testRotate(RotationMatrix.xAxis, 45, new Vector3D(1, 0, 0), new Vector3D(1, 0, 0));
		System.out.println();

		testRotate(RotationMatrix.yAxis, 90, new Vector3D(0, 1, 0), new Vector3D(0, 1, 0));
		testRotate(RotationMatrix.yAxis, 45, new Vector3D(1, 0, 0), new Vector3D(0.707, 0, -0.707));
		testRotate(RotationMatrix.yAxis, -90, new Vector3D(1, 0, 0), new Vector3D(0, 0, 1));
		testRotate(RotationMatrix.yAxis, 90, new Vector3D(0, 1, 0), new Vector3D(0, 1, 0));
		testRotate(RotationMatrix.yAxis, 90, new Vector3D(1, 1, 0), new Vector3D(0, 1, -1));
		System.out.println();

		testRotate(RotationMatrix.zAxis, 90, new Vector3D(1, 0, 0), new Vector3D(0, 1, 0));
		testRotate(RotationMatrix.zAxis, 45, new Vector3D(1, 0, 0), new Vector3D(0.707, 0.707, 0));
		testRotate(RotationMatrix.zAxis, -90, new Vector3D(1, 0, 0), new Vector3D(0, -1, 0));
		testRotate(RotationMatrix.zAxis, 90, new Vector3D(0, 1, 0), new Vector3D(-1, 0, 0));
		testRotate(RotationMatrix.zAxis, 90, new Vector3D(1, 1, 0), new Vector3D(-1, 1, 0));
		testRotate(RotationMatrix.zAxis, 90, new Vector3D(0, 0, 1), new Vector3D(0, 0, 1));
	}
}
