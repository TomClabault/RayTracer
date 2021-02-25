package geometry;

import javafx.scene.paint.Color;
import maths.Point;
import maths.Ray;
import maths.Vector;

public interface Shape
{
	/*
	 * Permet d'obtenir la quantité de lumière ambiante que l'objet est capable de renvoyer.
	 * 0 signifie que l'objet ne "reçevra" aucune luminosité ambiante, 1 qu'il "reçevra" 100% de la luminosité ambiante de la scène
	 * 
	 *  @return Retourne un réel entre 0 et 1 représentant le pourcentage de luminosité ambiante qui réfléchira l'objet 
	 */
	public abstract double getAmbientCoeff();
	
	/*
	 * Retourne le couleur de l'objet sous la forme d'un objet Color.RGB(r, g, b)
	 * 
	 * @return Color.rgb(r, g, b), la couleur de l'objet au format RGB
	 */
	public abstract Color getColor();
	
	/*
	 * Permet d'obtenir la composante diffuse d'un objet
	 * 
	 * @return Retourne un réel entre 0 et 1 représentant le pourcentage de diffusion de la lumière par l'objet
	 */
	public abstract double getDiffuseCoeff();
	
	/*
	 * Permet d'obtenir la normale à un point donné de la forme
	 * 
	 * @param point Le point par rapport auquel on souhaite la normale
	 */
	public abstract Vector getNormal(Point point);
	
	/*
	 * Permet d'obtenir la proportion de lumière que réfléchit l'objet
	 * 
	 * @return Retourne la proportion de lumière que réfélchit l'objet. Réel entre 0 et 1
	 */
	public abstract double getReflectiveCoeff();
	
	/*
	 * Permet d'obtenir la brillance de l'objet
	 * 
	 * @return Retourne un entier positif représentant la brillance de l'objet. Plus l'objet est brillant plut la valeur de shininess est petite 
	 */
	public abstract int getShininess(); 
	
	/*
	 * Permet d'obtenir le coefficient de spécularité de l'objet
	 * 
	 * @return Un réel entre 0 et 1 représentant le coefficient de spécularité de l'objet
	 */
	public abstract double getSpecularCoeff();
	
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
