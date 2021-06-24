package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;
import app.core.services.AdminService;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	public AdminController() {
		super();
	}

	@PostMapping("/companies")
	public Company addCompany(@RequestBody Company company, @RequestHeader String token) {
		try {
			return adminService.addCompany(company);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Adding company - failed - " + e.getMessage());
		}
	}

	@PutMapping("/companies")
	public Company updateCompany(@RequestBody Company company, @RequestHeader String token) {
		try {
			return adminService.updateCompany(company);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Updating company - failed - " + e.getMessage());
		}

	}

	@DeleteMapping("/companies/{companyID}")
	public void deleteCompany(@PathVariable int companyID, @RequestHeader String token) {
		try {
			adminService.deleteCompany(companyID);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deleting company - failed - " + e.getMessage());
		}
	}

	@GetMapping("/companies")
	public List<Company> getAllCompanies(@RequestHeader String token) {
		try {
			return adminService.getAllCompanies();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Getting companies - failed - " + e.getMessage());
		}
	}

	@GetMapping("/companies/{companyID}")
	public Company getOneCompany(@PathVariable int companyID, @RequestHeader String token) {
		try {
			return adminService.getOneCompany(companyID);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Getting company - failed - " + e.getMessage());
		}
	}

	@PostMapping("/customers")
	public Customer addCustomer(@RequestBody Customer customer, @RequestHeader String token) {
		try {
			return adminService.addCustomer(customer);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Adding customer - failed - " + e.getMessage());
		}
	}

	@PutMapping("/customers")
	public Customer updateCustomer(@RequestBody Customer customer, @RequestHeader String token) {
		try {
			return adminService.updateCustomer(customer);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Updating customer - failed - " + e.getMessage());
		}
	}

	@DeleteMapping("/customers/{customerID}")
	public void deleteCustomer(@PathVariable int customerID, @RequestHeader String token) {
		try {
			adminService.deleteCustomer(customerID);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deleting customer - failed - " + e.getMessage());
		}
	}

	@GetMapping("/customers")
	public List<Customer> getAllCustomers(@RequestHeader String token) {
		try {
			return adminService.getAllCustomers();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Getting customers - failed - " + e.getMessage());
		}
	}

	@GetMapping("/customers/{customerID}")
	public Customer getOneCustomer(@PathVariable int customerID, @RequestHeader String token) {
		try {
			return adminService.getOneCustomer(customerID);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Getting customer - failed - " + e.getMessage());
		}

	}
}
