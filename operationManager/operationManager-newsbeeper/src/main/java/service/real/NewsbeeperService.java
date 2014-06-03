package service.real;

import model.Doctor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.DoctorRepository;
import service.INewsbeeperService;

@Service
public class NewsbeeperService implements INewsbeeperService {

	@Autowired
	private DoctorRepository repo;
	
	@Override
	public Doctor getDoctorById(String id) {
		return repo.findOne(id);
	}

}
