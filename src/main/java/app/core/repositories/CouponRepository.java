package app.core.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.entities.Customer;

/**
 * repository for coupon entity
 */
public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	boolean existsByCompanyIdAndTitleIgnoreCase(int companyId, String title);

	boolean existsByIdNotAndCompanyIdAndTitleIgnoreCase(int id, int companyID, String title);

	List<Coupon> findAllByCompanyId(int companyId);

	List<Coupon> findAllByCompanyIdAndCategory(int companyId, Category category);

	List<Coupon> findAllByCompanyIdAndPriceLessThanEqual(int companyId, double price);

	boolean existsByCustomersIdAndId(int customerId, int couponId);

	boolean existsByIdAndAmountGreaterThan(int id, int i);

	boolean existsByIdAndEndDateAfter(int id, LocalDate date);

	List<Coupon> findAllByCustomersId(int customerId);

	List<Coupon> findAllByCustomersIdAndCategory(int customerId, Category category);

	List<Coupon> findAllByCustomersIdAndPriceLessThanEqual(int customerId, double price);

	void deleteAllByEndDateBefore(LocalDate date);

//	----------------------------------------------------------------------------
//	More methods for front-end

	List<Coupon> findAllByCategory(Category category);

	List<Coupon> findTop6ByOrderByPurchaseDesc();

	@Query("SELECT SUM(c.purchase) FROM Coupon c WHERE company.id=:companyId")
	int getCompanySumPurchases(int companyId);

	List<Coupon> findAllByCustomersNotContainingAndAmountGreaterThanAndEndDateAfter(Customer customer, int amount,
			LocalDate date);

}
