package scene;

import maths.CTWMatrix;
import maths.MatrixD;
import maths.Point;
import maths.Vector;

/*
 * Class permettant de représenter une caméra définie par sa position dans l'espace, la direction dans laquelle elle regarde ainsi que son champ de vision
 */
public class Camera 
{
	Point position;//Point depuis lequel regarde la caméra
	Point pointDirection;//Point que regarde la caméra
	
	MatrixD CTWMatrix;//Matrice de changement de base entre les coordonnées d'origine du monde [(1, 0, 0), (0, 1, 0), (0, 0, 1)] et les coordoonées de la caméra
	
	double degreeFOV;//Champ de vision de la caméra
	
	/*
	 * Crée une caméra d'origine le point (0, 0, 0) et de direction (0, 0, -1)
	 */
	public Camera()
	{
		this(new Point(0, 0, 0), new Point(0, 0, -1), 90);
	}
	
	/*
	 * Crée une caméra d'origine le point 'position' passé en argument. La direction de la caméra suit par défaut l'axe Z dans son sens négatif
	 * 
	 *  @param position Le point d'origine de la caméra
	 */
	public Camera(Point position)
	{
		this(position, new Point(0, 0, -1), 90);
	}
	
	/*
	 * Crée une caméra à partir d'un point d'origine ainsi que d'un point que le caméra regarde
	 * 
	 *  @param position Le point d'origine de la caméra
	 *  @param pointDirection Le point que regarde la caméra. Utilisé pour calculer la direction de la caméra
	 */
	public Camera(Point position, Point pointDirection)
	{
		this(position, pointDirection, 90);
	}
	
	/*
	 * Crée une caméra à partir de sa position, de sa direction et de son champ de vision
	 * 
	 * @param position Point de coordonnées (x, y, z) pour définir la position de la caméra
	 * @param direction Vector de coordoonnées (x, y, z) pour définir la direction de la caméra
	 * @param degreeFOV Réel 
	 */
	public Camera(Point position, Point pointDirection, float degreeFOV)
	{
		this.position = position;
		this.pointDirection = pointDirection;
		this.degreeFOV = degreeFOV;
		
		this.CTWMatrix = new CTWMatrix(position, pointDirection);
	}
	
	/*
	 * Retourne la matrice de passage des coordonnées d'origine vers les coordonnées de la caméra
	 * 
	 * @return Une MatrixD contenant la base de l'espace vectoriel de la caméra et la translation des points à effectuer	
	 */
	public MatrixD getCTWMatrix()
	{
		return this.CTWMatrix;
	}
	
	/*
	 * Retourne le vecteur définissant la direction dans laquelle regarde la caméra
	 * 
	 * @return Un vecteur de coordonnées (x, y, z) définissant la direction dans laquelle regarde la caméra 
	 */
	public Point getDirection()
	{
		return this.pointDirection;
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
	 * Permet d'obtenir la position actuelle de la caméra
	 * 
	 * @return Un point de coordonnées (x, y, z) représentant les coordoonées actuelle de la caméra
	 */
	public Point getPosition()
	{
		return this.position;
	}
	
	/*
	 * Méthode permettant de factoriser le code de getXAxis, getYAxis et getZAxis
	 * 
	 * @param axisIndex Entier entre 0 et 2 représentant l'axe que l'on souhaite obtenir. 0 pour l'axe x, 1 pour l'axe y et 2 pour l'axe z
	 */
	protected Vector getWAxis(int axisIndex)
	{
		return new Vector(this.CTWMatrix.get(axisIndex, 0), this.CTWMatrix.get(axisIndex, 1), this.CTWMatrix.get(axisIndex, 2));
	}
	
	/*
	 * Retourne les coordonnées de l'axe X de la caméra par rapport à l'origine du monde i.e. les coordonnées du vecteur (1, 0, 0) dans la base de l'espace vectoriel du monde
	 * 
	 * @return Un vecteur de coordoonées (x, y, z) où x, y et z représentent les coordonnées du vecteur de l'axe x de la caméra exprmimées dans la base de l'espace vectoriel de la scène i.e. {(1, 0, 0), (0, 1, 0), (0, 0, 1)}
	 */
	public Vector getXAxis()
	{
		return getWAxis(0);
	}
	
	/*
	 * Retourne les coordonnées de l'axe Y de la caméra par rapport à l'origine du monde i.e. les coordonnées du vecteur (0, 1, 0) dans la base de l'espace vectoriel du monde
	 * 
	 * @return Un vecteur de coordoonées (x, y, z) où x, y et z représentent les coordonnées du vecteur de l'axe y de la caméra exprmimées dans la base de l'espace vectoriel de la scène i.e. {(1, 0, 0), (0, 1, 0), (0, 0, 1)}
	 */
	public Vector getYAxis()
	{
		return getWAxis(1);
	}
	
	/*
	 * Retourne les coordonnées de l'axe Z de la caméra par rapport à l'origine du monde i.e. les coordonnées du vecteur (0, 0, 1) dans la base de l'espace vectoriel du monde
	 * 
	 * @return Un vecteur de coordoonées (x, y, z) où x, y et z représentent les coordonnées du vecteur de l'axe z de la caméra exprmimées dans la base de l'espace vectoriel de la scène i.e. {(1, 0, 0), (0, 1, 0), (0, 0, 1)}
	 */
	public Vector getZAxis()
	{
		return getWAxis(2);
	}
	
	/*
	 * Redéfinit la direction de la caméra. Cette méthode recalcule également la matrice de passage CTXMatrix
	 * 
	 * @param newDirection Un vecteur pour redéfinir la direction de la caméra
	 */
	public void setDirection(Point newPointDirection)
	{
		this.pointDirection = newPointDirection;
		
		this.CTWMatrix = new CTWMatrix(this.position, this.pointDirection);
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
	 * Définit la nouvelle position de la caméra. Cette méthode recalcule également la matrice de passage CTXMatrix
	 * 
	 * @param newPosition Un point de coordonnées (x, y, z) pour définir les nouvelles coordonnées de la caméra
	 */
	public void setPosition(Point newPosition)
	{
		this.position = newPosition;
		
		this.CTWMatrix = new CTWMatrix(this.position, this.pointDirection);
	}
}
