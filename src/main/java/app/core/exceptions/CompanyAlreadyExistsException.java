package app.core.exceptions;

/**
 * Company Already Exists Exception class
 */
public class CompanyAlreadyExistsException extends CouponSystemException {

	private static final long serialVersionUID = 1L;
	
	public CompanyAlreadyExistsException() {
		super();
}

	public CompanyAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
}

	public CompanyAlreadyExistsException(String message) {
		super(message);
}
	
	
	

}
