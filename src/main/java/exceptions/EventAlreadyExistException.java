package exceptions;

public class EventAlreadyExistException extends Exception {
	private static final long serialVersionUID = 1L;

	public EventAlreadyExistException() {
		super();
	}

	/**
	 * This exception is triggered if the forecast already exists 
	 *@param s String of the exception
	 */
	public EventAlreadyExistException(String s) {
		super(s);
	}
}
