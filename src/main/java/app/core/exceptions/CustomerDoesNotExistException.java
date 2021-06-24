package app.core.exceptions;

/**
 * exception class in cases a customer doesn't exist
 * 
 */
public class CustomerDoesNotExistException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public CustomerDoesNotExistException() {
		super();
}

	public CustomerDoesNotExistException(String message, Throwable cause) {
		super(message, cause);
}

	public CustomerDoesNotExistException(String message) {
		super(message);
}
}
