package geometry.shapes;

import materials.Material;
import maths.Point;
import geometry.Shape;
import geometry.ArbitraryTriangleShape;

import java.util.ArrayList;

import exceptions.InvalidParallelepipedException;


public class Parallelepiped extends ArbitraryTriangleShape implements Shape
{

	private double height,width,length;
	private Point A, G;

	
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

														   y
		A = coin1                                          |
		G = coin2										   |____ x
														  /
														 z			( coordonnees x,y,z )


	 */

	public Parallelepiped(Point coin1, Point coin2, Material material)
	{
		super(material);
		
		if(coin1.getX() == coin2.getX()
		|| coin1.getY() == coin2.getY() 
		|| coin1.getZ() == coin2.getZ())//Si le rectangle est "plat"
			throw new InvalidParallelepipedException("Le parallelepipede que vous avez essaye de creer etait plat.");
			
		this.A = coin1;
		this.G = coin2;
		/*car les longeurs sont inconnus*/
		this.length = this.G.getX() - this.A.getX();
		this.width = this.G.getZ() - this.A.getZ();
		this.height = this.G.getY() - this.A.getY();
		
		this.buildParallelepiped();
	}

	public Parallelepiped(Point A, double height, double length, double width, Material material)
	{	/*ici coin1 peut etre considere comme le point de depart*/
		super(material);
		
		if(height == 0 || length == 0 || width == 0)
			throw new InvalidParallelepipedException("Le parallelepipede que vous avez essaye de creer etait plat.");
		
		this.A = A;
		this.height = height;
		this.length = length;
		this.width = width;
		/*car le coin2 est inconnu*/
		this.G.setX(this.A.getX() + this.length);
		this.G.setZ(this.A.getZ() + this.width);
		this.G.setY(this.A.getY() + this.height);

		this.buildParallelepiped();
	}





	protected void buildParallelepiped()
	{
		Point B, C, D, E, F, H;

		/*on localise les points selon les coordonnees de depart */
		B = new Point(A.getX() + length, A.getY(), A.getZ());
		C = new Point(A.getX()+ length, A.getY(), A.getZ() + width);
		D = new Point(A.getX(),A.getY(), A.getZ() + width);
		E = new Point(A.getX(),A.getY() + height, A.getZ());
		F = new Point(A.getX() + length,A.getY() + height, A.getZ());
		H = new Point(A.getX(), A.getY() + height, A.getZ() + width);

		/*on va construire les 12 triangles*/
		Triangle tr1 = new Triangle(E,A,B, material);
		Triangle tr2 = new Triangle(E,B,F, material);
		Triangle tr3 = new Triangle(F,B,C, material);
		Triangle tr4 = new Triangle(F,C,G, material);
		Triangle tr5 = new Triangle(G,C,D, material);
		Triangle tr6 = new Triangle(G,D,H, material);
		Triangle tr7 = new Triangle(H,D,A, material);
		Triangle tr8 = new Triangle(H,A,E, material);
		Triangle tr9 = new Triangle(D,A,B, material);
		Triangle tr10 = new Triangle(D,B,C, material);
		Triangle tr11 = new Triangle(H,E,F, material);
		Triangle tr12 = new Triangle(H,F,G, material);

		/*on ajoute dans le array des triangles*/
		super.triangleList = new ArrayList<Triangle>();
		super.triangleList.add(tr1);
		super.triangleList.add(tr2);
		super.triangleList.add(tr3);
		super.triangleList.add(tr4);
		super.triangleList.add(tr5);
		super.triangleList.add(tr6);
		super.triangleList.add(tr7);
		super.triangleList.add(tr8);
		super.triangleList.add(tr9);
		super.triangleList.add(tr10);
		super.triangleList.add(tr11);
		super.triangleList.add(tr12);
	}
	
	@Override
	public String toString()
	{
		return String.format("[Rectangle Shape] Coin 1: %s | Coin 2: %s | length, height, width: %.3f, %.3f, %.3f | Material: %s", this.A, this.G, this.length, this.height, this.width, this.material);
	}
}
