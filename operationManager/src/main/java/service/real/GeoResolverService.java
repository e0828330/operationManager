package service.real;

import model.Doctor;
import model.OPSlot;
import model.Patient;
import model.dto.OPSlotDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.Distance;
import org.springframework.data.mongodb.core.geo.Metrics;
import org.springframework.stereotype.Service;

import repository.DoctorRepository;
import repository.OPSlotRepository;
import service.IGeoResolverService;

@Service
public class GeoResolverService implements IGeoResolverService {

	@Autowired
	private OPSlotRepository repo;

	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private PatientService patientService;

	@Override
	public OPSlot findSlot(OPSlotDTO params) {
		Patient patient = patientService.getById(params.getPatientID());

		if (patient == null) {
			return null; // TODO: Throw exception for notification
		}

		Doctor doctor = doctorRepository.findOne(params.getDoctorID());
		
		if (doctor == null) {
			return null; // TODO: Throw exception for notification
		}
		
		OPSlot slot = repo.findBestInRange(patient.getPosition(), new Distance(params.getDistance(), Metrics.KILOMETERS), params.getType(), params.getFrom(), params.getTo());
		if (slot != null) {
			slot.setPatient(patient);
			slot.setDoctor(doctor);
		}
		return slot;
	}

}
