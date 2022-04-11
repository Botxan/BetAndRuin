package exceptions;

/**
 * Exception for when user places an already placed bet.
 * @author Nko
 *
 */
@SuppressWarnings("serial")
public class BetAlreadyExistsException extends Exception{
    /**
     * Exception constructor without message.
     */
    public BetAlreadyExistsException() {
        super();
    }

    /**
     * Exception constructor with s message.
     * @param s Message to show user when exception is raised.
     */
    public BetAlreadyExistsException(String s) {
        super(s);
    }
}
