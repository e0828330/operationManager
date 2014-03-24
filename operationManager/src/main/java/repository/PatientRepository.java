package repository;

import model.Patient;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
@Lazy
public interface PatientRepository extends PagingAndSortingRepository<Patient, String> {

}
