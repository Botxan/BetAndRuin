package exceptions;

/**
 * Exception thrown when a user with the chosen username already is persisted in the database.
 * @author Nko
 *
 */
@SuppressWarnings("serial")
public class UsernameAlreadyInDBException extends Exception{
	/**
	 * Exception constructor without message.
	 */
	public UsernameAlreadyInDBException() {
		super();
	}
	
	/**
	 * Exception constructor with s message.
	 * @param s Message to show user when exception is raised.
	 */
	public UsernameAlreadyInDBException(String s) {
		super(s);
	}
}
