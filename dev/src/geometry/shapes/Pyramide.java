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


public class Pyramide implements ShapeTriangle {
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

    protected Point A, B, C, D, E;
    protected double height, width;
    protected ArrayList<Triangle> listeTriangle;
    private Material material;

    public Pyramide(Point A, Point B, Point C, Point D, Point E, Material material) {
        this.A = A;
        this.B = B;
        this.C = C;
        this.D = D;
        this.E = E;

        this.material = material;

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

        this.material = material;

        buildPyramide();


    }


    protected void buildPyramide() {
        /*on va construire les 6 triangles*/
        Triangle tr1 = new Triangle(E, D, A, material);
        Triangle tr2 = new Triangle(E, A, B, material);
        Triangle tr3 = new Triangle(E, B, C, material);
        Triangle tr4 = new Triangle(E, C, D, material);
        Triangle tr5 = new Triangle(A, D, C, material);
        Triangle tr6 = new Triangle(A, B, C, material);

        /*on va ajouter les triangles dans la listeTriangle*/
        listeTriangle = new ArrayList<Triangle>();
        this.listeTriangle.add(tr1);
        this.listeTriangle.add(tr2);
        this.listeTriangle.add(tr3);
        this.listeTriangle.add(tr4);
        this.listeTriangle.add(tr5);
        this.listeTriangle.add(tr6);

    }


    @Override
    public ArrayList<Triangle> getTriangleList() {
        return listeTriangle;
    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public Vector getNormal(Point point) {
        for (int i = 0; i < listeTriangle.size(); i++) {
            if (listeTriangle.get(i).insideOutsideTest(point) == true) {
                return listeTriangle.get(i).getNormal(point);
            }
        }
        return null;
    }

    @Override
    public Point intersect(Ray ray, Vector outNormalAtInter) {
        Double distancemin = null;
        Point intersection = null;
        Triangle intersectedTriangle = null;
        for (int i = 0; i < listeTriangle.size(); i++) {
        	Triangle triangle = listeTriangle.get(i);
            intersection = triangle.intersect(ray, null);
            if (intersection != null) {
                double distance = Point.distance(intersection, ray.getOrigin());
                if (distancemin == null || distance < distancemin) {
                    distancemin = distance;
                    intersectedTriangle = listeTriangle.get(i);
                }
            }


        }
        if (outNormalAtInter != null) {
            if (intersectedTriangle != null) {
                outNormalAtInter.copyIn(intersectedTriangle.getNormal(null));
            }
        }
        return intersection;

    }
}