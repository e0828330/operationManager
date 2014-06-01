package newsbeeper.service;

import model.Doctor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.DoctorRepository;

@Service
public class NewsbeeperService implements INewsbeeperService {

	@Autowired
	private DoctorRepository repo;
	
	@Override
	public Doctor getDoctorById(String id) {
		return repo.findOne(id);
	}

}
