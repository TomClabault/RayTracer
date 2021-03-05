package geometry.shapes;

import geometry.materials.Material;
import geometry.shapes.Triangle;
import maths.Point;
import geometry.Shape;
import geometry.ShapeTriangle;
import maths.Ray;
import maths.Vector;

import java.util.ArrayList;
import java.lang.Math;

public class Prism implements ShapeTriangle
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
    protected ArrayList<Triangle> listeTriangle;
    private Material material;


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

        this.B.setX(this.A.getX() + width);
        this.B.setY(this.A.getY());
        this.B.setZ(this.A.getZ());

        this.C.setX(this.A.getX() + width);
        this.C.setY(this.A.getY() + width);
        this.C.setZ(this.A.getZ());

        this.D.setX(this.A.getX());
        this.D.setY(this.A.getY() + width);
        this.D.setZ(this.A.getZ());

        this.E.setX(this.A.getX() + width/2);
        this.E.setY(this.A.getY());
        this.E.setZ(this.A.getZ() + height);

        this.F.setX(this.A.getX() + width/2);
        this.F.setY(this.A.getY() + width);
        this.F.setZ(this.A.getZ() + height);

        this.material = material;

        buildPrism();
    }




    protected void buildPrism()
    {

        /*on va construire les 8 triangles*/
        Triangle tr1 = new Triangle(A,B,E);
        Triangle tr2 = new Triangle(D,C,F);
        Triangle tr3 = new Triangle(A,D,C);
        Triangle tr4 = new Triangle(A,B,C);
        Triangle tr5 = new Triangle(E,F,D);
        Triangle tr6 = new Triangle(E,A,D);
        Triangle tr7 = new Triangle(E,F,C);
        Triangle tr8 = new Triangle(E,B,C);

        /*on ajoute dans le array des triangles*/
        this.listeTriangle.add(tr1);
        this.listeTriangle.add(tr2);
        this.listeTriangle.add(tr3);
        this.listeTriangle.add(tr4);
        this.listeTriangle.add(tr5);
        this.listeTriangle.add(tr6);
        this.listeTriangle.add(tr7);
        this.listeTriangle.add(tr8);

    }




    public ArrayList<Triangle> getTriangleList()
    {
        return listeTriangle;
    }

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
