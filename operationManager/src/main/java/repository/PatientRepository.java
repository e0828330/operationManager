package repository;

import model.Patient;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PatientRepository extends PagingAndSortingRepository<Patient, String> {
	public Patient findByUsernameAndPassword(String username, String password);
}
