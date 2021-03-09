package geometry.shapes;

import geometry.materials.Material;
import geometry.materials.MatteMaterial;
import geometry.shapes.Triangle;
import javafx.scene.paint.Color;
import maths.Point;
import geometry.Shape;
import geometry.ShapeTriangle;
import maths.Ray;
import maths.Vector;

import java.util.ArrayList;
import java.lang.Math;


public class Rectangle implements ShapeTriangle
{

	protected double height,width,length,volume;
	protected Point A,B,C,D,E,F,G,H;
	protected ArrayList<Triangle> listeTriangle;
	private Material material;

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
		this.A = coin1;
		this.G = coin2;
		/*car les longeurs sont inconnus*/
		this.length = this.G.getX() - this.A.getX();
		this.width = this.G.getZ() - this.A.getZ();
		this.height = this.G.getY() - this.A.getY();
		this.material = material;
		
		this.buildRectangle();
	}

	public Rectangle(Point A, double height, double length, double width, Material material)
	{	/*ici coin1 peut etre considere comme le point de depart*/
		this.A = A;
		this.height = height;
		this.length = length;
		this.width = width;
		/*car le coin2 est inconnu*/
		this.G.setX(this.A.getX() + this.length);
		this.G.setZ(this.A.getZ() + this.width);
		this.G.setY(this.A.getY() + this.height);
		this.volume = this.length * this.height * this.width;

		this.material = new MatteMaterial(Color.rgb(200, 200, 200));
		this.buildRectangle();
	}





	protected void buildRectangle()
	{

		/*on localise les points selon les coordonnees de depart */
		this.B = new Point(A.getX() + length, A.getY(), A.getZ());
		this.C = new Point(A.getX()+ length, A.getY(), A.getZ() + width);
		this.D = new Point(A.getX(),A.getY(), A.getZ() + width);
		this.E = new Point(A.getX(),A.getY() + height, A.getZ());
		this.F = new Point(A.getX() + length,A.getY() + height, A.getZ());
		this.H = new Point(A.getX(), A.getY() + height, A.getZ() + width);

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
		listeTriangle = new ArrayList<Triangle>();
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
		/*return listeTriangle;*/
	}

	public ArrayList<Triangle> getTriangleList()
	{
		return listeTriangle;
	}

	public Point intersect(Ray ray, Vector outNormalAtInter)
	{
			Double distancemin = null;
			Point intersection = null;
			Triangle intersectedTriangle = null;
			
			for (int i = 0; i < listeTriangle.size(); i++)
			{
				intersection = listeTriangle.get(i).intersect(ray, null);
				if(intersection != null)
				{
					double distance = Point.distance(intersection, ray.getOrigin());
					if(distancemin == null || distance < distancemin )
					{
						distancemin = distance ;
						intersectedTriangle = listeTriangle.get(i);
					}
				}


			}
			if (outNormalAtInter != null)
			{
				if (intersectedTriangle != null)
				{
					outNormalAtInter.copyIn(intersectedTriangle.getNormal(null));
				}
			}
			return intersection;

	}
	public Vector getNormal(Point point)
	{
		for (int i = 0 ; i < listeTriangle.size() ;i++)
		{
			if (listeTriangle.get(i).insideOutsideTest(point) == true)
			{
				return listeTriangle.get(i).getNormal(point);
			}
		}
		return null;
	}

	public Material getMaterial()
	{
		return material;
	}
}
