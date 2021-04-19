package geometry.shapes;

import materials.Material;
import materials.MatteMaterial;
import javafx.scene.paint.Color;
import maths.Point;
import geometry.Shape;
import geometry.ShapeTriangleUtil;

import java.util.ArrayList;

import exceptions.InvalidRectangleException;


public class Rectangle extends ShapeTriangleUtil implements Shape
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

	public Rectangle(Point coin1, Point coin2, Material material)
	{
		if(coin1.getX() == coin2.getX()
		|| coin1.getY() == coin2.getY() 
		|| coin1.getZ() == coin2.getZ())//Si le rectangle est "plat"
			throw new InvalidRectangleException("Le parallélépipède que vous avez essayé de créer était plat.");
			
		this.A = coin1;
		this.G = coin2;
		/*car les longeurs sont inconnus*/
		this.length = this.G.getX() - this.A.getX();
		this.width = this.G.getZ() - this.A.getZ();
		this.height = this.G.getY() - this.A.getY();
		super.material = material;
		
		this.buildRectangle();
	}

	public Rectangle(Point A, double height, double length, double width, Material material)
	{	/*ici coin1 peut etre considere comme le point de depart*/
		if(height == 0 || length == 0 || width == 0)
			throw new InvalidRectangleException("Le parallélépipède que vous avez essayé de créer était plat.");
		
		this.A = A;
		this.height = height;
		this.length = length;
		this.width = width;
		/*car le coin2 est inconnu*/
		this.G.setX(this.A.getX() + this.length);
		this.G.setZ(this.A.getZ() + this.width);
		this.G.setY(this.A.getY() + this.height);

		super.material = new MatteMaterial(Color.rgb(200, 200, 200));
		
		this.buildRectangle();
	}





	protected void buildRectangle()
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
		super.listeTriangle = new ArrayList<Triangle>();
		super.listeTriangle.add(tr1);
		super.listeTriangle.add(tr2);
		super.listeTriangle.add(tr3);
		super.listeTriangle.add(tr4);
		super.listeTriangle.add(tr5);
		super.listeTriangle.add(tr6);
		super.listeTriangle.add(tr7);
		super.listeTriangle.add(tr8);
		super.listeTriangle.add(tr9);
		super.listeTriangle.add(tr10);
		super.listeTriangle.add(tr11);
		super.listeTriangle.add(tr12);

		for(Triangle triangle : listeTriangle)
		{
			System.out.println(triangle + "normal = " + triangle.getNormal(null));
		}
		
		/*on retourne la liste des triangles*/
		/*return listeTriangle;*/
	}
	
	@Override
	public String toString()
	{
		return String.format("[Rectangle Shape] Coin 1: %s | Coin 2: %s | length, height, width: %.3f, %.3f, %.3f | Material: %s", this.A, this.G, this.length, this.height, this.width, this.material);
	}
}
