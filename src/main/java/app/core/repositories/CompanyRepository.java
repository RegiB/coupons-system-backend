package app.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.core.entities.Company;

/**
 * repository for company entity
 */
public interface CompanyRepository extends JpaRepository<Company, Integer> {

	boolean existsByEmailIgnoreCaseAndPassword(String email, String password);

	Company findByEmailIgnoreCaseAndPassword(String email, String password);

	boolean existsByNameIgnoreCase(String name);

	boolean existsByEmailIgnoreCase(String email);
	
	boolean existsByIdNotAndEmailIgnoreCase(int companyId, String email);

}
