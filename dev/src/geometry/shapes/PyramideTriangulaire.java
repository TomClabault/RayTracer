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

public class PyramideTriangulaire implements ShapeTriangle
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
    protected ArrayList<Triangle> listeTriangle;
    private Material material;


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
        Triangle tr1 = new Triangle(A,B,D);
        Triangle tr2 = new Triangle(D,B,C);
        Triangle tr3 = new Triangle(D,C,A);
        Triangle tr4 = new Triangle(A,C,B); //ceci est le sol

        /*on va ajouter les triangles dans la liste des triangle*/
        listeTriangle = new ArrayList<Triangle>();
        this.listeTriangle.add(tr1);
        this.listeTriangle.add(tr2);
        this.listeTriangle.add(tr3);
        this.listeTriangle.add(tr4);

    }


    @Override
    public ArrayList<Triangle> getTriangleList()
    {
        return listeTriangle;
    }

    @Override
    public Material getMaterial()
    {
        return material;
    }

    @Override
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

    @Override
    public Point intersect(Ray ray)
    {
        ArrayList<Point> banque = new ArrayList<Point>();
        for (int i = 0; i < listeTriangle.size(); i++) {
            Point intersection = listeTriangle.get(i).intersect(ray);
            if(intersection != null)
            {
                banque.add(intersection);
            }

        }

        if(banque.size() == 0)
        {
            return null;
        }

        else if (banque.size() == 1)
        {
            return banque.get(0);
        }

        else
        {
            if (Point.distance(banque.get(0),ray.getOrigin()) < Point.distance(banque.get(1),ray.getOrigin()))
            {
                return banque.get(0);
            }
            else
            {
                return banque.get(1);
            }

        }
    }
}

