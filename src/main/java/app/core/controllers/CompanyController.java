package app.core.controllers;

import java.util.List;

import org.springframework.aop.AopInvocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import app.core.JWT.JwtUtil;
import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.exceptions.CouponSystemException;
import app.core.services.CompanyService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/company")
public class CompanyController {

	@Autowired
	private CompanyService companyService;
	@Autowired
	private JwtUtil jwtUtil;

	public CompanyController() {
		super();
	}

	private CompanyService getService(String token) {
		companyService.setCompanyID(jwtUtil.extractClientId(token));
		return companyService;
	}

	@PostMapping("/coupons")
	public Coupon addCoupon(@RequestHeader String token, @ModelAttribute Coupon coupon,
			@RequestParam MultipartFile imageFile) {
		try {
			return getService(token).addCoupon(coupon, imageFile);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Adding coupon - failed - " + e.getMessage());
		}
	}

	@PutMapping("/coupons")
	public Coupon updateCoupon(@RequestHeader String token, @ModelAttribute Coupon coupon,
			@RequestParam(required = false) MultipartFile imageFile) {
		try {
			return getService(token).updateCoupon(coupon, imageFile);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Updating coupon - failed - " + e.getMessage());
		}
	}

	@DeleteMapping("/coupons/{couponID}")
	public void deleteCoupon(@PathVariable int couponID, @RequestHeader String token) {
		try {
			getService(token).deleteCoupon(couponID);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deleting coupon - failed - " + e.getMessage());
		}
	}

	@GetMapping("/coupons")
	public List<Coupon> getAllCompanyCoupons(@RequestHeader String token) {
		try {
			return getService(token).getAllCompanyCoupons();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					" getting all coupons - failed - " + e.getMessage());
		}
	}

	@GetMapping("/coupons/category/{category}")
	public List<Coupon> getCompanyCoupons(@PathVariable Category category, @RequestHeader String token) {
		try {
			return getService(token).getCompanyCoupons(category);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Getting coupons by category - failed - " + e.getMessage());
		}

	}

	@GetMapping("/coupons/price/{maxPrice}")
	public List<Coupon> getCompanyCoupons(@PathVariable double maxPrice, @RequestHeader String token) {
		try {
			return getService(token).getCompanyCoupons(maxPrice);
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Getting coupons by price - failed - " + e.getMessage());
		}
	}

	@GetMapping("/coupons/purchased/")
	public int getCompanyCoupons(@RequestHeader String token) {
		try {
			return getService(token).getCompanyCoupons();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Getting coupons by price - failed - " + e.getMessage());
		} catch (AopInvocationException e) {
			return 0;
		}
	}

	@GetMapping()
	public Company getCompanyDetails(@RequestHeader String token) {
		try {
			return getService(token).getCompanyDetails();
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"Getting company details - failed - " + e.getMessage());
		}
	}

}
