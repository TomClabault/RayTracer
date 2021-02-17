package geometry;

public interface Shape
{
	/*
	 * Permet d'obtenir la normale à un point donné de la forme
	 * 
	 * @param point Le point par rapport auquel on souhaite la normale
	 */
	public abstract Vector getNormal(Point point);
	
	/*
	 * Calcule le point d'intersection avec un rayon et le renvoie si existant. Le point d'intersection n'est cherché que "en face" du rayon.
	 * 
	 * @param ray Rayon avec lequel chercher une intersection
	 * 
	 * @return Renvoie le point d'intersection du rayon et de l'objet. Null s'il n'y a pas de point d'intersection
	 */
	public Point intersect(Ray ray);
	
	
	
	
	
//    protected ArrayList<Triangle> forme;
//
//    public Shape(ArrayList<Triangle> forme)
//    {
//        this.forme = forme;
//    }
//
//    /*addTriangle va ajouter un triangle, faut juste ajouter les points du triangle qu'on veut ajouter*/
//    public void addTriangle(Point a,Point b,Point c)
//    {
//        Triangle truc = new Triangle(a,b,c);
//        this.forme.add(truc);
//    }
//
//    /*removeTriangle va supprimer un triangle. elle va chercher le triangle avec les points saisis dans le parametre, puis elle le supprime*/
//    public void removeTriangle(Point a, Point b, Point c)
//    {
//        Triangle truc = new Triangle(a,b,c);
//        if (this.forme.indexOf(truc) >= 0)
//        {
//            int toremove = this.forme.indexOf(truc);
//            this.forme.remove(toremove);
//        }
//        else
//        {
//            System.out.println("le triangle que vous souhaitez supprimer n'existe pas");
//        }
//    }
}
