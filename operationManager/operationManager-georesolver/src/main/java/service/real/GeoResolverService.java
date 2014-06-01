package service.real;

import model.Doctor;
import model.OPSlot;
import model.Patient;
import model.dto.OPSlotDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Service;

import repository.DoctorRepository;
import repository.OPSlotRepository;
import service.IGeoResolverService;
import service.exceptions.ServiceException;

@Service
public class GeoResolverService implements IGeoResolverService {

	@Autowired
	private OPSlotRepository repo;

	@Autowired
	private DoctorRepository doctorRepository;
	
	@Autowired
	private PatientService patientService;

	@Override
	public OPSlot findSlot(OPSlotDTO params) throws ServiceException {
		Patient patient = patientService.getById(params.getPatientID());

		if (patient == null) {
			throw new ServiceException("Invalid patient!");
		}

		Doctor doctor = doctorRepository.findOne(params.getDoctorID());
		
		if (doctor == null) {
			throw new ServiceException("Invalid doctor!");
		}
		
		OPSlot slot = repo.findBestInRange(patient.getPosition(), new Distance(params.getDistance(), Metrics.KILOMETERS), params.getType(), params.getFrom(), params.getTo());
		if (slot != null) {
			slot.setPatient(patient);
			slot.setDoctor(doctor);
		}
		return slot;
	}

}
