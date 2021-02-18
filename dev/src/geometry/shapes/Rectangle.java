package geometry.shapes;

import geometry.shapes.Triangle;
import geometry.Shape;
import geometry.Point;
import java.util.ArrayList;
import java.lang.Math;


public class Rectangle implements ShapeTriangle
{

	protected double height,width,length,volume;
	protected float diagonal;
	protected Point coin1,coin2,B,C,D,E,F,H;
	protected ArrayList<Triangle> listeTriangle;

	/*

		Imagine un bloc ABCD EFGH

						  H________________ coin2
						  /|			  /|
						E/_|____________F/ |
						|  |			|  |
						| D|____________|__|C
						| /				| /
						|/______________|/
					coin1				B

		A = coin1
		G = coin2


	 */

	public Rectangle(Point coin1, Point coin2)
	{
		this.coin1 = coin1;
		this.coin2 = coin2;
	}

	public Rectangle(Point coin1, double height, double length, double width)
	{	/*ici coin1 peut etre considere comme le point de depart*/
		this.coin1 = coin1;
		this.height = height;
		this.length = length;
		this.width = width;
	}





	public ArrayList<Triangle> buildRectangle()
	{
		if(this.coin1 != null && this.coin2 != null)
		{
			/*si les longeurs sont inconnus*/
			/*this.difference = this.coin2 - this.coin1;*/
			this.length = this.coin2.getX() - this.coin1.getX();
			this.height = this.coin2.getZ() - this.coin1.getZ();
			this.width = this.coin2.getY() - this.coin1.getY();

			/*on localise les points selon les */
			this.B = new Point(coin1.getX() + length, coin1.getY(), coin1.getZ());
			this.C = new Point(coin1.getX()+ length, coin1.getY() + width, coin1.getZ());
			this.D = new Point(coin1.getX(),coin1.getY() + width,coin1.getZ());
			this.E = new Point(coin1.getX(),coin1.getY(), coin1.getZ() + height);
			this.F = new Point(coin1.getX() + length,coin1.getY(),coin1.getZ() + height);
			this.H = new Point(coin1.getX(), coin1.getY() + width, coin1.getZ() + height);

			/*on va construire les 12 triangles*/
			Triangle tr1 = new Triangle(E,coin1,B);
			Triangle tr2 = new Triangle(E,F,B);
			Triangle tr3 = new Triangle(F,B,C);
			Triangle tr4 = new Triangle(F,coin2,C);
			Triangle tr5 = new Triangle(coin2,C,D);
			Triangle tr6 = new Triangle(coin2,H,D);
			Triangle tr7 = new Triangle(H,D,coin1);
			Triangle tr8 = new Triangle(H,E,coin1);
			Triangle tr9 = new Triangle(D,coin1,B);
			Triangle tr10 = new Triangle(D,C,B);
			Triangle tr11 = new Triangle(H,E,F);
			Triangle tr12 = new Triangle(H,coin2,F);

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


		}
		else if(this.coin1 != null && this.length == length  && this.height == height && this.width == width)
		{
			/*si le coin2 est inconnu*/
			this.diagonal = (float) Math.sqrt(Math.pow(this.length, 2) + Math.pow(this.height, 2) + Math.pow(this.width, 2));
			this.coin2.setX(this.coin1.getX() + this.diagonal);
			this.coin2.setY(this.coin1.getY() + this.diagonal);
			this.coin2.setY(this.coin1.getZ() + this.diagonal);
			this.volume = this.length * this.height * this.width;

			/*on localise les points selon les */
			this.B = new Point(coin1.getX() + length, coin1.getY(), coin1.getZ());
			this.C = new Point(coin1.getX()+ length, coin1.getY() + width, coin1.getZ());
			this.D = new Point(coin1.getX(),coin1.getY() + width,coin1.getZ());
			this.E = new Point(coin1.getX(),coin1.getY(), coin1.getZ() + height);
			this.F = new Point(coin1.getX() + length,coin1.getY(),coin1.getZ() + height);
			this.H = new Point(coin1.getX(), coin1.getY() + width, coin1.getZ() + height);

			/*on va construire les 12 triangles*/
			Triangle tr1 = new Triangle(E,coin1,B);
			Triangle tr2 = new Triangle(E,F,B);
			Triangle tr3 = new Triangle(F,B,C);
			Triangle tr4 = new Triangle(F,coin2,C);
			Triangle tr5 = new Triangle(coin2,C,D);
			Triangle tr6 = new Triangle(coin2,H,D);
			Triangle tr7 = new Triangle(H,D,coin1);
			Triangle tr8 = new Triangle(H,E,coin1);
			Triangle tr9 = new Triangle(D,coin1,B);
			Triangle tr10 = new Triangle(D,C,B);
			Triangle tr11 = new Triangle(H,E,F);
			Triangle tr12 = new Triangle(H,coin2,F);

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

		}

		/*on retourne la liste des triangles*/
		return listeTriangle;
	}

}
