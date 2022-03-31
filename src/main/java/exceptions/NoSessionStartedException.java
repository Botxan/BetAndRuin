package exceptions;

/**
 * Exception for throwing when a session has not been started.
 * @author Team Josefinators
 *
 */
@SuppressWarnings("serial")
public class NoSessionStartedException extends Exception{
    /**
     * Exception constructor without message.
     */
    public NoSessionStartedException() {
        super();
    }

    /**
     * Exception constructor with s message.
     * @param s Message to show to the user when the exception is raised
     */
    public NoSessionStartedException(String s) {
        super(s);
    }
}