package service.real;

import java.util.Date;

import model.Doctor;
import model.Hospital;
import model.Notification;
import model.OPSlot;
import model.Patient;
import model.User;
import model.dto.NotificationDTO;
import newsbeeper.Main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repository.DoctorRepository;
import repository.HospitalRepository;
import service.INewsbeeperService;
import service.IOPSlotService;
import service.IPatientService;

@Service
public class NewsbeeperService implements INewsbeeperService {

	private Logger logger = LoggerFactory.getLogger(NewsbeeperService.class);	
	
	@Autowired
	private DoctorRepository repoD;
	
	@Autowired
	private HospitalRepository repoH;
	
	@Autowired
	private IPatientService patientService;

	@Autowired
	private IOPSlotService opSlotService;	
	
	@Override
	public Doctor getDoctorById(String id) {
		return repoD.findOne(id);
	}

	@Override
	public Hospital getHospitalById(String id) {
		return repoH.findOne(id);
	}

	@Override
	public Notification handleNotification(NotificationDTO notificationDTO) {
		if (notificationDTO == null) return null;
		if (notificationDTO.getRecipientID() == null) {
			return null;
		}
		try {
			Notification notification = new Notification();
			notification.setMessage(notificationDTO.getMessage());
			notification.setType(notificationDTO.getType());

			logger.info("Got request: " + notificationDTO);

			Patient patient = patientService.getById(notificationDTO.getRecipientID());
			OPSlot slot = notificationDTO.getOpSlotID() == null ? null : opSlotService.getById(notificationDTO.getOpSlotID());

			if (patient != null) {
				notification.setRecipient((User) patient);
			} else {
				Doctor doctor = getDoctorById(notificationDTO.getRecipientID());
				if (doctor != null) {
					notification.setRecipient((User) doctor);
				} else {
					Hospital hospital = getHospitalById(notificationDTO.getRecipientID());
					if (hospital != null) {
						notification.setRecipient((User) hospital);
					} else {
						logger.info("No User found with id = " + notificationDTO.getRecipientID());
						return null;
					}
				}
			}

			if (slot == null) {
				logger.info("No slot found.");
			}

			notification.setSlot(slot);
			notification.setTimestamp(new Date());

			return notification;
		} catch (Exception e) {
			logger.error("Failed to handle message", e);

		}	
		return null;
	}

}
