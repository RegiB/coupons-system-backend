package app.core.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;
import app.core.exceptions.CouponDoesNotExistException;
import app.core.exceptions.CouponFeaturesException;
import app.core.exceptions.CouponSystemException;
import app.core.exceptions.CustomerDoesNotExistException;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

@Service
@Scope("prototype")
@Transactional
public class CustomerService extends ClientService {

	private int customerID;
	private CustomerRepository customerRepository;
	private CouponRepository couponRepository;

	@Autowired
	public CustomerService(CustomerRepository customerRepository, CouponRepository couponRepository) {
		this.customerRepository = customerRepository;
		this.couponRepository = couponRepository;
	}

	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {

		Customer customer = customerRepository.findByEmailIgnoreCaseAndPassword(email, password);

		if (customer != null) {
			setCustomerID(customer.getId());
			return true;

		} else {
			throw new CustomerDoesNotExistException("Customer not in the system");
		}

	}

	/**
	 * a method that adds coupon purchase to the system: customer can purchase the
	 * same coupon only once, cannot purchase coupon if its amount is 0, cannot
	 * purchase coupon if its end date has already past
	 * 
	 * @param coupon
	 * @return coupon after purchase
	 * @throws CouponSystemException
	 */
	public Coupon purchaseCoupon(Coupon coupon) throws CouponSystemException {

		if (couponRepository.existsById(coupon.getId())) {

			if (!couponRepository.existsByCustomersIdAndId(this.customerID, coupon.getId())) {

				if (couponRepository.existsByIdAndAmountGreaterThan(coupon.getId(), 0)) {

					if (couponRepository.existsByIdAndEndDateAfter(coupon.getId(), LocalDate.now())) {

						Coupon dbCoupon = getOneCoupon(coupon.getId());
						Customer loginCustomer = getCustomerDetails();
						loginCustomer.addCoupon(dbCoupon);
						dbCoupon.setAmount(dbCoupon.getAmount() - 1);
						dbCoupon.setPurchase(dbCoupon.getPurchase() + 1);
						return dbCoupon;

					} else {
						throw new CouponFeaturesException("Coupon is out of date");
					}
				} else {
					throw new CouponFeaturesException("Coupon is out of stock");
				}
			} else {
				throw new CouponFeaturesException("Coupon already purchased by customer");
			}
		} else {
			throw new CouponDoesNotExistException("Coupon not in the system");
		}

	}

	private Coupon getOneCoupon(int couponId) {
		Optional<Coupon> opt = couponRepository.findById(couponId);
		if (opt.isPresent()) {
			return opt.get();
		}
		return null;
	}

	/**
	 * a method that returns all customer's coupons (after login)
	 * 
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCustomerCoupons() throws CouponSystemException {
		return couponRepository.findAllByCustomersId(this.customerID);
	}

	/**
	 * a method that gets customer's coupons of specific category (after login)
	 * 
	 * @param category
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCustomerCoupons(Category category) throws CouponSystemException {
		return couponRepository.findAllByCustomersIdAndCategory(this.customerID, category);
	}

	/**
	 * a method that gets customer's coupons of price lower than the given one
	 * (after login)
	 * 
	 * @param maxPrice
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCustomerCoupons(double maxPrice) throws CouponSystemException {
		return couponRepository.findAllByCustomersIdAndPriceLessThanEqual(this.customerID, maxPrice);
	}

	/**
	 * a method that gets a customer's details (after login)
	 * 
	 * @return customer
	 * @throws CouponSystemException
	 */
	public Customer getCustomerDetails() throws CouponSystemException {

		Optional<Customer> opt = customerRepository.findById(customerID);

		if (opt.isPresent()) {
			return opt.get();

		} else {
			throw new CustomerDoesNotExistException("Customer not in the system");

		}

	}

//	-------------------------------------------

	/**
	 * a method that gets coupons that current customer can purchase: checks 
	 * previous purchase of coupon, checks end date after today, checks amount more than
	 * 0
	 * 
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	public List<Coupon> couponsForPurchaseByCustomer() throws CouponSystemException {
		return couponRepository.findAllByCustomersNotContainingAndAmountGreaterThanAndEndDateAfter(getCustomerDetails(),
				0, LocalDate.now());

	}

}
