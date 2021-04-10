package geometry.shapes;

import materials.Material;
import maths.Point;
import geometry.ShapeUtil;

import java.util.ArrayList;

public class Prism extends ShapeUtil
{


    /*

    Imagine un prism

                         F
                        /\
                       / |\
                      /  | \
                     /   |  \
                    /    |   \
                  D/_____|____\C
                   |     |    |
                   |    E|    |
                   |    / \   |
                   |   /   \  |
                   |  /     \ |
                   | /       \|
                   |/_________\
                   A           B



                Prism couchee

                   E                   F
                   _____________________
                  /\                   /\
                 /  \                 /  \
                /    \               /    \
             A /______\_____________/D     \
                       \____________________\
                       B                    C




    */

	private Point A,B,C,D,E,F;


    public Prism(Point A, Point B, Point C, Point D, Point E, Point F, Material material)
    {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;
        this.F = F;

        super. material = material;

        buildPrism();

    }

    public Prism(Point depart, double height, double width, Material material)
    {
        this.A = depart;

        this.B = new Point(this.A.getX() + width, this.A.getY(), this.A.getZ());
        this.C = new Point(this.A.getX() + width, this.A.getY(), this.A.getZ() + width);
        this.D = new Point(this.A.getX(), this.A.getY(), this.A.getZ() + width);
        this.E = new Point(this.A.getX() + width/2, this.A.getY() + height, this.A.getZ());
        this.F = new Point(this.A.getX() + width/2, this.A.getY() + height, this.A.getZ() + width);

        super.material = material;

        buildPrism();
    }




    protected void buildPrism()
    {

        /*on va construire les 8 triangles*/
        Triangle tr1 = new Triangle(D,A,B, material);
        Triangle tr2 = new Triangle(D,B,C, material);
        Triangle tr3 = new Triangle(C,B,E, material);
        Triangle tr4 = new Triangle(C,E,F, material);
        Triangle tr5 = new Triangle(D,A,E, material);
        Triangle tr6 = new Triangle(D,E,F, material);
        Triangle tr7 = new Triangle(F,D,C, material);
        Triangle tr8 = new Triangle(E,B,A, material);

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

    }

    /*
	 * @link{geometry.shapes.Shape#getUVCoords}
	 */
	@Override
	public Point getUVCoords(Point point)
	{
		return null;
	}
}

