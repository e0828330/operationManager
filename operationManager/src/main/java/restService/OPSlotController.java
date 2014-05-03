package restService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import model.OPSlot;
import model.User;
import model.dto.OPSlotFilter;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.IAuthenticationService;
import service.IOPSlotService;

@Controller
public class OPSlotController {

	@Autowired
	private IOPSlotService opSlotService;
	
	@Autowired
	IAuthenticationService authenticationService;
	
	@RequestMapping("/rest/getSlots/")
	/**
	 * Returns the list of opSlot that the given user (authenticated by username and password)
	 * if no user credentials are given just the public list is returned
	 * 
	 * The list can optional by filtered by date, from, to, doctor, patient or hospital.
	 * The date has to be supplied in "dd.mm.yyyy" format while the times from and to
	 * have to be in "HH:mm" format.
	 * 
	 * @param username
	 * @param password
	 * @param date
	 * @param from
	 * @param to
	 * @param patient
	 * @param doctor
	 * @param hospital
	 * @return
	 */
	public @ResponseBody List<OPSlot> getSlots(@RequestParam(value="username", required=false, defaultValue="") String username,
											   @RequestParam(value="password", required=false, defaultValue="") String password,
											   @RequestParam(value="date", required=false, defaultValue="") String date,
											   @RequestParam(value="from", required=false, defaultValue="") String from,
											   @RequestParam(value="to", required=false, defaultValue="") String to,
											   @RequestParam(value="patient", required=false, defaultValue="") String patient,
											   @RequestParam(value="doctor", required=false, defaultValue="") String doctor,
											   @RequestParam(value="hospital", required=false, defaultValue="") String hospital) {
		User user = authenticationService.authenticate(username, password);
		// TODO: Pass role
		
		OPSlotFilter filter = new OPSlotFilter();
		
		SimpleDateFormat dateParser = new SimpleDateFormat("dd.MM.yyyy");
		try {
			filter.setDate(dateParser.parse(date));
		} catch (ParseException e) {
			// TODO: Pass exceptions?
		}
		
		dateParser = new SimpleDateFormat("HH:mm");
		try {
			filter.setFrom(dateParser.parse(from));
		} catch (ParseException e) {
			// TODO: Pass exceptions?
		}

		filter.setPatient(patient);
		filter.setDoctor(doctor);
		filter.setHospital(hospital);
		
		return opSlotService.getOPSlots(new SortParam<String>("date", false), filter, 0, Integer.MAX_VALUE);
	}
}
