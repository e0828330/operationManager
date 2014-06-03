package service;

import model.Doctor;

public interface INewsbeeperService {
	/**
	 * Get a doctor with the identification @id
	 * 
	 * @param id The id of the doctor
	 * @return Returns the Doctor object or null if not found
	 */
	public Doctor getDoctorById(String id);
	
}
