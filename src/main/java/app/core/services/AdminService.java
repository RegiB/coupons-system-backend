package app.core.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CompanyAlreadyExistsException;
import app.core.exceptions.CompanyDoesNotExistException;
import app.core.exceptions.CouponSystemException;
import app.core.exceptions.CustomerAlreadyExistsException;
import app.core.exceptions.CustomerDoesNotExistException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CustomerRepository;

@Service
@Transactional
public class AdminService extends ClientService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private CompanyRepository companyRepository;


	@Override
	public boolean login(String email, String password) throws CouponSystemException {

		if (email.equals("admin@admin.com") && password.equals("admin")) {
			return true;
		} else {
			throw new CouponSystemException("administrator login failed");
		}
	}

	/**
	 * a method that adds a company: cannot add company with the same name or email
	 * of an existing company
	 * 
	 * @param company
	 * @return company after addition
	 * @throws CouponSystemException
	 */
	public Company addCompany(Company company) throws CouponSystemException {

		if (!companyRepository.existsById(company.getId())
				&& !companyRepository.existsByEmailIgnoreCaseAndPassword(company.getEmail(), company.getPassword())) {

			if (!companyRepository.existsByNameIgnoreCase(company.getName())) {

				if (!companyRepository.existsByEmailIgnoreCase(company.getEmail())) {
					return companyRepository.save(company);

				} else {
					throw new CompanyAlreadyExistsException("Company with the same email already exists");
				}
			} else {
				throw new CompanyAlreadyExistsException("Company with the same name already exists");
			}
		} else {
			throw new CompanyAlreadyExistsException("Company already exists");
		}

	}

	/**
	 * a method that updates an existing company: cannot update companyID and
	 * company name
	 * 
	 * @param company
	 * @return company after update
	 * @throws CouponSystemException
	 */
	public Company updateCompany(Company company) throws CouponSystemException {

		Company dbCompany = getOneCompany(company.getId());

		if (dbCompany != null) {

			if (!companyRepository.existsByIdNotAndEmailIgnoreCase(dbCompany.getId(), company.getEmail())) {
				dbCompany.setEmail(company.getEmail());
				dbCompany.setPassword(company.getPassword());
				return dbCompany;

			} else {
				throw new CompanyAlreadyExistsException("Company with the same email already exists");
			}
		} else {
			throw new CompanyDoesNotExistException("Company not in the system");
		}

	}

	/**
	 * a method that deletes a company and all its coupons and historical coupon
	 * purchases
	 * 
	 * @param companyID
	 * @return boolean after company deletion
	 * @throws CouponSystemException
	 */
	public void deleteCompany(int companyID) throws CouponSystemException {

		if (companyRepository.existsById(companyID)) {
			companyRepository.deleteById(companyID);

		} else
			throw new CompanyDoesNotExistException("Company not in the system");
	}

	/**
	 * a method that returns all companies from the system
	 * 
	 * @return list of all companies
	 * @throws CouponSystemException
	 */
	public List<Company> getAllCompanies() throws CouponSystemException {
		return companyRepository.findAll();
	}

	/**
	 * a method that returns a company
	 * 
	 * @param companyID
	 * @return company
	 * @throws CouponSystemException
	 */
	public Company getOneCompany(int companyID) throws CouponSystemException {

		Optional<Company> opt = companyRepository.findById(companyID);

		if (opt.isPresent()) {
			return opt.get();

		} else {
			throw new CompanyDoesNotExistException("Company not in the system");
		}
	}

	/**
	 * a method that adds a customer: cannot add a customer with the same email of
	 * an existing customer
	 * 
	 * @param customer
	 * @return customer after addition
	 * @throws CouponSystemException
	 */
	public Customer addCustomer(Customer customer) throws CouponSystemException {

		if (!customerRepository.existsById(customer.getId()) && !customerRepository
				.existsByEmailIgnoreCaseAndPassword(customer.getEmail(), customer.getPassword())) {

			if (!customerRepository.existsByEmailIgnoreCase(customer.getEmail())) {
				return customerRepository.save(customer);

			} else {
				throw new CustomerAlreadyExistsException("Customer with the same email already exists");
			}
		} else {
			throw new CustomerAlreadyExistsException("Customer already exists");
		}
	}

	/**
	 * a method that updates an existing customer: cannot update customer id
	 * 
	 * @param customer
	 * @return customer after update
	 * @throws CouponSystemException
	 */
	public Customer updateCustomer(Customer customer) throws CouponSystemException {

		Customer dbCustomer = getOneCustomer(customer.getId());

		if (dbCustomer != null) {

			if (!customerRepository.existsByIdNotAndEmailIgnoreCase(dbCustomer.getId(), customer.getEmail())) {
				dbCustomer.setFirstName(customer.getFirstName());
				dbCustomer.setLastName(customer.getLastName());
				dbCustomer.setEmail(customer.getEmail());
				dbCustomer.setPassword(customer.getPassword());
				return dbCustomer;

			} else {
				throw new CustomerAlreadyExistsException("Customer with the same email already exists");
			}
		} else {
			throw new CustomerDoesNotExistException("Customer not in the system");
		}
	}

	/**
	 * a method that deletes a customer and his coupon purchases
	 * 
	 * @param customerID
	 * @return boolean after customer deletion
	 * @throws CouponSystemException
	 */
	public void deleteCustomer(int customerID) throws CouponSystemException {

		if (customerRepository.existsById(customerID)) {
			customerRepository.deleteById(customerID);

		} else {
			throw new CustomerDoesNotExistException("Customer not in the system");
		}

	}

	/**
	 * a method the gets all customers from the system
	 * 
	 * @return a list of customers
	 * @throws CouponSystemException
	 */
	public List<Customer> getAllCustomers() throws CouponSystemException {
		return customerRepository.findAll();
	}

	/**
	 * a method that returns only one customer
	 * 
	 * @param customerID
	 * @return customer
	 * @throws CouponSystemException
	 */
	public Customer getOneCustomer(int customerID) throws CouponSystemException {
		Optional<Customer> opt = customerRepository.findById(customerID);

		if (opt.isPresent()) {
			return opt.get();
		} else {
			throw new CustomerDoesNotExistException("Customer not in the system");
		}

	}

}
