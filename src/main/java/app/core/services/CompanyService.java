package app.core.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Coupon.Category;
import app.core.exceptions.CompanyDoesNotExistException;
import app.core.exceptions.CouponAlreadyExistsException;
import app.core.exceptions.CouponDoesNotExistException;
import app.core.exceptions.CouponFeaturesException;
import app.core.exceptions.CouponSystemException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;

@Service
@Scope("prototype")
@Transactional
public class CompanyService extends ClientService {

	private int companyID;
	@Autowired
	private FileStorageService storageService;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private CouponRepository couponRepository;


	public void setCompanyID(int companyID) {
		this.companyID = companyID;
	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {

		Company company = companyRepository.findByEmailIgnoreCaseAndPassword(email, password);

		if (company != null) {
			setCompanyID(company.getId());
			return true;

		} else {
			throw new CompanyDoesNotExistException("Company login - failed - not in the system");
		}

	}

	/**
	 * a method that adds a coupon: cannot add a coupon with the same title of an
	 * existing coupon of the same company
	 * 
	 * @param coupon
	 * @return coupon after addition
	 * @throws CouponSystemException
	 */
	public Coupon addCoupon(Coupon coupon, MultipartFile imageFile) throws CouponSystemException {

		if (!couponRepository.existsById(coupon.getId())
				&& !couponRepository.existsByCompanyIdAndTitleIgnoreCase(this.companyID, coupon.getTitle())) {
			if (coupon.getStartDate().isBefore(coupon.getEndDate()) && coupon.getEndDate().isAfter(LocalDate.now())) {

				String imagePath = this.storageService.storeFile(imageFile);
				coupon.setImage(imagePath);

				Company dbCompany = getCompanyDetails();
				coupon.setCompany(dbCompany);
				couponRepository.save(coupon);
				return coupon;

			} else {
				throw new CouponFeaturesException("Coupon end date is not valid");
			}
		} else {
			throw new CouponAlreadyExistsException("Coupon already exists");
		}
	}

	/**
	 * a method that updates details of an existing coupon: cannot update coupon id
	 * and company id
	 * 
	 * @param coupon
	 * @return coupon after update
	 * @throws CouponSystemException
	 */
	public Coupon updateCoupon(Coupon coupon, MultipartFile imageFile) throws CouponSystemException {

		Coupon dbCoupon = getOneCoupon(coupon.getId());

		if (dbCoupon != null) {

			if (dbCoupon.getCompany().getId() == this.companyID) {

				if (!(couponRepository.existsByIdNotAndCompanyIdAndTitleIgnoreCase(dbCoupon.getId(), this.companyID,
						coupon.getTitle()))) {

					if (coupon.getStartDate().isBefore(coupon.getEndDate())
							&& coupon.getEndDate().isAfter(LocalDate.now())) {
						
						if (imageFile != null) {
							this.storageService.deleteFile(coupon.getImage());
							String imagePath = this.storageService.storeFile(imageFile);
							coupon.setImage(imagePath);
						}
						
						dbCoupon.setTitle(coupon.getTitle());
						dbCoupon.setCategory(coupon.getCategory());
						dbCoupon.setDescription(coupon.getDescription());
						dbCoupon.setStartDate(coupon.getStartDate());
						dbCoupon.setEndDate(coupon.getEndDate());
						dbCoupon.setAmount(coupon.getAmount());
						dbCoupon.setImage(coupon.getImage());
						dbCoupon.setPrice(coupon.getPrice());
						return dbCoupon;

					} else {
						throw new CouponFeaturesException("Coupon end date is not valid");
					}
				} else {
					throw new CouponFeaturesException("Company already has a coupon with a similar title");
				}
			} else {
				throw new CouponFeaturesException("Coupon doesn't belong to company");
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
	 * a method that deletes a coupon: deletes historical purchases of the coupon
	 * 
	 * @param couponID
	 * @return boolean after coupon deletion
	 * @throws CouponSystemException
	 */
	public void deleteCoupon(int couponID) throws CouponSystemException {

		Coupon dbCoupon = getOneCoupon(couponID);

		if (dbCoupon != null) {

			if (dbCoupon.getCompany().getId() == this.companyID) {

				this.storageService.deleteFile(dbCoupon.getImage());
				couponRepository.deleteById(couponID);

			} else {
				throw new CouponFeaturesException("Coupon doesn't belong to company");
			}
		} else {
			throw new CouponDoesNotExistException("Coupon not in the system");
		}

	}

	/**
	 * a method that gets all company coupons (after login)
	 * 
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	public List<Coupon> getAllCompanyCoupons() throws CouponSystemException {
		return couponRepository.findAllByCompanyId(this.companyID);
	}

	/**
	 * a method that gets company coupons of specific category (after login)
	 * 
	 * @param category
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCompanyCoupons(Category category) throws CouponSystemException {
		return couponRepository.findAllByCompanyIdAndCategory(this.companyID, category);
	}

	/**
	 * a method that gets company coupons of price lower than the given one (after
	 * login)
	 * 
	 * @param maxPrice
	 * @return list of coupons
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCompanyCoupons(double maxPrice) throws CouponSystemException {
		return couponRepository.findAllByCompanyIdAndPriceLessThanEqual(this.companyID, maxPrice);
	}

	/**
	 * a method that gets a company details (after login)
	 * 
	 * @return company
	 * @throws CouponSystemException
	 */
	public Company getCompanyDetails() throws CouponSystemException {

		Optional<Company> opt = companyRepository.findById(this.companyID);

		if (opt.isPresent()) {
			return opt.get();

		} else {
			throw new CompanyDoesNotExistException("Company not in the system");
		}
	}

//	---------------------------------------------------------------------------

	/**
	 * a method that gets sum of company coupons that were purchased (after login)
	 * 
	 * @param amount
	 * @return sum of coupons
	 * @throws CouponSystemException
	 */
	public int getCompanyCoupons() throws CouponSystemException {
		return couponRepository.getCompanySumPurchases(this.companyID);
	}
}
