package app.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.core.entities.Customer;

/**
 * repository for customer entity
 */
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	boolean existsByEmailIgnoreCaseAndPassword(String email, String password);

	Customer findByEmailIgnoreCaseAndPassword(String email, String password);

	boolean existsByEmailIgnoreCase(String email);
	
	boolean existsByIdNotAndEmailIgnoreCase(int customerId, String email);
}
