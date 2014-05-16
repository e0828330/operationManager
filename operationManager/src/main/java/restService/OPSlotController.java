package restService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import model.Hospital;
import model.OPSlot;
import model.Role;
import model.User;
import model.dto.OPSlotFilter;
import model.dto.RestErrorDTO;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.IAuthenticationService;
import service.IOPSlotService;
import utils.Utils;

@Controller
public class OPSlotController {

	@Autowired
	private IOPSlotService opSlotService;
	
	@Autowired
	private IAuthenticationService authenticationService;
	
	@ExceptionHandler(RestServiceException.class)
	/**
	 * Handles exceptions for wrong service calls
	 * 
	 * @param exception
	 * @return
	 */
	public @ResponseBody RestErrorDTO handleError(RestServiceException exception) {
		RestErrorDTO dto = new RestErrorDTO();
		dto.setError(exception.getMessage());
		return dto;
	}
	
	
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
											   @RequestParam(value="hospital", required=false, defaultValue="") String hospital) throws RestServiceException {
		User user = authenticationService.authenticate(username, password);
		
		if (!username.equals("") && user.getRole().equals(Role.DEFAULT)) {
			throw new RestServiceException("Invalid username or password!");
		}
		
		OPSlotFilter filter = new OPSlotFilter();
		
		SimpleDateFormat dateParser = new SimpleDateFormat("dd.MM.yyyy");
		try {
			filter.setDate(dateParser.parse(date));
		} catch (ParseException e) {
			throw new RestServiceException("Could not parse date .. probably wrong format.");
		}
		
		dateParser = new SimpleDateFormat("HH:mm");
		try {
			filter.setFrom(dateParser.parse(from));
		} catch (ParseException e) {
			throw new RestServiceException("Could not parse date .. probably wrong format.");
		}
		
		dateParser = new SimpleDateFormat("HH:mm");
		try {
			filter.setTo(dateParser.parse(to));
		} catch (ParseException e) {
			throw new RestServiceException("Could not to date .. probably wrong format.");
		}

		filter.setPatient(patient);
		filter.setDoctor(doctor);
		filter.setHospital(hospital);
		
		return opSlotService.getOPSlots(user, new SortParam<String>("date", false), filter, 0, Integer.MAX_VALUE);
	}

	@RequestMapping("/rest/addSlot/")
	/**
	 * Adds an empty opSLot for the hospital who's credentials are given
	 * 
	 * The date has to be supplied in "dd.mm.yyyy" format while the times from and to
	 * have to be in "HH:mm" format.
	 * 
	 * @param username
	 * @param password
	 * @param date
	 * @param from
	 * @param to
	 * @return
	 */
	public @ResponseBody OPSlot addSlot(@RequestParam(value="username", required=false, defaultValue="") String username,
									    @RequestParam(value="password", required=false, defaultValue="") String password,
									    @RequestParam(value="date", required=false, defaultValue="") String date,
									    @RequestParam(value="from", required=false, defaultValue="") String from,
									    @RequestParam(value="to", required=false, defaultValue="") String to) throws RestServiceException {
		
		User user = authenticationService.authenticate(username, password);
		if (!user.getRole().equals(Role.HOSPITAL)) {
			if (user.getRole().equals(Role.DEFAULT)) {
				throw new RestServiceException("Invalid username or password!");
			}
			else {
				throw new RestServiceException("Only hospitals can add slots.");
			}
		}
		
		OPSlot slot = new OPSlot();
		slot.setHospital((Hospital) user);
		
		SimpleDateFormat dateParser = new SimpleDateFormat("dd.MM.yyyy");
		try {
			slot.setDate(dateParser.parse(date));
		} catch (ParseException e) {
			throw new RestServiceException("Could not parse date .. probably wrong format.");
		}

		dateParser = new SimpleDateFormat("HH:mm");
		try {
			slot.setFrom(Utils.adjustDayTime(slot.getDate(), dateParser.parse(from)));
		} catch (ParseException e) {
			throw new RestServiceException("Could not parse from date .. probably wrong format.");
		}
		
		try {
			slot.setTo(Utils.adjustDayTime(slot.getDate(), dateParser.parse(to)));
		} catch (ParseException e) {
			throw new RestServiceException("Could not parse to date .. probably wrong format.");
		}
		
		opSlotService.saveOPSlot(slot);
		
		return slot;
	}
}
