package exceptions;

/**
 * Will be thrown when the user card number introduced by the user
 * registering is already in use by another user
 */
public class CreditCardAlreadyExists extends Exception {
    private static final long serialVersionUID = 1L;

    public CreditCardAlreadyExists() {
        super();
    }

    /**
     * This exception is triggered if the forecast already exists
     *@param s String of the exception
     */
    public CreditCardAlreadyExists(String s) {
        super(s);
    }
}
