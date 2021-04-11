package geometry.shapes;

import materials.Material;
import maths.Point;
import geometry.Shape;
import geometry.ShapeTriangleUtil;

import java.util.ArrayList;


public class Pyramide extends ShapeTriangleUtil implements Shape
{
    /*Imagine une pyramide ABCDE


                        E
                        /\
                       /  \
                      /    \
                     /      \
                    /        \
                   /          \
                  /            \
                 /              \
                /                \
             D / ________________ \ C
              / /                \ \
             /______________________\
            A                        B

    Cette pyramide sera construite avec 6 Triangles.

     */

	private Point A, B, C, D, E;

    public Pyramide(Point A, Point B, Point C, Point D, Point E, Material material) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;

        super.material = material;

        buildPyramide();
    }

    // Constructeur pour creer une pyramide equilaterale
    public Pyramide(Point depart, double height, double width, Material material) {
        this.A = depart;

        this.C = new Point(this.A.getX() + width, this.A.getY(), this.A.getZ() + width);
        this.D = new Point(this.A.getX(), this.A.getY(), this.A.getZ() + width);
        this.B = new Point(this.A.getX() + width, this.A.getY(), this.A.getZ());
        this.E = new Point(this.A.getX() + width / 2, this.A.getY() + height, this.A.getZ() + width / 2);

        // b --> c , c --> d , d --> b

        /*
        this.B.setX(this.A.getX() + width);
        this.B.setY(this.A.getY());
        this.B.setZ(this.A.getZ() + width);

        this.C.setX(this.A.getX());
        this.C.setY(this.A.getY());
        this.C.setZ(this.A.getZ() + width);

        this.D.setX(this.A.getX() + width);
        this.D.setY(this.A.getY());
        this.D.setZ(this.A.getZ());

        this.E.setX(this.A.getX() + width/2);
        this.E.setY(this.A.getY() + height);
        this.E.setZ(this.A.getZ() + width/2);
        */

        super.material = material;

        buildPyramide();


    }


    protected void buildPyramide() {
        /*on va construire les 6 triangles*/
        Triangle tr1 = new Triangle(E, D, A, material);
        Triangle tr2 = new Triangle(E, A, B, material);
        Triangle tr3 = new Triangle(E, B, C, material);
        Triangle tr4 = new Triangle(E, C, D, material);
        Triangle tr5 = new Triangle(D, A, B, material);
        Triangle tr6 = new Triangle(D, B, C, material);

        /*on va ajouter les triangles dans la listeTriangle*/
        super.listeTriangle = new ArrayList<Triangle>();
        super.listeTriangle.add(tr1);
        super.listeTriangle.add(tr2);
        super.listeTriangle.add(tr3);
        super.listeTriangle.add(tr4);
        super.listeTriangle.add(tr5);
        super.listeTriangle.add(tr6);

    }
}