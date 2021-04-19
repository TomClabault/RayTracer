package exceptions;

/**
 * 
 * Exception jetée lors de la création d'un parallélépipède incorrect, notamment plat.
 *
 */
public class InvalidParallelepipedException extends IllegalArgumentException 
{
	public InvalidParallelepipedException(String message) 
	{
		super(message);
	}
}
