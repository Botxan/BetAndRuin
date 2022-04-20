package exceptions;

/**
 * Exception that occurs when there is not enough money in the wallet.
 * @author Team Josefinators
 *
 */
public class NotEnoughMoneyException extends Exception {
    /**
     * Exception constructor without message.
     */
    public NotEnoughMoneyException() {
        super();
    }

    /**
     * Exception constructor with s message.
     * @param s Message to show to the user when the exception is raised
     */
    public NotEnoughMoneyException(String s) {
        super(s);
    }
}
