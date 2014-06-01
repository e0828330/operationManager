package service;

import java.util.List;

import model.Patient;

public interface IPatientService {

	/**
	 * Returns the patient with the given id
	 *
	 * @param id
	 * @return
	 */
	public Patient getById(String id);

	/**
	 * Returns the list of patients
	 *
	 * @return
	 */
	public List<Patient> getPatients();

	/**
	 * Returns the list of patients for a given keyword
	 * (searches first and lastname)
	 * 
	 * The case is not case sensitve
	 * 
	 * @return
	 */
	public List<Patient> getPatients(String search);
}
