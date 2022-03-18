package exceptions;

public class ForecastAlreadyExistException extends Exception {
	private static final long serialVersionUID = 1L;

	public ForecastAlreadyExistException() {
		super();
	}

	/**This exception is triggered if the event already exists 
	 *@param s String of the exception
	 */
	public ForecastAlreadyExistException(String s) {
		super(s);
	}
}
