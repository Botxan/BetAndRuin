package exceptions;

/**
 * Exception for when user wants to place a bet with not enough money available.
 * @author Nko
 *
 */
@SuppressWarnings("serial")
public class LiquidityLackException extends Exception{
    /**
     * Exception constructor without message.
     */
    public LiquidityLackException() {
        super();
    }

    /**
     * Exception constructor with s message.
     * @param s Message to show user when exception is raised.
     */
    public LiquidityLackException(String s) {
        super(s);
    }
}

