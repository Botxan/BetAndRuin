package exceptions;

/**
 * Exception for when user inserts less fee than required.
 * @author Josefinator
 *
 */
@SuppressWarnings("serial")
public class MinBetException extends Exception{
    /**
     * Exception constructor without message.
     */
    public MinBetException() {
        super();
    }

    /**
     * Exception constructor with s message.
     * @param s Message to show user when exception is raised.
     */
    public MinBetException(String s) {
        super(s);
    }
}


