package exceptions;

/**
 * 
 * Exception jetée lors de la création d'une sphère incorrecte (rayon négatif par exemple)
 *
 */
public class InvalidSphereException extends IllegalArgumentException
{
	public InvalidSphereException(String message)
	{
		super(message);
	}
}
