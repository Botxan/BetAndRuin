package exceptions;

/**
 * Exception for Invalid Date creation.
 * @author Nko
 *
 */
public class InvalidDateException extends Exception{
	/**
	 * Exception constructor without message.
	 */
	public InvalidDateException() {
		super();
	}
	
	/**
	 * Exception constructor with s message.
	 * @param s Message to show user when exception is raised.
	 */
	public InvalidDateException(String s) {
		super(s);
	}

}
