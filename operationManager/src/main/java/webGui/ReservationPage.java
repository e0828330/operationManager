package webGui;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import model.Doctor;
import model.OperationType;
import model.Patient;
import model.Role;
import model.dto.OPSlotDTO;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.IOPSlotService;
import service.IPatientService;
import session.OperationManagerWebSession;

import com.googlecode.wicket.kendo.ui.form.datetime.DatePicker;

public class ReservationPage extends IndexPage {
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	IPatientService patientService;
	
	@SpringBean
	IOPSlotService opSlotService;

	public ReservationPage(PageParameters parameters) {
		super(parameters);
		
		OperationManagerWebSession session = (OperationManagerWebSession) WebSession.get();
		
		//Only hospital users may create op slots
		if (!session.getActiveUser().getRole().equals(Role.DOCTOR)) {
			throw new RestartResponseException(StartPage.class);
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		OperationManagerWebSession session = (OperationManagerWebSession) WebSession.get();
		Doctor doctor = (Doctor) session.getActiveUser();
		
		OPSlotDTO slot = new OPSlotDTO();
		
		//set fixed values
		slot.setDoctorID(doctor.getId());
		slot.setDistance(12);
		
		final Model<OPSlotDTO> model = new Model<OPSlotDTO>(slot);
		LoadableDetachableModel<List<Patient>> patientsModel = new LoadableDetachableModel<List<Patient>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Patient> load() {
				return patientService.getPatients();
			}
			
		};
		//The dropdownchoice works with Patient objects, but in the dto we only save the string, so we need a special model for that.
		IModel<Patient> patientModel = new IModel<Patient>() {
			private static final long serialVersionUID = 1L;

			@Override
			public void detach() {
				
			}

			@Override
			public Patient getObject() {
				return patientService.getById(model.getObject().getPatientID());
			}

			@Override
			public void setObject(Patient object) {
				model.getObject().setPatientID(object.getId());
			}
			
		};
		
		add(new FeedbackPanel("feedback"));
		
		Form<OPSlotDTO> form = new Form<OPSlotDTO>("form", new CompoundPropertyModel<OPSlotDTO>(model)) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				opSlotService.reserveOPSlot(model.getObject());
				
				setResponsePage(StartPage.class);
			}
		};
		
		final DatePicker from = new DatePicker("from", Locale.GERMAN);
		final DatePicker to = new DatePicker("to", Locale.GERMAN);

		form.add(new DropDownChoice<Patient>("patient", patientModel, patientsModel, new IChoiceRenderer<Patient>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Patient object) {
				return object.getFirstName() + " " + object.getLastName();
			}

			@Override
			public String getIdValue(Patient object, int index) {
				return object.getId();
			}
		}).setRequired(true));
		form.add(new DropDownChoice<OperationType>("type", Arrays.asList(OperationType.values()),
				new EnumChoiceRenderer<OperationType>(ReservationPage.this)).setRequired(true));
		form.add(from.setRequired(true));
		form.add(to.setRequired(true));
		form.add(new TextField<Integer>("distance"));
		form.add(new SubmitLink("save"));
		
		form.add(new AbstractFormValidator() {
			private static final long serialVersionUID = 1L;

			@Override
			public FormComponent<?>[] getDependentFormComponents() {
				return new FormComponent<?>[] { from, to };
			}

			@Override
			public void validate(Form<?> form) {
				if (to.getConvertedInput().before(from.getConvertedInput())) {
					form.error(getString("timeValidationError"));
				}
				if (from.getConvertedInput().before(new Date()) || to.getConvertedInput().before(new Date())) {
					form.error(getString("pastTimeValidationError"));
				}
			}
			
		});
		
		add(form);
	}

}
