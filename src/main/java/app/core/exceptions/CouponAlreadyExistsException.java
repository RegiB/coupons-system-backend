package app.core.exceptions;

/**
 * Coupon Already Exists Exception class
 */
public class CouponAlreadyExistsException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public CouponAlreadyExistsException() {
		super();
}

	public CouponAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
}

	public CouponAlreadyExistsException(String message) {
		super(message);
}
	
	

}
