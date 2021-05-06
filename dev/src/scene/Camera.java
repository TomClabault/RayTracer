package scene;

import maths.CTWMatrix;
import maths.MatrixD;
import maths.Point;
import maths.Vector;

/**
 * Permet de representer une camera definie par sa position dans l'espace, ses angles de rotation definissant sa direction
 * de regard ainsi que son champ de vision.<br>
 * La camera est par defaut orientee pour regarder le long de l'axe Z negatif (0, 0, -1). Les rotations autour des axes de la camera
 * peuvent etre deduites de cette direction de regard de depart.
 */
public class Camera 
{
	private Point position;//Vector3D depuis lequel regarde la camera
	
	private double angleHori;/* Angle de rotation de camera sur le plan x, z en degre */
	private double angleVerti;/* Angle de rotation de camera sur le plan x, y en degre */
	
	private static final int X_AXIS = 0;
	private static final int Y_AXIS = 1;
	private static final int Z_AXIS = 2;
	
	private MatrixD CTWMatrix;//Matrice de changement de base entre les coordonnees d'origine du monde [(1, 0, 0), (0, 1, 0), (0, 0, 1)] et les coordoonees de la camera
	
	private double degreeFOV;//Champ de vision de la camera
	
	/**
	 * Cree une camera d'origine le point (0, 0, 0) et de direction (0, 0, -1)
	 */
	public Camera()
	{
		this(new Point(0, 0, 0), 0, 0, 60);
	}
	
	/**
	 * Cree une camera d'origine le point 'position' passe en argument. La direction de la camera suit par defaut l'axe Z dans son sens negatif
	 * 
	 *  @param position Le point d'origine de la camera
	 */
	public Camera(Point position)
	{
		this(position, 0, 0, 60);
	}
	
	/**
	 * Cree une camera a partir de son point d'ancrage et d'un point qu'elle regarde. Ce dernier definira la direction de regard de la camera
	 * 
	 * @param position Point d'ancrage de la camera
	 * @param direction Point que regarde la camera
	 */
	public Camera(Point position, Point direction)
	{
		this(position, getHoriAngleFromDir(position, direction), getVertiAngleFromDir(position, direction), 60);//this.getVeriAngleFromDir(direction));
	}
	
	/**
	 * Cree une camera a partir de son point d'ancrage et d'un point qu'elle regarde. Ce dernier definira la direction de regard de la camera
	 * 
	 * @param position Point d'ancrage de la camera
	 * @param direction Point que regarde la camera
	 * @param FOV Le champ de vision de la camera. Entier entre 1 et 179
	 */
	public Camera(Point position, Point direction, double FOV)
	{
		this(position, getHoriAngleFromDir(position, direction), getVertiAngleFromDir(position, direction), FOV);//this.getVeriAngleFromDir(direction));
	}
	
	/**
	 * Cree une camera avec un point d'ancrage donne et l'angle de rotation horizontal et vertical de la camera
	 * 
	 * @param position 		Le point d'ancrage/d'origine de la camera
	 * @param angleHori 	L'angle de rotation horizontal en degre de la camera
	 * @param angleVerti	L'angle de rotation vertical en degre de la camera. Un angle de plus de 90° ou de moins de -90° sera ramene a 900 ou -90° repsectivement
	 * @param FOV Le champ de vision de la camera. Entier entre 1 et 179
	 */
	public Camera(Point position, double angleHori, double angleVerti, double FOV)
	{
		this.position = position;
		
		this.angleHori = angleHori;
		this.angleVerti = angleVerti;
		this.angleVerti = this.angleVerti > 90 ? 90 : this.angleVerti < -90 ? -90 : this.angleVerti;//On ramene l'angle a 900 / -90° s'il depassait
		this.degreeFOV = FOV;
		
		this.CTWMatrix = new CTWMatrix(this, angleHori, angleVerti);
	}
	
	/**
	 * Ajoute un certain degre de rotation horizontal a la camera
	 * 
	 * @param deltaAngle L'angle de rotation horizontal en degre que l'on veut ajouter
	 */
	public void addAngleHori(double deltaAngle)
	{
		this.angleHori += deltaAngle;
		
		this.CTWMatrix = new CTWMatrix(this, this.angleHori, this.angleVerti);//On a change l'etat de la camera, il faut donc recalculer la matrice de passage qui lui est associee
	}
	
	/**
	 * Ajoute un certain degre de rotation vertical a la camera.
	 * Attention, cette methode ne permet de pas des angles de rotation verticaux de plus de 90° ou de moins de 90°.
	 * Si ajouter 'deltaAngle' a l'angle de rotation vertical actuel de la camera ferait depasser 90° de rotation ou -90°, l'angle est ramene a 90° ou -900 respectivement.
	 * 
	 * @param deltaAngle L'angle de rotation vertical en degre que l'on veut ajouter
	 */
	public void addAngleVerti(double deltaAngle)
	{
		this.angleVerti += deltaAngle;
		if(this.angleVerti > 90)//90° veut dire que la camera regarde directement le ciel, on n'accepte pas plus que cela sinon la camera sera "retournee"
			this.angleVerti = 90;
		else if(this.angleVerti < -90)//Pareil, -90° veut dire qu'on regarde le sol. On se limite a cela
			this.angleVerti = -90;
		
		this.CTWMatrix = new CTWMatrix(this, this.angleHori, this.angleVerti);//On a change l'etat de la camera, il faut donc recalculer la matrice de passage qui lui est associee
	}
	
	/**
	 * Retourne l'angle de rotation horizontal (selon le plan (x, z)) de la camera
	 * 
	 * @return Retourne l'angle de rotation horizontal de la camera en degre
	 */
	public double getAngleHori()
	{
		return this.angleHori;
	}
	
	/**
	 * Retourne l'angle de rotation vertical (selon le plan (x, z)) de la camera
	 * 
	 * @return Retourne l'angle de rotation vertical de la camera en degre
	 */
	public double getAngleVerti()
	{
		return this.angleVerti;
	}
	
	/**
	 * Retourne la matrice de passage des coordonnees d'origine vers les coordonnees de la camera
	 * 
	 * @return Une MatrixD contenant la base de l'espace vectoriel de la camera et la translation des points a effectuer	
	 */
	public MatrixD getCTWMatrix()
	{
		return this.CTWMatrix;
	}
	
	/**
	 * Retourne le vecteur definissant la direction dans laquelle regarde la camera
	 * 
	 * @return Un vecteur de coordonnees (x, y, z) definissant la direction dans laquelle regarde la camera 
	 */
	public Vector getDirection()
	{
		return this.getZAxis();
	}
	
	/**
	 * Retourne le champ de vision (FOV) de la camera. Ce FOV est donne en degre entre 0 et 180
	 * 
	 * @return Un reel pour le champ de vision de la camera en degre entre 0 et 180
	 */
	public double getFOV()
	{
		return this.degreeFOV;
	}
	
	/**
	 * En supposant que la direction de regard par defaut de la camera suit le vecteur (0, 0, -1), calcule l'angle de rotation horizontal necessaire a la camera pour regarder le point 'direction' passe en parametre
	 * 
	 * @param position Position de la camera / point d'ancrage
	 * @param direction Le point que regarde la camera. Utilise pour calculer l'angle de rotation horizontal
	 * 
	 * @return Retourne l'angle de rotation horizontal dont la camera a besoin pour regarder le point passe en argument. L'angle retourne est en degre
	 */
	public static double getHoriAngleFromDir(Point position, Point direction)
	{
		Vector vecDir = new Vector(position, direction);
		Vector vecDirNoY = new Vector(vecDir.getX(), 0, vecDir.getZ());
		if(vecDirNoY.getX() == 0 && vecDirNoY.getY() == 0 && vecDirNoY.getZ() == 0)//Le point qu'on veut regarder est deja aligne avec la direction de regard par defaut de la camera 
			return 0;
		
		Vector vecDirNoYNorm = Vector.normalizeV(vecDirNoY);
		
		double dotProd = Vector.dotProduct(vecDirNoYNorm, new Vector(0, 0, -1));
		double arcos = Math.acos(dotProd);
		double degrees = Math.toDegrees(arcos);
		
		double angle = -Math.signum(vecDirNoYNorm.getX())*degrees;
		return angle;
	}
	
	/**
	 * En supposant que la direction de regard par defaut de la camera suit le vecteur (0, 0, -1), calcule l'angle de rotation vertical necessaire a la camera pour regarder le point 'direction' passe en parametre
	 * 
	 * @param position Position actuelle de la camera
	 * @param direction Le point que regarde la camera. Utilise pour calculer l'angle de rotation vertical
	 * 
	 * @return Retourne l'angle de rotation vertical dont la camera a besoin pour regarder le point passe en argument. L'angle retourne est en degre
	 */
	public static double getVertiAngleFromDir(Point position, Point direction)
	{
		Vector vecDir = new Vector(position, direction);
		Vector vecDirNoX = new Vector(0, vecDir.getY(), vecDir.getZ());
		if(vecDirNoX.getX() == 0 && vecDirNoX.getY() == 0 && vecDirNoX.getZ() == 0)//La direction de regard n'a pas change pas rapport a la direction de regard par defaut de la camera (0, 0, -1)
			return 0;
		Vector vecDirNoXNorm = Vector.normalizeV(vecDirNoX);
		
		double dotProd = Vector.dotProduct(vecDirNoXNorm, new Vector(0, 0, -1));
		double arcos = Math.acos(dotProd);
		double degrees = Math.toDegrees(arcos);
		
		double angle = Math.signum(vecDirNoXNorm.getY())*degrees;
		
		return angle;
	}
	
	/**
	 * Permet d'obtenir la position actuelle de la camera
	 * 
	 * @return Un point de coordonnees (x, y, z) representant les coordoonees actuelle de la camera
	 */
	public Point getPosition()
	{
		return this.position;
	}
	
	/**
	 * Methode permettant de factoriser le code de getXAxis, getYAxis et getZAxis
	 * 
	 * @param axisIndex Entier entre 0 et 2 representant l'axe que l'on souhaite obtenir. 0 pour l'axe x, 1 pour l'axe y et 2 pour l'axe z
	 */
	protected Vector getAxis(int axisIndex)
	{
		return new Vector(this.CTWMatrix.get(axisIndex, 0), this.CTWMatrix.get(axisIndex, 1), this.CTWMatrix.get(axisIndex, 2));
	}
	
	/**
	 * Retourne les coordonnees de l'axe X de la camera par rapport a l'origine du monde i.e. les coordonnees du vecteur (1, 0, 0) dans la base de l'espace vectoriel du monde
	 * 
	 * @return Un vecteur de coordoonees (x, y, z) où x, y et z representent les coordonnees du vecteur de l'axe x de la camera exprmimees dans la base de l'espace vectoriel de la scene i.e. {(1, 0, 0), (0, 1, 0), (0, 0, 1)}
	 */
	public Vector getXAxis()
	{
		return getAxis(X_AXIS);
	}
	
	/**
	 * Retourne les coordonnees de l'axe Y de la camera par rapport a l'origine du monde i.e. les coordonnees du vecteur (0, 1, 0) dans la base de l'espace vectoriel du monde
	 * 
	 * @return Un vecteur de coordoonees (x, y, z) où x, y et z representent les coordonnees du vecteur de l'axe y de la camera exprmimees dans la base de l'espace vectoriel de la scene i.e. {(1, 0, 0), (0, 1, 0), (0, 0, 1)}
	 */
	public Vector getYAxis()
	{
		return getAxis(Y_AXIS);
	}
	
	/**
	 * Retourne les coordonnees de l'axe Z de la camera par rapport a l'origine du monde i.e. les coordonnees du vecteur (0, 0, 1) dans la base de l'espace vectoriel du monde
	 * 
	 * @return Un vecteur de coordoonees (x, y, z) où x, y et z representent les coordonnees du vecteur de l'axe z de la camera exprmimees dans la base de l'espace vectoriel de la scene i.e. {(1, 0, 0), (0, 1, 0), (0, 0, 1)}
	 */
	public Vector getZAxis()
	{
		return getAxis(Z_AXIS);
	}
	
	/**
	 * Redefini l'angle de rotation horizontal de la camera
	 * 
	 * @param angle Nouvel angle de rotation horizontal de la camera en degre
	 */
	public void setAngleHori(double angle)
	{
		this.angleHori = angle;
		
		this.CTWMatrix = new CTWMatrix(this, this.angleHori, this.angleVerti);//On a change l'etat de la camera, il faut donc recalculer la matrice de passage qui lui est associee
	}
	
	/**
	 * Redefini l'angle de rotation vertical de la camera
	 * 
	 * @param angle Nouvel angle de rotation vertical  de la camera en degre
	 */
	public void setAngleVerti(double angle)
	{
		this.angleHori = angle;
		
		this.CTWMatrix = new CTWMatrix(this, this.angleHori, this.angleVerti);//On a change l'etat de la camera, il faut donc recalculer la matrice de passage qui lui est associee
	}
	
	/**
	 * Redefinit le FOV (champ de vision) de la camera.
	 * 
	 * @param newFOV Un reel entre 0 et 180 representant le champ de vision de la camera en degre
	 */
	public void setFOV(double newFOV)
	{
		this.degreeFOV = newFOV;
	}
	
	/**
	 * Permet de redefinir le point que regarde la camera
	 * 
	 * @param lookAtPoint Le nouveau point que regarde la camera 
	 */
	public void setLookAt(Point lookAtPoint)
	{
		this.angleHori = getHoriAngleFromDir(position, lookAtPoint);
		this.angleVerti = getVertiAngleFromDir(position, lookAtPoint);
	}
	
	/**
	 * Definit la nouvelle position de la camera. Cette methode recalcule egalement la matrice de passage CTXMatrix
	 * 
	 * @param newPosition Un point de coordonnees (x, y, z) pour definir les nouvelles coordonnees de la camera
	 */
	public void setPosition(Point newPosition)
	{
		this.position = newPosition;
		
		this.CTWMatrix = new CTWMatrix(this, this.angleHori, this.angleVerti);
	}
	
	@Override
	public String toString()
	{
		return String.format("Position: %s | Angle hori: %.3f | Angle verti: %.3f | FOV: %.3f", position, this.angleHori, this.angleVerti, this.degreeFOV);
	}
}
