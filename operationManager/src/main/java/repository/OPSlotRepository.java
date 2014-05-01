package repository;

import java.util.List;

import model.OPSlot;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface OPSlotRepository extends PagingAndSortingRepository<OPSlot, String>{
	
	/**
	 * Returns all rows matching the given filter object
	 * 
	 * @param firstName
	 * @param hospital
	 * @param doctor
	 * @param status
	 * @param type
	 * @param dateMin
	 * @param dateMax
	 * @param page
	 * @return
	 */
	@Query("{ $and : [ "
			+ "{ $or : [ {'patient' : { $exists: false } }, "
			+ "	{'patient.firstName' : {$regex : ?0, $options: 'i'} }, "
			+ "	{'patient.lastName' : {$regex : ?0, $options: 'i'} } ]"
			+ "}, "
			+ "{'hospital.name' : {$regex : ?1, $options: 'i'} }, "
			+ "{ $or : [ {'doctor' : { $exists: false } }, "
			+ "	{'doctor.firstName' : {$regex : ?0, $options: 'i'} }, "
			+ "	{'doctor.lastName' : {$regex : ?0, $options: 'i'} } ]"
			+ "}, "
			+ "{'status' : {$regex : ?3, $options: 'i'} }, "
			+ "{'type' : {$regex : ?4, $options: 'i'} } "
			+ "], "
			+ "$where : '?5 == null || this.date > new Date(?5) && this.date < new Date(?6)' } }")
	public List<OPSlot> findByFilter(String firstName,
									 String hospital,
									 String doctor,
									 String status,
									 String type,
									 String dateMin,
									 String dateMax,
									 Pageable page);
	
	
	/**
	 * Count all rows matching the given filter object
	 * 
	 * @param firstName
	 * @param hospital
	 * @param doctor
	 * @param status
	 * @param type
	 * @param dateMin
	 * @param dateMax
	 * @return
	 */
	@Query(value = 
			"{ $and : [ "
			+ "{ $or : [ {'patient' : { $exists: false } }, "
			+ "	{'patient.firstName' : {$regex : ?0, $options: 'i'} }, "
			+ "	{'patient.lastName' : {$regex : ?0, $options: 'i'} } ]"
			+ "}, "
			+ "{'hospital.name' : {$regex : ?1, $options: 'i'} }, "
			+ "{ $or : [ {'doctor' : { $exists: false } }, "
			+ "	{'doctor.firstName' : {$regex : ?0, $options: 'i'} }, "
			+ "	{'doctor.lastName' : {$regex : ?0, $options: 'i'} } ]"
			+ "}, "
			+ "{'status' : {$regex : ?3, $options: 'i'} }, "
			+ "{'type' : {$regex : ?4, $options: 'i'} } "
			+ "], "
			+ "$where : '?5 == null || this.date > new Date(?5) && this.date < new Date(?6)' } }", count = true)
	public Long countByFilter(String firstName,
								 String hospital,
								 String doctor,
								 String status,
								 String type,
								 String dateMin,
								 String dateMax);
}
