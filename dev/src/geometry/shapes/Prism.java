package geometry.shapes;

import geometry.materials.Material;
import geometry.materials.*;
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

public class Prism extends ShapeTriangle
{


    /*

    Imagine une prism

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

    protected double height,width;
    protected Point A,B,C,D,E,F;


    public Prism(Point A, Point B, Point C, Point D, Point E, Point F, Material material)
    {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;
        this.F = F;

        this. material = material;

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

        /*
        this.B.setX(this.A.getX() + width);
        this.B.setY(this.A.getY());
        this.B.setZ(this.A.getZ());

        this.C.setX(this.A.getX() + width);
        this.C.setY(this.A.getY());
        this.C.setZ(this.A.getZ() + width);

        this.D.setX(this.A.getX());
        this.D.setY(this.A.getY());
        this.D.setZ(this.A.getZ() + width);

        this.E.setX(this.A.getX() + width/2);
        this.E.setY(this.A.getY() + height);
        this.E.setZ(this.A.getZ());

        this.F.setX(this.A.getX() + width/2);
        this.F.setY(this.A.getY() + height);
        this.F.setZ(this.A.getZ() + width);
         */

        this.material = material;

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



}

