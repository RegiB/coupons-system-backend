package app.core.exceptions;

/**
 * exception class in cases a coupon doesn't exist
 * 
 */
public class CouponDoesNotExistException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public CouponDoesNotExistException() {
		super();
}

	public CouponDoesNotExistException(String message, Throwable cause) {
		super(message, cause);
}

	public CouponDoesNotExistException(String message) {
		super(message);
}
}
