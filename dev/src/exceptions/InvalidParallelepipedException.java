package exceptions;

/**
 * 
 * Exception jetee lors de la creation d'un parallelepipede incorrect, notamment plat.
 *
 */
public class InvalidParallelepipedException extends IllegalArgumentException 
{
	public InvalidParallelepipedException(String message) 
	{
		super(message);
	}
}
