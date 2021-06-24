package app.core.clientsLogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.core.JWT.JwtUtil.ClientDetails.ClientType;
import app.core.exceptions.CouponSystemException;
import app.core.services.AdminService;
import app.core.services.ClientService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;

/**
 * class LoginManager manages the login of clients to the system
 *
 */
@Component
public class LoginManager {

	@Autowired
	private ApplicationContext ctx;

	public LoginManager() {
	}

	/**
	 * a method that checks email and password according to client type
	 * 
	 * @param email
	 * @param password
	 * @param clientType
	 * @return clientService or null if not logged in
	 * @throws CouponSystemException
	 */
	public ClientService login(String email, String password, ClientType clientType) throws CouponSystemException {

		ClientService clientService = null;

		switch (clientType) {

		case ADMINISTRATOR:
			clientService = ctx.getBean(AdminService.class);
			if (clientService.login(email, password)) {
				return clientService;
			} else {
				return null;
			}

		case COMPANY:
			clientService = ctx.getBean(CompanyService.class);
			if (clientService.login(email, password)) {
				return clientService;
			} else {
				return null;
			}

		case CUSTOMER:
			clientService = ctx.getBean(CustomerService.class);
			if (clientService.login(email, password)) {
				return clientService;
			} else {
				return null;
			}
		}
		return clientService;

	}
}
