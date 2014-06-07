package service;

import model.Doctor;
import model.Hospital;

public interface INewsbeeperService {
	/**
	 * Get a doctor with the identification @id
	 * 
	 * @param id The id of the doctor
	 * @return Returns the Doctor object or null if not found
	 */
	public Doctor getDoctorById(String id);
	
	/**
	 * Get a hospital with the identification @id
	 * 
	 * @param id The id of the hospital
	 * @return Returns the Hospital object or null if not found
	 */
	public Hospital getHospitalById(String id);	
	
}
