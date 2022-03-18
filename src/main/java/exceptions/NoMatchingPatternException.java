package exceptions;

/**
 * Exception for throwing when the predetermined pattern does not match the given one.
 * @author Nko
 *
 */
@SuppressWarnings("serial")
public class NoMatchingPatternException extends Exception{
	/**
	 * Exception constructor without message.
	 */
	public NoMatchingPatternException() {
		super();
	}
	
	/**
	 * Exception constructor with s message.
	 * @param s Message to show user when exception is raised.
	 */
	public NoMatchingPatternException(String s) {
		super("The " + s + " does not match the pattern.");
	}
}
