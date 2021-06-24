package app.core.exceptions;

/**
 * exception class in cases a company doesn't exist
 * 
 */
public class CompanyDoesNotExistException extends CouponSystemException {

	private static final long serialVersionUID = 1L;

	public CompanyDoesNotExistException() {
		super();
}

	public CompanyDoesNotExistException(String message, Throwable cause) {
		super(message, cause);
}

	public CompanyDoesNotExistException(String message) {
		super(message);
}

}
