package exceptions;

/**
 * Exception jetée lorsque l'on tente de multiplier deux matrices dont les tailles ne permettent pas la multiplication.
 */
public class IncompatibleMatricesException extends RuntimeException 
{
	public IncompatibleMatricesException()
	{
		super("La multiplication des matrices demandée n'est pas faisable");
	}
	
	public IncompatibleMatricesException(String message)
	{
		super(message);
	}
}
