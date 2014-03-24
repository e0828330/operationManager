package restService;

import java.util.List;

import model.Patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import service.TestService;

@Controller
public class MainController {

	@Autowired
	private TestService service;

	@RequestMapping("/rest")
	public @ResponseBody List<Patient> root() {
		return service.getPatients();
	}

	@RequestMapping("/rest/addPatient/{firstName}/{lastName}")
	public @ResponseBody String addPatient(@PathVariable("firstName") String firstName, @PathVariable("lastName") String lastName) {
		service.addPatient(firstName, lastName);
		return "Patient saved!";
	}
}
