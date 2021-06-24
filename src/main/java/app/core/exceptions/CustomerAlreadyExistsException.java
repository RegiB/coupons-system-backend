package app.core.exceptions;

/**
 * Customer Already Exists Exception class
 */
public class CustomerAlreadyExistsException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public CustomerAlreadyExistsException() {
		super();
}

	public CustomerAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
}

	public CustomerAlreadyExistsException(String message) {
		super(message);
}
}
