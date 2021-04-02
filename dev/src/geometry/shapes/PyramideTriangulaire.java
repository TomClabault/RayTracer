package geometry.shapes;

import materials.Material;
import maths.Point;
import geometry.ShapeTriangle;

import java.util.ArrayList;

public class PyramideTriangulaire extends ShapeTriangle
{

    /*
        Imagine un pyramide triangulaire

                 D
                /\
               / |\
              /  | \
             /   |  \
            /    |   \
           /     |    \
          /      |     \
       A /_______|______\ C
         \       |      /
          \      |     /
           \     |    /
            \    |   /
             \   |  /
              \  | /
               \ |/
                \/
                B

     */

    protected double height,width;
    protected Point A,B,C,D;


    /* Le constructeur prendra 4 parametres sous la forme de Point; Point A, Point B, Point C, Point D
    *  Point A, B et C sont le sol de la pyramide triangulaire
    *  Point D sera le toit de la pyramide
    *  Tout est deja explique sur le petit dessin au-dessus*/

    public PyramideTriangulaire(Point A, Point B, Point C, Point D, Material material)
    {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;

        this.material = material;

        this.buildPyramide();
    }

    /* Constructeur pour construire une pyramide triangulaire equilaterale */
    public PyramideTriangulaire(Point depart, double height, double width, Material material)
    {
        this.A = depart;

        this.D = new Point(this.A.getX() + width/4, this.A.getY() + height, this.A.getZ() + width*3/4);
        this.B = new Point(this.A.getX() + width, this.A.getY(), this.A.getZ() + width);
        this.C = new Point(this.A.getX(), this.A.getY(), this.A.getZ() + width);

        /*
        this.D.setX(this.A.getX() + width/4);
        this.D.setY(this.A.getY() + height);
        this.D.setZ(this.A.getZ() + width*3/4);


        this.B.setX(this.A.getX() + width);
        this.B.setY(this.A.getY());
        this.B.setZ(this.A.getZ() + width);

        this.C.setX(this.A.getX());
        this.C.setY(this.A.getY());
        this.C.setZ(this.A.getZ() + width);
        */

        this.material = material;

        this.buildPyramide();
    }


    protected void buildPyramide()
    {
        /*on va construire les 4 triangles*/
        Triangle tr1 = new Triangle(A,B,D, material);
        Triangle tr2 = new Triangle(D,B,C, material);
        Triangle tr3 = new Triangle(D,C,A, material);
        Triangle tr4 = new Triangle(A,C,B, material); //ceci est le sol

        /*on va ajouter les triangles dans la liste des triangle*/
        super.listeTriangle = new ArrayList<Triangle>();
        super.listeTriangle.add(tr1);
        super.listeTriangle.add(tr2);
        super.listeTriangle.add(tr3);
        super.listeTriangle.add(tr4);

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
