package exceptions;

@SuppressWarnings("serial")
public class UserNotFoundException extends Exception{
	/**
	 * Exception constructor without message.
	 */
	public UserNotFoundException() {
		super();
	}
	
	/**
	 * Exception constructor with s message.
	 * @param s Message to show user when exception is raised.
	 */
	public UserNotFoundException(String s) {
		super(s);
	}
}
