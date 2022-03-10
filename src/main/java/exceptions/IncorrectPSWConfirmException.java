package exceptions;

/**
 * Exception for different password and password confirmation field error.
 * @author Nko
 *
 */
@SuppressWarnings("serial")
public class IncorrectPSWConfirmException extends Exception{
	/**
	 * Exception constructor without message.
	 */
	public IncorrectPSWConfirmException() {
		super();
	}
	
	/**
	 * Exception constructor with s message.
	 * @param s Message to show user when exception is raised.
	 */
	public IncorrectPSWConfirmException(String s) {
		super(s);
	}
}
