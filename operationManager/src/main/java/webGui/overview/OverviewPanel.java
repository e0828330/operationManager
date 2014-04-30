package webGui.overview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.OPSlot;
import model.OperationStatus;
import model.OperationType;
import model.Role;
import model.dto.OPSlotFilter;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
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

public class OverviewPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	IOPSlotService opSlotService;
	
	private IModel<OPSlotFilter> filterModel;

	public OverviewPanel(String id) {
		super(id);
		
		filterModel = new CompoundPropertyModel<OPSlotFilter>(new OPSlotFilter());
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		Form<OPSlotFilter> filterForm = new Form<OPSlotFilter>("filterForm", filterModel);
		
		filterForm.add(new DateTextField("date", "dd.MM.YYYY"));
		filterForm.add(new DateTextField("from", "HH:mm"));
		filterForm.add(new DateTextField("to", "HH:mm"));
		filterForm.add(new TextField<String>("patient"));
		filterForm.add(new TextField<String>("hospital"));
		filterForm.add(new TextField<String>("doctor"));
		filterForm.add(new DropDownChoice<OperationType>("type", Arrays.asList(OperationType.values()),
				new EnumChoiceRenderer<OperationType>(OverviewPanel.this)));
		filterForm.add(new DropDownChoice<OperationStatus>("status", Arrays.asList(OperationStatus.values()),
				new EnumChoiceRenderer<OperationStatus>(OverviewPanel.this)));
		
		filterForm.add(new Button("filterButton", new ResourceModel("filterButton")));
		
		add(filterForm);
		
		DataTable<OPSlot, String> table = new DefaultDataTable<OPSlot, String>("overviewTable", getColumns(), getDataProvider(), 10);
		table.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));

		add(table);
	}

	private List<IColumn<OPSlot, String>> getColumns() {
		List<IColumn<OPSlot, String>> columns = new ArrayList<IColumn<OPSlot, String>>();
		OperationManagerWebSession session = (OperationManagerWebSession) getSession();

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
		columns.add(new OPSlotColumn() {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
					String componentId, IModel<OPSlot> rowModel) {
				OPSlot slot = rowModel.getObject();
				
				cellItem.add(new DateLabel(componentId, new Model<Date>(slot.getDate()), new PatternDateConverter("HH:mm", false)));
			}

			@Override
			protected String getColumnPropertyName() {
				return "from";
			}
			
		});
		columns.add(new OPSlotColumn() {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
					String componentId, IModel<OPSlot> rowModel) {
				OPSlot slot = rowModel.getObject();
				
				cellItem.add(new DateLabel(componentId, new Model<Date>(slot.getDate()), new PatternDateConverter("HH:mm", false)));
			}

			@Override
			protected String getColumnPropertyName() {
				return "to";
			}
			
		});
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
		if (session.getActiveRole() != Role.HOSPITAL) {
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
		if (session.getActiveRole() != Role.DOCTOR) {
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
		if (session.getActiveRole() == Role.DOCTOR || session.getActiveRole() == Role.HOSPITAL) {
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
		if (session.getActiveRole() == Role.DEFAULT) {
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
	
	private ISortableDataProvider<OPSlot, String> getDataProvider() {
		return new SortableDataProvider<OPSlot, String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Iterator<? extends OPSlot> iterator(long first, long count) {
				return opSlotService.getOPSlots(getSort(), filterModel.getObject(), first, count).iterator();
			}

			@Override
			public IModel<OPSlot> model(OPSlot object) {
				return new Model<OPSlot>(object);
			}

			@Override
			public long size() {
				return opSlotService.getOPSlotCount(filterModel.getObject());
			}
			
		};
	}
	
	private abstract class OPSlotColumn implements IColumn<OPSlot, String>, Serializable {

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
