package exceptions;

/**
 * 
 * Exception jetee lors de la creation d'une sphere incorrecte (rayon negatif par exemple)
 *
 */
public class InvalidSphereException extends IllegalArgumentException
{
	public InvalidSphereException(String message)
	{
		super(message);
	}
}
