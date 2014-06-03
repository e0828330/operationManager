package repository;

import java.util.List;

import model.OPSlot;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OPSlotRepository extends OPSlotRepositoryCustom, PagingAndSortingRepository<OPSlot, String>{

	/**
	 * Returns all rows matching the given filter object and patientID
	 *
	 * @param firstName
	 * @param hospital
	 * @param doctor
	 * @param status
	 * @param type
	 * @param dateMin
	 * @param dateMax
	 * @param fromMinute
	 * @param toMinute
	 * @param page
	 * @return
	 */
	@Query("{ $and : [ "
			+ "{'hospital.name' : {$regex : ?0, $options: 'i'} }, "
			+ "{ $or : [ {'doctor' : { $exists: false } }, "
			+ "	{'doctor.firstName' : {$regex : ?1, $options: 'i'} }, "
			+ "	{'doctor.lastName' : {$regex : ?1, $options: 'i'} } ]"
			+ "}, "
			+ "{'status' : {$regex : ?2, $options: 'i'} }, "
			+ "{'type' : {$regex : ?3, $options: 'i'} } "
			+ "], "
			// Date and time handling
			+ "$where : '(this.from > new Date()) &&"
			+ "			 (?4 == null || this.date > new Date(?4) && this.date < new Date(?5)) &&"
			+ "          (?6 == null || this.from.getHours()*60+this.from.getMinutes() >= ?6) &&"
			+ "          (?7 == null || this.to.getHours()*60+this.to.getMinutes() <= ?7)'"
			+ "} }")
	public List<OPSlot> findByFilter(String hospital,
									 String doctor,
									 String status,
									 String type,
									 String dateMin,
									 String dateMax,
									 Integer fromMinute,
									 Integer toMinute,
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
	 * @param fromMinute
	 * @param toMinute
	 * @return
	 */
	@Query(value =
			"{ $and : [ "
					+ "{'hospital.name' : {$regex : ?0, $options: 'i'} }, "
					+ "{ $or : [ {'doctor' : { $exists: false } }, "
					+ "	{'doctor.firstName' : {$regex : ?1, $options: 'i'} }, "
					+ "	{'doctor.lastName' : {$regex : ?1, $options: 'i'} } ]"
					+ "}, "
					+ "{'status' : {$regex : ?2, $options: 'i'} }, "
					+ "{'type' : {$regex : ?3, $options: 'i'} } "
					+ "], "
					// Date and time handling
					+ "$where : '(this.from > new Date()) &&"
					+ "			 (?4 == null || this.date > new Date(?4) && this.date < new Date(?5)) &&"
					+ "          (?6 == null || this.from.getHours()*60+this.from.getMinutes() >= ?6) &&"
					+ "          (?7 == null || this.to.getHours()*60+this.to.getMinutes() <= ?7)'"
					+ "} }", count = true)
	public Long countByFilter(String hospital,
							  String doctor,
							  String status,
							  String type,
							  String dateMin,
							  String dateMax,
							  Integer fromMinute,
							  Integer toMinute);

	/**
	 * Returns all rows matching the given filter object
	 *
	 * @param patientId
	 * @param hospital
	 * @param doctor
	 * @param status
	 * @param type
	 * @param dateMin
	 * @param dateMax
	 * @param fromMinute
	 * @param toMinute
	 * @param page
	 * @return
	 */
	@Query("{ $and : [ "
			+ "{ 'patient._id' : {$oid: ?0} }, "
			+ "{'hospital.name' : {$regex : ?1, $options: 'i'} }, "
			+ "{ $or : [ {'doctor' : { $exists: false } }, "
			+ "	{'doctor.firstName' : {$regex : ?2, $options: 'i'} }, "
			+ "	{'doctor.lastName' : {$regex : ?2, $options: 'i'} } ]"
			+ "}, "
			+ "{'status' : {$regex : ?3, $options: 'i'} }, "
			+ "{'type' : {$regex : ?4, $options: 'i'} } "
			+ "], "
			// Date and time handling
			+ "$where : '(this.from > new Date()) &&"
			+ "			 (?5 == null || this.date > new Date(?5) && this.date < new Date(?6)) &&"
			+ "          (?7 == null || this.from.getHours()*60+this.from.getMinutes() >= ?7) &&"
			+ "          (?8 == null || this.to.getHours()*60+this.to.getMinutes() <= ?8)'"
			+ "} }")
	public List<OPSlot> findByFilterForPatient(String patientId,
											   String hospital,
											   String doctor,
											   String status,
											   String type,
											   String dateMin,
											   String dateMax,
											   Integer fromMinute,
											   Integer toMinute,
											   Pageable page);


	/**
	 * Count all rows matching the given filter object and patientID
	 *
	 * @param firstName
	 * @param hospital
	 * @param doctor
	 * @param status
	 * @param type
	 * @param dateMin
	 * @param dateMax
	 * @param fromMinute
	 * @param toMinute
	 * @return
	 */
	@Query(value="{ $and : [ "
			+ "{ 'patient._id' : {$oid: ?0} }, "
			+ "{'hospital.name' : {$regex : ?1, $options: 'i'} }, "
			+ "{ $or : [ {'doctor' : { $exists: false } }, "
			+ "	{'doctor.firstName' : {$regex : ?2, $options: 'i'} }, "
			+ "	{'doctor.lastName' : {$regex : ?2, $options: 'i'} } ]"
			+ "}, "
			+ "{'status' : {$regex : ?3, $options: 'i'} }, "
			+ "{'type' : {$regex : ?4, $options: 'i'} } "
			+ "], "
			// Date and time handling
			+ "$where : '(this.from > new Date()) &&"
			+ "			 (?5 == null || this.date > new Date(?5) && this.date < new Date(?6)) &&"
			+ "          (?7 == null || this.from.getHours()*60+this.from.getMinutes() >= ?7) &&"
			+ "          (?8 == null || this.to.getHours()*60+this.to.getMinutes() <= ?8)'"
			+ "} }", count=true)
	public Long countByFilterForPatient(String patientId,
										String hospital,
									    String doctor,
									    String status,
									    String type,
									    String dateMin,
									    String dateMax,
									    Integer fromMinute,
									    Integer toMinute);

	/**
	 * Returns all rows matching the given filter object and doctorId
	 *
	 * @param doctorId
	 * @param firstName
	 * @param hospital
	 * @param doctor
	 * @param status
	 * @param type
	 * @param dateMin
	 * @param dateMax
	 * @param fromMinute
	 * @param toMinute
	 * @param page
	 * @return
	 */
	@Query("{ $and : [ "
			+ "{ 'doctor._id' : {$oid: ?0} }, "
			+ "{ $or : [ {'patient' : { $exists: false } }, "
			+ "	{'patient.firstName' : {$regex : ?1, $options: 'i'} }, "
			+ "	{'patient.lastName' : {$regex : ?1, $options: 'i'} } ]"
			+ "}, "
			+ "{'hospital.name' : {$regex : ?2, $options: 'i'} }, "
			+ "{'status' : {$regex : ?3, $options: 'i'} }, "
			+ "{'type' : {$regex : ?4, $options: 'i'} } "
			+ "], "
			// Date and time handling
			+ "$where : '(this.from > new Date()) &&"
			+ "			 (?5 == null || this.date > new Date(?5) && this.date < new Date(?6)) &&"
			+ "          (?7 == null || this.from.getHours()*60+this.from.getMinutes() >= ?7) &&"
			+ "          (?8 == null || this.to.getHours()*60+this.to.getMinutes() <= ?8)'"
			+ "} }")
	public List<OPSlot> findByFilterForDoctor(String doctorId,
											  String firstName,
											  String hospital,
											  String status,
											  String type,
											  String dateMin,
											  String dateMax,
											  Integer fromMinute,
											  Integer toMinute,
											  Pageable page);


	/**
	 * Count all rows matching the given filter object and doctorId
	 *
	 * @param firstName
	 * @param hospital
	 * @param doctor
	 * @param status
	 * @param type
	 * @param dateMin
	 * @param dateMax
	 * @param fromMinute
	 * @param toMinute
	 * @param page
	 * @return
	 */
	@Query(value="{ $and : [ "
			+ "{ 'doctor._id' : {$oid: ?0} }, "
			+ "{ $or : [ {'patient' : { $exists: false } }, "
			+ "	{'patient.firstName' : {$regex : ?1, $options: 'i'} }, "
			+ "	{'patient.lastName' : {$regex : ?1, $options: 'i'} } ]"
			+ "}, "
			+ "{'hospital.name' : {$regex : ?2, $options: 'i'} }, "
			+ "{'status' : {$regex : ?3, $options: 'i'} }, "
			+ "{'type' : {$regex : ?4, $options: 'i'} } "
			+ "], "
			// Date and time handling
			+ "$where : '(this.from > new Date()) &&"
			+ "			 (?5 == null || this.date > new Date(?5) && this.date < new Date(?6)) &&"
			+ "          (?7 == null || this.from.getHours()*60+this.from.getMinutes() >= ?7) &&"
			+ "          (?8 == null || this.to.getHours()*60+this.to.getMinutes() <= ?8)'"
			+ "} }", count=true)
	public Long countByFilterForDoctor(String doctorId,
									   String firstName,
									   String hospital,
									   String status,
									   String type,
									   String dateMin,
									   String dateMax,
									   Integer fromMinute,
									   Integer toMinute);

	/**
	 * Returns all rows matching the given filter object and hospitalId
	 *
	 * @param firstName
	 * @param hospital
	 * @param doctor
	 * @param status
	 * @param type
	 * @param dateMin
	 * @param dateMax
	 * @param fromMinute
	 * @param toMinute
	 * @param page
	 * @return
	 */
	@Query("{ $and : [ "
			+ "{ 'hospital._id' : {$oid: ?0} }, "
			+ "{ $or : [ {'patient' : { $exists: false } }, "
			+ "	{'patient.firstName' : {$regex : ?1, $options: 'i'} }, "
			+ "	{'patient.lastName' : {$regex : ?1, $options: 'i'} } ]"
			+ "}, "
			+ "{ $or : [ {'doctor' : { $exists: false } }, "
			+ "	{'doctor.firstName' : {$regex : ?2, $options: 'i'} }, "
			+ "	{'doctor.lastName' : {$regex : ?2, $options: 'i'} } ]"
			+ "}, "
			+ "{'status' : {$regex : ?3, $options: 'i'} }, "
			+ "{'type' : {$regex : ?4, $options: 'i'} } "
			+ "], "
			// Date and time handling
			+ "$where : '(this.from > new Date()) &&"
			+ "			 (?5 == null || this.date > new Date(?5) && this.date < new Date(?6)) &&"
			+ "          (?7 == null || this.from.getHours()*60+this.from.getMinutes() >= ?7) &&"
			+ "          (?8 == null || this.to.getHours()*60+this.to.getMinutes() <= ?8)'"
			+ "} }")
	public List<OPSlot> findByFilterForHospital(String hospitalId,
												String firstName,
												String doctor,
												String status,
												String type,
												String dateMin,
												String dateMax,
												Integer fromMinute,
												Integer toMinute,
												Pageable page);

	/**
	 * Count all rows matching  the given filter object and hospitalId
	 *
	 * @param firstName
	 * @param hospital
	 * @param doctor
	 * @param status
	 * @param type
	 * @param dateMin
	 * @param dateMax
	 * @param fromMinute
	 * @param toMinute
	 * @param page
	 * @return
	 */
	@Query(value="{ $and : [ "
			+ "{ 'hospital._id' : {$oid: ?0} }, "
			+ "{ $or : [ {'patient' : { $exists: false } }, "
			+ "	{'patient.firstName' : {$regex : ?1, $options: 'i'} }, "
			+ "	{'patient.lastName' : {$regex : ?1, $options: 'i'} } ]"
			+ "}, "
			+ "{ $or : [ {'doctor' : { $exists: false } }, "
			+ "	{'doctor.firstName' : {$regex : ?2, $options: 'i'} }, "
			+ "	{'doctor.lastName' : {$regex : ?2, $options: 'i'} } ]"
			+ "}, "
			+ "{'status' : {$regex : ?3, $options: 'i'} }, "
			+ "{'type' : {$regex : ?4, $options: 'i'} } "
			+ "], "
			// Date and time handling
			+ "$where : '(this.from > new Date()) &&"
			+ "			 (?5 == null || this.date > new Date(?5) && this.date < new Date(?6)) &&"
			+ "          (?7 == null || this.from.getHours()*60+this.from.getMinutes() >= ?7) &&"
			+ "          (?8 == null || this.to.getHours()*60+this.to.getMinutes() <= ?8)'"
			+ "} }", count=true)
	public Long countByFilterForHospital(String hospitalId,
										 String firstName,
										 String doctor,
										 String status,
										 String type,
										 String dateMin,
										 String dateMax,
										 Integer fromMinute,
										 Integer toMinute);

}
