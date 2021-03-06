package restService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.Doctor;
import model.Hospital;
import model.Notification;
import model.OPSlot;
import model.OperationStatus;
import model.OperationType;
import model.Patient;
import model.Role;
import model.User;
import model.dto.OPSlotDTO;
import model.dto.OPSlotFilter;
import model.dto.RestErrorDTO;
import model.dto.RestMessageDTO;
import model.dto.RestNotificationDTO;
import model.dto.RestOPSlotDTO;
import model.dto.RestPatientDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import service.IAuthenticationService;
import service.INotificationService;
import service.IOPSlotService;
import service.IPatientService;
import utils.Utils;

@Controller
public class RestServiceController {

	@Autowired
	private IOPSlotService opSlotService;
	
	@Autowired
	private IAuthenticationService authenticationService;
	
	@Autowired
	private IPatientService patientService;
	
	@Autowired 
	private INotificationService notificationService;
	
	
	@ExceptionHandler(RestServiceException.class)
	/**
	 * Handles exceptions for wrong service calls
	 * 
	 * @param exception
	 * @return
	 * @throws RestServiceException
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
	 * @throws RestServiceException
	 */
	public @ResponseBody List<RestOPSlotDTO> getSlots(@RequestParam(value="username", required=false, defaultValue="") String username,
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
		if (!date.isEmpty()) {
			try {
				filter.setDate(dateParser.parse(date));
			} catch (ParseException e) {
				throw new RestServiceException("Could not parse date .. probably wrong format.");
			}
		}
		
		if (!from.isEmpty()) {
			dateParser = new SimpleDateFormat("HH:mm");
			try {
				filter.setFrom(dateParser.parse(from));
			} catch (ParseException e) {
				throw new RestServiceException("Could not parse date .. probably wrong format.");
			}
		}
		
		if (!to.isEmpty()) {
			dateParser = new SimpleDateFormat("HH:mm");
			try {
				filter.setTo(dateParser.parse(to));
			} catch (ParseException e) {
				throw new RestServiceException("Could not to date .. probably wrong format.");
			}
		}

		filter.setPatient(patient);
		filter.setDoctor(doctor);
		filter.setHospital(hospital);
		
		List<OPSlot> list = opSlotService.getOPSlots(user, new Sort(Sort.Direction.DESC, "date"), filter, 0, Integer.MAX_VALUE);
		
		List<RestOPSlotDTO> results = new ArrayList<>(list.size());
		
		for(OPSlot slot : list) {
			RestOPSlotDTO dto = new RestOPSlotDTO(slot);
			if (user.getRole().equals(Role.DEFAULT)) {
				dto.setPatient(null);
			}
			results.add(dto);
		}
		
		return results;
	}

	@RequestMapping(value = "/rest/addSlot/", method = RequestMethod.POST)
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
	 * @throws RestServiceException
	 */
	public @ResponseBody RestOPSlotDTO addSlot(@RequestParam(value="username", required=true) String username,
											   @RequestParam(value="password", required=true) String password,
											   @RequestParam(value="date", required=true) String date,
											   @RequestParam(value="from", required=true) String from,
											   @RequestParam(value="to", required=true) String to) throws RestServiceException {
		
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

		slot.setStatus(OperationStatus.free);
		opSlotService.saveOPSlot(slot);
		
		return new RestOPSlotDTO(slot);
	}

	@RequestMapping("/rest/getPatients/")
	/**
	 * Returns a list of patients optionally filter by a keyword
	 * 
	 * @param username
	 * @param password
	 * @param keyword
	 * @return
	 * @throws RestServiceException
	 */
	public @ResponseBody List<RestPatientDTO> getPatients(@RequestParam(value="username", required=true) String username,
												   		  @RequestParam(value="password", required=true) String password,
												   		  @RequestParam(value="keyword", required=false, defaultValue="") String keyword) throws RestServiceException {
		
		User user = authenticationService.authenticate(username, password);
		if (user.getRole().equals(Role.DEFAULT)) {
			throw new RestServiceException("Invalid username or password!");
		}
		
		if (user.getRole().equals(Role.PATIENT)) {
			throw new RestServiceException("Patients are not allowed to access the patient list");
		}

		List<Patient> list;
		
		if (keyword.isEmpty()) {
			list = patientService.getPatients();
		}
		else {
			list = patientService.getPatients(keyword);
		}
		
		List<RestPatientDTO> results = new ArrayList<>(list.size());
		for(Patient patient : list) {
			results.add(new RestPatientDTO(patient));
		}
		
		return results;
	}
	
	@RequestMapping("/rest/listNotifications/")
	/**
	 * Returns a list of notifications for the user who's credentials are given
	 * 
	 * @param username
	 * @param password
	 * @return
	 * @throws RestServiceException
	 */
	public @ResponseBody List<RestNotificationDTO> listNotifications(@RequestParam(value="username", required=true) String username,
			   												  		 @RequestParam(value="password", required=true) String password) throws RestServiceException {
		User user = authenticationService.authenticate(username, password);
		if (user.getRole().equals(Role.DEFAULT)) {
			throw new RestServiceException("Invalid username or password!");
		}
		
		List<Notification> list = notificationService.getForUser(user);
		List<RestNotificationDTO> results = new ArrayList<>(list.size());

		for (Notification notification : list) {
			results.add(new RestNotificationDTO(notification));
		}
		
		return results;
	}
	
	@RequestMapping(value = "/rest/reserveOPSlot/", method = RequestMethod.POST)
	/**
	 * 
	 * @param username
	 * @param password
	 * @param patientId
	 * @param operationType
	 * @param from
	 * @param to
	 * @param distance
	 * @return
	 * @throws RestServiceException
	 */
	public @ResponseBody RestMessageDTO reserveOPSlot(@RequestParam(value="username", required=true) String username,
													  @RequestParam(value="password", required=true) String password,
													  @RequestParam(value="patientId", required=true) String patientId,
													  @RequestParam(value="operationType", required=true) String operationType,
													  @RequestParam(value="from", required=true) String from,
													  @RequestParam(value="to", required=true) String to,
													  @RequestParam(value="distance", defaultValue="12") Integer distance) throws RestServiceException {
		/* Only doctors can reserve slots */
		User user = authenticationService.authenticate(username, password);
		if (user.getRole().equals(Role.DEFAULT)) {
			throw new RestServiceException("Invalid username or password!");
		}
		if (!user.getRole().equals(Role.DOCTOR)) {
			throw new RestServiceException("Invalid username or password!");
		}
		
		/* Is the patient id valid ?*/
		if (patientService.getById(patientId) == null) {
			throw new RestServiceException("Invalid patient id!");
		}
		
		OPSlotDTO slot = new OPSlotDTO();
		slot.setDoctorID(((Doctor)user).getId());
		slot.setPatientID(patientId);
		slot.setDistance(distance);
		try {
			slot.setType(OperationType.valueOf(operationType));
		}
		catch(IllegalArgumentException e) {
			throw new RestServiceException("Invalid operation type"); 
		}
		
		/* Parse date values */
		SimpleDateFormat dateParser = new SimpleDateFormat("dd.MM.yyyy");
		try {
			slot.setFrom(dateParser.parse(from));
		} catch (ParseException e) {
			throw new RestServiceException("Could not parse from date .. probably wrong format.");
		}
		try {
			slot.setTo(dateParser.parse(to));
		} catch (ParseException e) {
			throw new RestServiceException("Could not parse to date .. probably wrong format.");
		}
		
		opSlotService.reserveOPSlot(slot);
		
		RestMessageDTO result = new RestMessageDTO();
		result.setMessage("Reservation request sent!");
		
		return result;
	}

	
	@RequestMapping(value = "/rest/deleteOPSlot/", method = RequestMethod.POST)
	/**
	 * Deletes a slot from the database
	 * 
	 * @param username
	 * @param password
	 * @param slotId
	 * @return
	 * @throws RestServiceException
	 */
	public @ResponseBody RestMessageDTO deleteOPSlot(@RequestParam(value="username", required=true) String username,
													 @RequestParam(value="password", required=true) String password,
													 @RequestParam(value="slotId", required=true) String slotId) throws RestServiceException {
		
		User user = authenticationService.authenticate(username, password);
		if (user.getRole().equals(Role.DEFAULT)) {
			throw new RestServiceException("Invalid username or password!");
		}
		
		/* Only hospitals can delete slots */
		if (!user.getRole().equals(Role.HOSPITAL)) {
			throw new RestServiceException("Only hospitals can delete slots!");
		}
		
		OPSlot slot = opSlotService.getById(slotId);

		/* Validate slot */
		if (slot == null) {
			throw new RestServiceException("Invalid slot id!");
		}
		if (slot.getHospital().getId() != null && !slot.getHospital().getId().equals(user.getId())) {
			throw new RestServiceException("This slot does not belong to you!");
		}
		if (slot.getStatus().equals(OperationStatus.reserved)) {
			throw new RestServiceException("This slot is already reserved, cancel first!");
		}
		
		opSlotService.deleteOPSlot(slot);
		
		RestMessageDTO result = new RestMessageDTO();
		result.setMessage("The slot has been deleted!");
		
		return result;
	}

	@RequestMapping(value = "/rest/cancelReservation/", method = RequestMethod.POST)
	/**
	 * Deletes a slot from the database
	 * 
	 * @param username
	 * @param password
	 * @param slotId
	 * @return
	 * @throws RestServiceException
	 */
	public @ResponseBody RestMessageDTO cancelReservation(@RequestParam(value="username", required=true) String username,
														  @RequestParam(value="password", required=true) String password,
														  @RequestParam(value="slotId", required=true) String slotId) throws RestServiceException {
		
		User user = authenticationService.authenticate(username, password);
		if (user.getRole().equals(Role.DEFAULT)) {
			throw new RestServiceException("Invalid username or password!");
		}
		
		/* Only doctors can cancel slots */
		if (!user.getRole().equals(Role.DOCTOR)) {
			throw new RestServiceException("Only doctors can cancel slots!");
		}
		
		OPSlot slot = opSlotService.getById(slotId);

		/* Validate slot */
		if (slot == null) {
			throw new RestServiceException("Invalid slot id!");
		}
		if (slot.getDoctor().getId() != null && !slot.getDoctor().getId().equals(user.getId())) {
			throw new RestServiceException("This slot does not belong to you!");
		}
		if (slot.getStatus().equals(OperationStatus.free)) {
			throw new RestServiceException("This slot is already free!");
		}
		
		opSlotService.cancelReservation(slot);
		
		RestMessageDTO result = new RestMessageDTO();
		result.setMessage("The slot has been canceled!");
		
		return result;
	}
}
