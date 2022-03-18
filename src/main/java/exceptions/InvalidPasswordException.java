package exceptions;

@SuppressWarnings("serial")
public class InvalidPasswordException extends Exception{
	/**
	 * Exception constructor without message.
	 */
	public InvalidPasswordException() {
		super();
	}
	
	/**
	 * Exception constructor with s message.
	 * @param s Message to show user when exception is raised.
	 */
	public InvalidPasswordException(String s) {
		super(s);
	}
}
