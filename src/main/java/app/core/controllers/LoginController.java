package app.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.JWT.JwtUtil;
import app.core.JWT.JwtUtil.ClientDetails;
import app.core.JWT.JwtUtil.ClientDetails.ClientType;
import app.core.clientsLogin.LoginManager;
import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;
import app.core.services.CompanyService;
import app.core.services.CustomerService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class LoginController {

	@Autowired
	private LoginManager loginManager;
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/login")
	public ClientDetails login(@RequestHeader String email, @RequestHeader String password,
			@RequestHeader ClientType clientType) {

		ClientDetails clientDetails = null;

		try {
			switch (clientType) {
			case ADMINISTRATOR:
				loginManager.login(email, password, clientType);
				clientDetails = new ClientDetails(0, email, clientType);
				clientDetails.token = this.jwtUtil.generateToken(clientDetails);
				return clientDetails;

			case COMPANY:
				CompanyService companyService = (CompanyService) loginManager.login(email, password, clientType);
				Company company = companyService.getCompanyDetails();
				clientDetails = new ClientDetails(company.getId(), email, clientType);
				clientDetails.token = this.jwtUtil.generateToken(clientDetails);
				return clientDetails;

			case CUSTOMER:
				CustomerService customerService = (CustomerService) loginManager.login(email, password, clientType);
				Customer customer = customerService.getCustomerDetails();
				clientDetails = new ClientDetails(customer.getId(), email, clientType);
				clientDetails.token = this.jwtUtil.generateToken(clientDetails);
				return clientDetails;
			}
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
					"Log in details are not valid, you are not authorized!", e);
		}
		return clientDetails;

	}
}
