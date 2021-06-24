package app.core.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.exceptions.CouponSystemException;
import app.core.repositories.CouponRepository;

@Service
@Transactional
public class GeneralService {

	@Autowired
	protected CouponRepository couponRepository;

	public List<Coupon> couponsByCategory(Category category) throws CouponSystemException {
		return couponRepository.findAllByCategory(category);

	}

//	for top picks - most purchases
	public List<Coupon> couponsTopPurchased() throws CouponSystemException {
		return couponRepository.findTop6ByOrderByPurchaseDesc();
	}


}
