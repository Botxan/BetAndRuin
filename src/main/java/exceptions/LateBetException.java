package exceptions;

/**
 * Exception for when user wants to bet on a bet from an hour for the event later.
 * @author Nko
 *
 */
@SuppressWarnings("serial")
public class LateBetException extends Exception{
    /**
     * Exception constructor without message.
     */
    public LateBetException() {
        super();
    }

    /**
     * Exception constructor with s message.
     * @param s Message to show user when exception is raised.
     */
    public LateBetException(String s) {
        super(s);
    }
}
