package geometry.shapes;

import geometry.shapes.Triangle;
import geometry.Shape;
import geometry.ShapeTriangle;
import geometry.Point;
import java.util.ArrayList;
import java.lang.Math;


public class Rectangle implements ShapeTriangle
{

	protected double height,width,length,volume;
	protected Point A,B,C,D,E,F,G,H;
	protected ArrayList<Triangle> listeTriangle;

	/*

		Imagine un bloc ABCD EFGH

										( coin2 )
						  H________________ G
						  /|			  /|
						E/_|____________F/ |
						|  |			|  |
						| D|____________|__|C
						| /				| /
						|/______________|/
					    A 				B
					( coin1 )


		A = coin1
		G = coin2


	 */

	public Rectangle(Point coin1, Point coin2)
	{
		this.A = coin1;
		this.G = coin2;
		/*car les longeurs sont inconnus*/
		this.length = this.G.getX() - this.A.getX();
		this.height = this.G.getZ() - this.A.getZ();
		this.width = this.G.getY() - this.A.getY();
	}

	public Rectangle(Point A, double height, double length, double width)
	{	/*ici coin1 peut etre considere comme le point de depart*/
		this.A = A;
		this.height = height;
		this.length = length;
		this.width = width;
		/*car le coin2 est inconnu*/
		this.G.setX(this.A.getX() + this.length);
		this.G.setY(this.A.getY() + this.width);
		this.G.setY(this.A.getZ() + this.height);
		this.volume = this.length * this.height * this.width;


	}





	public ArrayList<Triangle> buildRectangle()
	{

		/*on localise les points selon les */
		this.B = new Point(A.getX() + length, A.getY(), A.getZ());
		this.C = new Point(A.getX()+ length, A.getY() + width, A.getZ());
		this.D = new Point(A.getX(),A.getY() + width,A.getZ());
		this.E = new Point(A.getX(),A.getY(), A.getZ() + height);
		this.F = new Point(A.getX() + length,A.getY(),A.getZ() + height);
		this.H = new Point(A.getX(), A.getY() + width, A.getZ() + height);

		/*on va construire les 12 triangles*/
		Triangle tr1 = new Triangle(E,A,B);
		Triangle tr2 = new Triangle(E,F,B);
		Triangle tr3 = new Triangle(F,B,C);
		Triangle tr4 = new Triangle(F,G,C);
		Triangle tr5 = new Triangle(G,C,D);
		Triangle tr6 = new Triangle(G,H,D);
		Triangle tr7 = new Triangle(H,D,A);
		Triangle tr8 = new Triangle(H,E,A);
		Triangle tr9 = new Triangle(D,A,B);
		Triangle tr10 = new Triangle(D,C,B);
		Triangle tr11 = new Triangle(H,E,F);
		Triangle tr12 = new Triangle(H,G,F);

		/*on ajoute dans le array des triangles*/
		this.listeTriangle.add(tr1);
		this.listeTriangle.add(tr2);
		this.listeTriangle.add(tr3);
		this.listeTriangle.add(tr4);
		this.listeTriangle.add(tr5);
		this.listeTriangle.add(tr6);
		this.listeTriangle.add(tr7);
		this.listeTriangle.add(tr8);
		this.listeTriangle.add(tr9);
		this.listeTriangle.add(tr10);
		this.listeTriangle.add(tr11);
		this.listeTriangle.add(tr12);

		/*on retourne la liste des triangles*/
		return listeTriangle;
	}

}
