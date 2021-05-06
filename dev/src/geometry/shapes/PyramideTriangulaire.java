package geometry.shapes;

import materials.Material;
import maths.Point;
import geometry.Shape;
import geometry.ArbitraryTriangleShape;

import java.util.ArrayList;

public class PyramideTriangulaire extends ArbitraryTriangleShape implements Shape
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

	private Point A,B,C,D;


    /**
    *  @param A Premier point de la base triangulaire de la pyramide
    *  @param B Deuxieme point de la base triangulaire de la pyramide
    *  @param C Troisieme point de la base triangulaire de la pyramide
    *  @param D sera le "point sommet" de la pyramide
    *  @param material Le materiau qui sera utilise pour le rendu de la pyramide
    */
    public PyramideTriangulaire(Point A, Point B, Point C, Point D, Material material)
    {
    	super(material);
    	
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;

        this.buildPyramide();
    }

    /* Constructeur pour construire une pyramide triangulaire equilaterale */
    public PyramideTriangulaire(Point depart, double height, double width, Material material)
    {
    	super(material);
    	
        this.A = depart;

        this.D = new Point(this.A.getX() + width/4, this.A.getY() + height, this.A.getZ() + width*3/4);
        this.B = new Point(this.A.getX() + width, this.A.getY(), this.A.getZ() + width);
        this.C = new Point(this.A.getX(), this.A.getY(), this.A.getZ() + width);

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
        super.triangleList = new ArrayList<Triangle>();
        super.triangleList.add(tr1);
        super.triangleList.add(tr2);
        super.triangleList.add(tr3);
        super.triangleList.add(tr4);

    }
}
