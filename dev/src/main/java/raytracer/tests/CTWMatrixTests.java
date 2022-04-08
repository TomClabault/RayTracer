package raytracer.tests;

import raytracer.maths.CTWMatrix;
import raytracer.maths.MatrixD;
import raytracer.maths.Point;
import raytracer.scene.Camera;

public class CTWMatrixTests 
{
	public static void main(String[] args) 
	{
		Camera camera = new Camera(new Point(0, 0, 0), 0, 0, 60);
		CTWMatrix rot90X = new CTWMatrix(camera, -90, 0);
		
		System.out.println(MatrixD.mulPointP(new Point(0, 0, -1), rot90X) + " | Expected : (1, 0, 0)");
	}
}
