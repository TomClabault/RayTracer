package tests;

import maths.MatrixD;
import maths.Point;
import maths.RotationMatrix;

public class RotationMatricesTests
{
	public static void testRotate(int axis, double angle, Point toRotate, Point expected)
	{
		RotationMatrix rotM = new RotationMatrix(axis, angle);
		Point rotatedPoint = MatrixD.mulPointP(toRotate, rotM);

		System.out.println("Point rotate: " + rotatedPoint + " | Attendu: " + expected);
	}

	public static void main(String[] args)
	{
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
