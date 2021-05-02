package geometry.shapes;

import materials.Material;
import maths.Point;
import geometry.Shape;
import geometry.ArbitraryTriangleShape;

import java.util.ArrayList;


public class Pyramide extends ArbitraryTriangleShape implements Shape
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

    public Pyramide(Point A, Point B, Point C, Point D, Point E, Material material) 
    {
    	super(material);
    	
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;

        buildPyramide();
    }

    // Constructeur pour creer une pyramide equilaterale
    public Pyramide(Point depart, double height, double width, Material material) 
    {
    	super(material);
    	
        this.A = depart;

        this.C = new Point(this.A.getX() + width, this.A.getY(), this.A.getZ() + width);
        this.D = new Point(this.A.getX(), this.A.getY(), this.A.getZ() + width);
        this.B = new Point(this.A.getX() + width, this.A.getY(), this.A.getZ());
        this.E = new Point(this.A.getX() + width / 2, this.A.getY() + height, this.A.getZ() + width / 2);

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
        super.triangleList = new ArrayList<Triangle>();
        super.triangleList.add(tr1);
        super.triangleList.add(tr2);
        super.triangleList.add(tr3);
        super.triangleList.add(tr4);
        super.triangleList.add(tr5);
        super.triangleList.add(tr6);

    }
}