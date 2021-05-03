package geometry;

/**
 * Permet de stocker l'objet intersecté par les rayons. Sans cette classe, nous ne pourrions pas ET retourner le coefficient
 * t du rayon ET l'objet intersecté. Cette classe est donc utilisée pour contenir l'objet intersecté et agir comme un
 * pointeur sur l'objet intersecté. On peut donc passer cette classe en
 * paramètre de {@link accelerationStructures.OctreeNode#intersect(Ray, Point, Vector, ObjectContainer)} et ainsi obtenir
 * l'objet intersecté
 */
public class ObjectContainer
{
	private Shape containedShape;
	
	public ObjectContainer() 
	{
		this.containedShape = null;
	}
	
	public Shape getContainedShape() 
	{
		return containedShape;
	}
	
	public void setContainedShape(Shape containedShape) 
	{
		this.containedShape = containedShape;
	}
}
