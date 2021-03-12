package tests;

import maths.CTWMatrix;
import maths.MatrixD;
import maths.Point;

public class CTWMatrixTests 
{
	public static void main(String[] args) 
	{
		CTWMatrix rot90X = new CTWMatrix(new Point(0, 0, 0), -90, 90);
		System.out.println(rot90X);
		
		System.out.println(MatrixD.mulPoint(new Point(0, 0, -1), rot90X) + " | Expected : (1, 0, 0)");
	}
}
