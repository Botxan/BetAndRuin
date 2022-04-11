package exceptions;

/**
 * Thrown if the gambler tries to place a bet an hour before on the event associated with the forecast.
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
