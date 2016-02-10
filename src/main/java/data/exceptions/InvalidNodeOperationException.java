/**
 * 
 */
package data.exceptions;

/**
 * @author sdufour
 *
 */
public class InvalidNodeOperationException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8422581665928894048L;

	public InvalidNodeOperationException()
	{
	}

	public InvalidNodeOperationException(String message)
	{
		super(message);
	}

	public InvalidNodeOperationException(Throwable cause)
	{
		super(cause);
	}

	public InvalidNodeOperationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvalidNodeOperationException(String message, Throwable cause, 
                                       boolean enableSuppression, boolean writableStackTrace)
	{
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
