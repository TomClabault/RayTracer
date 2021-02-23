package scene;

import maths.Point;
import maths.Vector;

/*
 * Class permettant de représenter une caméra définie par sa position dans l'espace, la direction dans laquelle elle regarde ainsi que son champ de vision
 */
public class Camera 
{
	Point position;
	
	Vector direction;//Direction dans laquelle regarde la caméra
	
	double degreeFOV;//Champ de vision de la caméra
	
	/*
	 * Crée une caméra d'origine le point (0, 0, 0) et de direction (0, 0, -1)
	 */
	public Camera()
	{
		this(new Point(0, 0, 0), new Vector(0, 0, -1), 90);
	}
	
	/*
	 * Crée une caméra d'origine le point 'position' passé en argument. La direction de la caméra suit par défaut l'axe Z dans son sens négatif
	 * 
	 *  @param position Le point d'origine de la caméra
	 */
	public Camera(Point position)
	{
		this(position, new Vector(0, 0, -1), 90);
	}
	
	/*
	 * Crée une caméra à partir d'un point d'origine ainsi que d'une direction
	 * 
	 *  @param position Le point d'origine de la caméra
	 *  @param direction La direction dans laquelle regarde la caméra
	 */
	public Camera(Point position, Vector direction)
	{
		this(position, direction, 90);
	}
	
	/*
	 * Crée une caméra à partir de sa position, de sa direction et de son champ de vision
	 * 
	 * @param position Point de coordonnées (x, y, z) pour définir la position de la caméra
	 * @param direction Vector de coordoonnées (x, y, z) pour définir la direction de la caméra
	 * @param degreeFOV Réel 
	 */
	public Camera(Point position, Vector direction, float degreeFOV)
	{
		this.position = position;
		this.direction = direction;
		this.degreeFOV = degreeFOV;
	}
	
	/*
	 * Retourne le vecteur définissant la direction dans laquelle regarde la caméra
	 * 
	 * @return Un vecteur de coordonnées (x, y, z) définissant la direction dans laquelle regarde la caméra 
	 */
	public Vector getDirection()
	{
		return this.direction;
	}
	
	/*
	 * Permet d'obtenir la position actuelle de la caméra
	 * 
	 * @return Un point de coordonnées (x, y, z) représentant les coordoonées actuelle de la caméra
	 */
	public Point getPosition()
	{
		return this.position;
	}
	
	/*
	 * Retourne le champ de vision (FOV) de la caméra. Ce FOV est donné en degré entre 0 et 180
	 * 
	 * @return Un réel pour le champ de vision de la caméra en degré entre 0 et 180
	 */
	public double getFOV()
	{
		return this.degreeFOV;
	}
	
	/*
	 * Redéfinit la direction de la caméra
	 * 
	 * @param newDirection Un vecteur pour redéfinir la direction de la caméra
	 */
	public void setDirection(Vector newDirection)
	{
		this.direction = newDirection;
	}
	
	/*
	 * Redéfinit le FOV (champ de vision) de la caméra.
	 * 
	 * @param angle Un réel entre 0 et 180 représentant le champ de vision de la caméra en degré
	 */
	public void setFOV(double newFOV)
	{
		this.degreeFOV = newFOV;
	}
	
	/*
	 * Définit la nouvelle position de la caméra
	 * 
	 * @param newPosition Un point de coordonnées (x, y, z) pour définir les nouvelles coordonnées de la caméra
	 */
	public void setPosition(Point newPosition)
	{
		this.position = newPosition;
	}
}
