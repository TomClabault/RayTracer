package exceptions;

public class InvalidRectangleException extends IllegalArgumentException 
{
	public InvalidRectangleException(String message) 
	{
		super(message);
	}
}
