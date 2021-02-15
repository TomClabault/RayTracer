package geometry;

import java.util.ArrayList;
import geometry.shapes.Triangle;

public class Shape
{
    protected ArrayList<Triangle> forme;

    public Shape(ArrayList<Triangle> forme)
    {
        this.forme = forme;
    }

    /*addTriangle va ajouter un triangle, faut juste ajouter les points du triangle qu'on veut ajouter*/
    public void addTriangle(Point a,Point b,Point c)
    {
        Triangle truc = new Triangle(a,b,c);
        this.forme.add(truc);
    }

    /*removeTriangle va supprimer un triangle. elle va chercher le triangle avec les points saisis dans le parametre, puis elle le supprime*/
    public void removeTriangle(Point a, Point b, Point c)
    {
        Triangle truc = new Triangle(a,b,c);
        if (this.forme.indexOf(truc) >= 0)
        {
            int toremove = this.forme.indexOf(truc);
            this.forme.remove(toremove);
        }
        else
        {
            System.out.println("le triangle que vous souhaitez supprimer n'existe pas");
        }
    }

}
