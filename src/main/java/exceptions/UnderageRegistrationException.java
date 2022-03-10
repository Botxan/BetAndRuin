package exceptions;

/**
 * Exception for underage registration banning.
 * @author Nko
 *
 */
@SuppressWarnings("serial")
public class UnderageRegistrationException extends Exception{
	/**
	 * Exception constructor without message.
	 */
	public UnderageRegistrationException() {
		super();
	}
	
	/**
	 * Exception constructor with s message.
	 * @param s Message to show user when exception is raised.
	 */
	public UnderageRegistrationException(String s) {
		super(s);
	}
}
