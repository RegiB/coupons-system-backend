package app.core.services;

import java.time.LocalDate;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.core.exceptions.CouponSystemException;
import app.core.repositories.CouponRepository;

@Service
@Transactional
public class DailyJobService {

	@Autowired
	private CouponRepository couponRepository;

	/**
	 * a method for thread deleting expired coupons
	 * 
	 * @throws CouponSystemException
	 */
	public void deleteExpiredCoupons() throws CouponSystemException {
		couponRepository.deleteAllByEndDateBefore(LocalDate.now());
	}

}
