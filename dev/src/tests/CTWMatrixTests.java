package tests;

import maths.CTWMatrix;
import maths.MatrixD;
import maths.Vector3D;
import scene.Camera;

public class CTWMatrixTests 
{
	public static void main(String[] args) 
	{
		Camera camera = new Camera(new Vector3D(0, 0, 0), 0, 0);
		CTWMatrix rot90X = new CTWMatrix(camera, -90, 0);
		System.out.println(rot90X);
		
		System.out.println(MatrixD.mulPoint(new Vector3D(0, 0, -1), rot90X) + " | Expected : (1, 0, 0)");
	}
}
