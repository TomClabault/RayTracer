/*package geometry;*/

import java.util.ArrayList;
import geometry.shapes.Triangle;
import geometry.Point;

public class Shape
{
    protected ArrayList<Triangle> forme;

    public Shape(ArrayList<Triangle> forme)
    {
        this.forme = forme;
    }

    /*addTriangle va ajouter un triangle, faut juste ajouter les points du triangle qu'on veut ajouter*/
    public ArrayList<Triangle> addTriangle(Point a,Point b,Point c)
    {
        Triangle truc = new Triangle(a,b,c);
        this.forme.add(truc);
        return this.forme;
    }

    /*removeTriangle va supprimer un triangle. elle va chercher le triangle avec les points saisis dans le parametre, puis elle le supprime*/
    public ArrayList<Triangle> removeTriangle(Point a, Point b, Point c)
    {
        Triangle truc = new Triangle(a,b,c);
        int toremove = this.forme.indexOf(truc);
        this.forme.remove(toremove);
        return this.forme;
    }

}
