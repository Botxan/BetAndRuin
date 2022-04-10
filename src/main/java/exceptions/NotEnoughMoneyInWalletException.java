package exceptions;

/**
 * Exception that occurs when there is not enough money in the wallet.
 * @author Team Josefinators
 *
 */
public class NotEnoughMoneyInWalletException extends Exception {
    /**
     * Exception constructor without message.
     */
    public NotEnoughMoneyInWalletException() {
        super();
    }

    /**
     * Exception constructor with s message.
     * @param s Message to show to the user when the exception is raised
     */
    public NotEnoughMoneyInWalletException(String s) {
        super(s);
    }
}
