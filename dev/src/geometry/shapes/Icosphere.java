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




public class Icosphere extends ShapeTriangle
{
    /*
    !!!!!!!! Merci de lire cette notice avant de lire les codes !!!!!!!!!!!!!!
    !!! DISCLAIMER !!!
    Les codes ci-dessous ne sont pas totalement le fruit de mon travail, une petite partie est issue de ces sites:
    La liste des points vient de: http://blog.coredumping.com/subdivision-of-icosahedrons/
    La construction initiale d'icoshpere vient de: http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html








     */


    //protected Point BottomPoint,Centre,TopPoint,Atop,Btop,Ctop,Dtop,Etop,Arot,Brot,Crot,Drot,Erot;

    //Disclaimer: Les codes ci-dessous ne sont pas totalement le fruit de mon travail, une petite partie est issue de ces sites:
    //La liste des points vient de: http://blog.coredumping.com/subdivision-of-icosahedrons/
    //La construction initiale d'icoshpere vient de: http://blog.andreaskahler.com/2009/06/creating-icosphere-mesh-in-code.html

    protected Point depart;
    protected double t = ((1 + Math.sqrt(5))/2);
    protected double size;
    protected Material material;

    public Icosphere(Point depart, double size, Material material)
    {
        this.depart = depart;
        this.size = size;

        this.material = material;

        buildIcosphere();

    }

    protected void buildIcosphere()
    {
        //On va construire des Points
        //ArrayList<Point> listePoints = new ArrayList<Point>();
        Point A = new Point(-size, t*size,0);
        Point B = new Point(size,t*size,0);
        Point C = new Point(-size,-t*size,0);
        Point D = new Point(size,-t*size,0);

        Point E = new Point(0,-size,t*size);
        Point F = new Point(0,size,t*size);
        Point G = new Point(0,-size,-t*size);
        Point H = new Point(0,size,-t*size);

        Point I = new Point(t*size,0,-size);
        Point J = new Point(t*size,0,size);
        Point K = new Point(-t*size,0,-size);
        Point L = new Point(-t*size,0,size);

        //On va construire des triangles avec les points
        Triangle tr1 = new Triangle(J,F,E, material);
        Triangle tr2 = new Triangle(F,L,E, material);
        Triangle tr3 = new Triangle(J,E,D, material);
        Triangle tr4 = new Triangle(F,J,B, material);
        Triangle tr5 = new Triangle(B,J,I, material);
        Triangle tr6 = new Triangle(J,D,I, material);
        Triangle tr7 = new Triangle(B,I,H, material);
        Triangle tr8 = new Triangle(B,H,A, material);
        Triangle tr9 = new Triangle(A,F,B, material);
        Triangle tr10 = new Triangle(A,L,F, material);
        Triangle tr11 = new Triangle(L,C,E, material);
        Triangle tr12 = new Triangle(K,L,C, material);
        Triangle tr13 = new Triangle(A,K,L, material);
        Triangle tr14 = new Triangle(A,H,K, material);
        Triangle tr15 = new Triangle(H,G,K, material);
        Triangle tr16 = new Triangle(K,G,C, material);
        Triangle tr17 = new Triangle(C,D,E, material);
        Triangle tr18 = new Triangle(H,I,G, material);
        Triangle tr19 = new Triangle(I,D,G, material);
        Triangle tr20 = new Triangle(G,D,C, material);




    }









}
