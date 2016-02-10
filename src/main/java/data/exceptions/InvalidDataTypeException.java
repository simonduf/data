/**
 * 
 */
package data.exceptions;

/**
 * @author sdufour
 *
 */
public class InvalidDataTypeException extends RuntimeException {

	private static final long serialVersionUID = 967333876617781306L;
	public InvalidDataTypeException()
	{
	}

	public InvalidDataTypeException(String message)
	{
		super(message);
	}

	public InvalidDataTypeException(Throwable cause)
	{
		super(cause);
	}

	public InvalidDataTypeException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidDataTypeException(String message, Throwable cause, 
                                       boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
