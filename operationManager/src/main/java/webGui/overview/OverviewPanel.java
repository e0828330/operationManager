package webGui.overview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import model.OPSlot;
import model.OperationStatus;
import model.OperationType;
import model.Role;
import model.dto.OPSlotFilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.time.Duration;

import service.IOPSlotService;
import session.OperationManagerWebSession;

import com.googlecode.wicket.kendo.ui.form.datetime.DatePicker;
import com.googlecode.wicket.kendo.ui.form.datetime.TimePicker;
import com.googlecode.wicket.kendo.ui.panel.KendoFeedbackPanel;

public class OverviewPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	IOPSlotService opSlotService;
	
	private IModel<OPSlotFilter> filterModel;
	
	private DataTable<OPSlot, String> table;

	public OverviewPanel(String id) {
		super(id);
		
		filterModel = new CompoundPropertyModel<OPSlotFilter>(new OPSlotFilter());
	}

	@Override
	protected void onInitialize() {
		super.onInitialize();
		OperationManagerWebSession session = (OperationManagerWebSession) getSession();
		
		Form<OPSlotFilter> filterForm = new Form<OPSlotFilter>("filterForm", filterModel);
		
		final KendoFeedbackPanel feedback = new KendoFeedbackPanel("feedback");
		filterForm.add(feedback);

		filterForm.add(new DatePicker("date", Locale.GERMAN));
		filterForm.add(new TimePicker("from", Locale.GERMAN));
		filterForm.add(new TimePicker("to", Locale.GERMAN));
		filterForm.add(new TextField<String>("patient").setVisible(isPatientShown(session)));
		filterForm.add(new TextField<String>("hospital").setVisible(isHospitalShown(session)));
		filterForm.add(new TextField<String>("doctor").setVisible(isDoctorShown(session)));
		filterForm.add(new DropDownChoice<OperationType>("type", Arrays.asList(OperationType.values()),
				new EnumChoiceRenderer<OperationType>(OverviewPanel.this)));
		filterForm.add(new DropDownChoice<OperationStatus>("status", Arrays.asList(OperationStatus.values()),
				new EnumChoiceRenderer<OperationStatus>(OverviewPanel.this)).setVisible(isStatusShown(session)));
		
		filterForm.add(new Button("filterButton", new ResourceModel("filterButton")));
		
		add(filterForm);
		
		table = new DefaultDataTable<OPSlot, String>("overviewTable", getColumns(session), getDataProvider(), 10);
		table.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));

		add(table);
	}

	private List<IColumn<OPSlot, String>> getColumns(OperationManagerWebSession session) {
		List<IColumn<OPSlot, String>> columns = new ArrayList<IColumn<OPSlot, String>>();

		//Date
		columns.add(new OPSlotColumn() {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
					String componentId, IModel<OPSlot> rowModel) {
				OPSlot slot = rowModel.getObject();
				
				cellItem.add(new DateLabel(componentId, new Model<Date>(slot.getDate()), new PatternDateConverter("dd.MM.YYYY", false)));
			}

			@Override
			protected String getColumnPropertyName() {
				return "date";
			}
			
		});
		//From
		columns.add(new OPSlotColumn() {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
					String componentId, IModel<OPSlot> rowModel) {
				OPSlot slot = rowModel.getObject();
				
				cellItem.add(new DateLabel(componentId, new Model<Date>(slot.getFrom()), new PatternDateConverter("HH:mm", false)));
			}

			@Override
			protected String getColumnPropertyName() {
				return "from";
			}
			
		});
		//To
		columns.add(new OPSlotColumn() {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
					String componentId, IModel<OPSlot> rowModel) {
				OPSlot slot = rowModel.getObject();
				
				cellItem.add(new DateLabel(componentId, new Model<Date>(slot.getTo()), new PatternDateConverter("HH:mm", false)));
			}

			@Override
			protected String getColumnPropertyName() {
				return "to";
			}
			
		});
		//Type
		columns.add(new OPSlotColumn() {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
					String componentId, IModel<OPSlot> rowModel) {
				OPSlot slot = rowModel.getObject();
				
				cellItem.add(new EnumLabel<OperationType>(componentId, slot.getType()));
			}

			@Override
			protected String getColumnPropertyName() {
				return "type";
			}
			
		});
		//Hospital
		if (isHospitalShown(session)) {
			columns.add(new OPSlotColumn() {
				private static final long serialVersionUID = 1L;
	
				@Override
				public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
						String componentId, IModel<OPSlot> rowModel) {
					OPSlot slot = rowModel.getObject();
					
					cellItem.add(new Label(componentId, slot.getHospital() == null ? "-" : slot.getHospital().getName()));
				}
	
				@Override
				protected String getColumnPropertyName() {
					return "hospital";
				}
				
			});
		}
		//Doctor
		if (isDoctorShown(session)) {
			columns.add(new OPSlotColumn() {
				private static final long serialVersionUID = 1L;
	
				@Override
				public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
						String componentId, IModel<OPSlot> rowModel) {
					OPSlot slot = rowModel.getObject();
					
					cellItem.add(new Label(componentId, slot.getDoctor() == null ? "-" : slot.getDoctor().getLastName()));
				}
	
				@Override
				protected String getColumnPropertyName() {
					return "doctor";
				}
				
			});
		}
		//Patient
		if (isPatientShown(session)) {
			columns.add(new OPSlotColumn() {
				private static final long serialVersionUID = 1L;
	
				@Override
				public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
						String componentId, IModel<OPSlot> rowModel) {
					OPSlot slot = rowModel.getObject();
					
					cellItem.add(new Label(componentId, slot.getPatient() == null ? "-" : slot.getPatient().getFirstName() + " " + slot.getPatient().getLastName()));
				}
	
				@Override
				protected String getColumnPropertyName() {
					return "patient";
				}
				
			});
		}
		//Status
		if (isStatusShown(session)) {
			columns.add(new OPSlotColumn() {
				private static final long serialVersionUID = 1L;
	
				@Override
				public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
						String componentId, IModel<OPSlot> rowModel) {
					OPSlot slot = rowModel.getObject();
					
					cellItem.add(new EnumLabel<OperationStatus>(componentId, slot.getStatus()));
				}
	
				@Override
				protected String getColumnPropertyName() {
					return "status";
				}
			});
		}
		
		return columns;
	}
	
	private boolean isHospitalShown(OperationManagerWebSession session) {
		return session.getActiveRole() != Role.HOSPITAL;
	}
	
	private boolean isDoctorShown(OperationManagerWebSession session) {
		return session.getActiveRole() != Role.DOCTOR;
	}
	
	private boolean isPatientShown(OperationManagerWebSession session) {
		return session.getActiveRole() == Role.DOCTOR || session.getActiveRole() == Role.HOSPITAL;
	}
	
	private boolean isStatusShown(OperationManagerWebSession session) {
		return session.getActiveRole() == Role.DEFAULT;
	}
	
	private ISortableDataProvider<OPSlot, String> getDataProvider() {
		return new SortableDataProvider<OPSlot, String>() {
			private static final long serialVersionUID = 1L;
			OperationManagerWebSession session = (OperationManagerWebSession) getSession();

			@Override
			public Iterator<? extends OPSlot> iterator(long first, long count) {
				return opSlotService.getOPSlots(session.getActiveUser(), getSort(), filterModel.getObject(), table.getCurrentPage(), table.getItemsPerPage()).iterator();
			}

			@Override
			public IModel<OPSlot> model(OPSlot object) {
				return new Model<OPSlot>(object);
			}

			@Override
			public long size() {
				return opSlotService.getOPSlotCount(session.getActiveUser(), filterModel.getObject());
			}
			
		};
	}
	
	private abstract class OPSlotColumn implements IColumn<OPSlot, String>, Serializable {
		private static final long serialVersionUID = -4730014859884285134L;

		protected abstract String getColumnPropertyName();

		@Override
		public void detach() {
			
		}

		@Override
		public Component getHeader(String componentId) {
			return new Label(componentId, getString(getColumnPropertyName()));
		}

		@Override
		public String getSortProperty() {
			return getColumnPropertyName();
		}

		@Override
		public boolean isSortable() {
			return true;
		}
		
	}

}
