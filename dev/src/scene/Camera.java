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
	
	double angleHori;/* Angle de rotation de caméra sur le plan x, z en degré */
	double angleVerti;/* Angle de rotation de caméra sur le plan x, y en degré */
	/*
	 * Deux angles horizontal et vertical
	 * 
	 * Faire deux constructeurs avec le point de direction et un autre avec les deux angles
	 * Uné méthode pour faire regarder la caméra sur un certain point et calculer les deux angles à partir de ce point. En deux temps, horizontal et vertical après
	 */
	
	MatrixD CTWMatrix;//Matrice de changement de base entre les coordonnées d'origine du monde [(1, 0, 0), (0, 1, 0), (0, 0, 1)] et les coordoonées de la caméra
	
	double degreeFOV;//Champ de vision de la caméra
	
	/*
	 * Crée une caméra d'origine le point (0, 0, 0) et de direction (0, 0, -1)
	 */
	public Camera()
	{
		this(new Point(0, 0, 0), 0, 0);
	}
	
	/*
	 * Crée une caméra d'origine le point 'position' passé en argument. La direction de la caméra suit par défaut l'axe Z dans son sens négatif
	 * 
	 *  @param position Le point d'origine de la caméra
	 */
	public Camera(Point position)
	{
		this(position, 0, 0);
	}
	
	public Camera(Point position, double angleHori, double angleVerti)
	{
		this.position = position;
		
		this.angleHori = angleHori;
		this.angleVerti = angleVerti;
		
		this.CTWMatrix = new CTWMatrix(this, angleHori, angleVerti);
	}
	
	/*
	 * Ajoute un certain degré de rotation horizontal à la caméra
	 * 
	 * @param deltaAngle L'angle de rotation horizontal en degré que l'on veut ajouter
	 */
	public void addAngleHori(double deltaAngle)
	{
		this.angleHori += deltaAngle;
		
		this.CTWMatrix = new CTWMatrix(this, this.angleHori, this.angleVerti);//On a changé l'état de la caméra, il faut donc recalculer la matrice de passage qui lui est associée
	}
	
	/*
	 * Ajoute un certain degré de rotation vertical à la caméra.
	 * Attention, cette méthode ne permet de pas des angles de rotation verticaux de plus de 90° ou de moins de 90°.
	 * Si ajouter 'deltaAngle' à l'angle de rotation vertical actuel de la caméra ferait dépasser 90° de rotation ou -90°, l'angle est ramené à 90° ou -900 respectivement.
	 * 
	 * @param deltaAngle L'angle de rotation vertical en degré que l'on veut ajouter
	 */
	public void addAngleVerti(double deltaAngle)
	{
		this.angleVerti += deltaAngle;
		if(this.angleVerti > 90)//90° veut dire que la caméra regarde directement le ciel, on n'accepte pas plus que cela sinon la caméra sera "retournée"
			this.angleVerti = 90;
		else if(this.angleVerti < -90)//Pareil, -90° veut dire qu'on regarde le sol. On se limite à cela
			this.angleVerti = -90;
		
		this.CTWMatrix = new CTWMatrix(this, this.angleHori, this.angleVerti);//On a changé l'état de la caméra, il faut donc recalculer la matrice de passage qui lui est associée
	}
	
	/*
	 * Retourne l'angle de rotation horizontal (selon le plan (x, z)) de la caméra
	 * 
	 * @return Retourne l'angle de rotation horizontal de la caméra en degré
	 */
	public double getAngleHori()
	{
		return this.angleHori;
	}
	
	/*
	 * Retourne l'angle de rotation vertical (selon le plan (x, z)) de la caméra
	 * 
	 * @return Retourne l'angle de rotation vertical de la caméra en degré
	 */
	public double getAngleVerti()
	{
		return this.angleVerti;
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
	public Vector getDirection()
	{
		return this.getZAxis();
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
	 * Redéfini l'angle de rotation horizontal de la caméra
	 * 
	 * @param angle Nouvel angle de rotation horizontal de la caméra en degré
	 */
	public void setAngleHori(double angle)
	{
		this.angleHori = angle;
		
		this.CTWMatrix = new CTWMatrix(this, this.angleHori, this.angleVerti);//On a changé l'état de la caméra, il faut donc recalculer la matrice de passage qui lui est associée
	}
	
	/*
	 * Redéfini l'angle de rotation vertical de la caméra
	 * 
	 * @param angle Nouvel angle de rotation vertical  de la caméra en degré
	 */
	public void setAngleVerti(double angle)
	{
		this.angleHori = angle;
		
		this.CTWMatrix = new CTWMatrix(this, this.angleHori, this.angleVerti);//On a changé l'état de la caméra, il faut donc recalculer la matrice de passage qui lui est associée
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
		
		this.CTWMatrix = new CTWMatrix(this, this.angleHori, this.angleVerti);
	}
}
