package geometry.shapes;

import geometry.shapes.Triangle;
import geometry.Shape;
import geometry.Point;
import java.util.ArrayList;
import java.lang.Math;


public class Rectangle implements Shape
{

	protected double height,width,length,volume;
	protected float diagonal;
	protected Point coin1,coin2;
	protected ArrayList<Triangle> listeTriangle;

	public Rectangle(Point coin1, float height, float length, float width)
	{	/*ici coin1 peut etre considere comme le point de depart*/
		this.coin1 = coin1;
		this.height = height;
		this.length = length;
		this.width = width;
	}

	public Rectangle(Point coin1, Point coin2)
	{
		this.coin1 = coin1;
		this.coin2 = coin2;
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

		}
		else if(this.coin1 != null && this.length == length  && this.height == height && this.width == width)
		{
			/*si le coin est inconnu*/
			this.diagonal = (float) Math.sqrt(Math.pow(this.length, 2) + Math.pow(this.height, 2) + Math.pow(this.width, 2));
			this.coin2.setX(this.coin1.getX() + this.diagonal);
			this.coin2.setY(this.coin1.getY() + this.diagonal);
			this.coin2.setY(this.coin1.getZ() + this.diagonal);
			this.volume = this.length * this.height * this.width;
		}

	}

}
