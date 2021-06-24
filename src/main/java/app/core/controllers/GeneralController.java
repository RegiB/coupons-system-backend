package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.services.GeneralService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/general")
public class GeneralController {

	@Autowired
	private GeneralService generalService;

	@GetMapping("/coupons/{category}")
	public List<Coupon> getCouponsByCategory(@PathVariable Category category) {
		try {
			return generalService.couponsByCategory(category);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Getting coupons - failed - " + e.getMessage());
		}
	}

	@GetMapping("/coupons/top-picks")
	public List<Coupon> getMostPurchased() {
		try {
			return generalService.couponsTopPurchased();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Getting coupons - failed - " + e.getMessage());
		}
	}

}
