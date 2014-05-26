package webGui;

import java.util.Locale;

import model.OPSlot;
import model.Role;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.validation.AbstractFormValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import service.IOPSlotService;
import session.OperationManagerWebSession;

import com.googlecode.wicket.kendo.ui.form.datetime.DatePicker;
import com.googlecode.wicket.kendo.ui.form.datetime.TimePicker;

public class CreateOPSlotPage extends IndexPage {
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	IOPSlotService opSlotService;
	
	public CreateOPSlotPage(PageParameters parameters) {
		super(parameters);
		
		OperationManagerWebSession session = (OperationManagerWebSession) WebSession.get();
		
		//Only hospital users may create op slots
		if (!session.getActiveUser().getRole().equals(Role.HOSPITAL)) {
			throw new RestartResponseException(StartPage.class);
		}
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		LoadableDetachableModel<OPSlot> model = new LoadableDetachableModel<OPSlot>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected OPSlot load() {
				OPSlot slot = new OPSlot();
				
				return slot;
			}
			
		};
		
		add(new FeedbackPanel("feedback"));
		
		Form<OPSlot> form = new Form<OPSlot>("form", new CompoundPropertyModel<OPSlot>(model)) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onSubmit() {
				opSlotService.saveOPSlot(getModelObject());
				
				setResponsePage(StartPage.class);
			}
		};
		
		final TimePicker from = new TimePicker("from", Locale.GERMAN);
		final TimePicker to = new TimePicker("to", Locale.GERMAN);

		form.add(new DatePicker("date", Locale.GERMAN).setRequired(true));
		form.add(from.setRequired(true));
		form.add(to.setRequired(true));
		form.add(new SubmitLink("save"));
		
		form.add(new AbstractFormValidator() {

			@Override
			public FormComponent<?>[] getDependentFormComponents() {
				return new FormComponent<?>[] { from, to };
			}

			@Override
			public void validate(Form<?> form) {
				if (!from.getConvertedInput().before(to.getConvertedInput())) {
					form.error(getString("timeValidationError"));
				}
			}
			
		});
		
		add(form);
	}

}
