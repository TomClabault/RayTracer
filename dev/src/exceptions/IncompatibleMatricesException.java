package exceptions;

/**
 * Exception jetee lorsque l'on tente de multiplier deux matrices dont les tailles ne permettent pas la multiplication.
 */
public class IncompatibleMatricesException extends RuntimeException 
{
	public IncompatibleMatricesException()
	{
		super("La multiplication des matrices demandee n'est pas faisable");
	}
	
	public IncompatibleMatricesException(String message)
	{
		super(message);
	}
}
