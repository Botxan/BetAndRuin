package exceptions;

/**
 * Exception for throwing when the password is too short.
 * @author Nko
 *
 */
public class PswTooShortException extends Exception{
	/**
	 * Exception constructor without message.
	 */
	public PswTooShortException() {
		super();
	}
	
	/**
	 * Exception constructor with s message.
	 * @param s Message to show user when exception is raised.
	 */
	public PswTooShortException(String s) {
		super(s);
	}
}
