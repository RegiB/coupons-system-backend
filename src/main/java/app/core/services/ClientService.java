package app.core.services;

import app.core.exceptions.CouponSystemException;

/**
 * abstract service class for the 3 types of clients
 */
public abstract class ClientService {

	/**
	 * an abstract method of login
	 * 
	 * @param email
	 * @param password
	 * @return boolean
	 * @throws CouponSystemException
	 */
	public abstract boolean login(String email, String password) throws CouponSystemException;
}
