package service.real;

import model.Doctor;
import model.Hospital;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.DoctorRepository;
import repository.HospitalRepository;
import service.INewsbeeperService;

@Service
public class NewsbeeperService implements INewsbeeperService {

	@Autowired
	private DoctorRepository repoD;
	
	@Autowired
	private HospitalRepository repoH;
	
	@Override
	public Doctor getDoctorById(String id) {
		return repoD.findOne(id);
	}

	@Override
	public Hospital getHospitalById(String id) {
		return repoH.findOne(id);
	}

}
