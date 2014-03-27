package webGui.overview;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import model.OPSlot;
import model.OperationStatus;
import model.OperationType;

import org.apache.wicket.Component;
import org.apache.wicket.datetime.PatternDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.ISortableDataProvider;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.mockito.InjectMocks;

import service.IOPSlotService;
import service.real.OPSlotService;

public class OverviewPanel extends Panel {
	private static final long serialVersionUID = 1L;
	
	@SpringBean
	IOPSlotService opSlotService;

	public OverviewPanel(String id) {
		super(id);
	}
	
	@Override
	protected void onInitialize() {
		super.onInitialize();
		
		DataTable<OPSlot, String> table = new DefaultDataTable<OPSlot, String>("overviewTable", getColumns(), getDataProvider(), 10);

		add(table);
	}

	private List<IColumn<OPSlot, String>> getColumns() {
		List<IColumn<OPSlot, String>> columns = new ArrayList<IColumn<OPSlot, String>>();
		
		columns.add(new OPSlotColumn() {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
					String componentId, IModel<OPSlot> rowModel) {
				OPSlot slot = rowModel.getObject();
				
				cellItem.add(new DateLabel(componentId, new Model<Date>(slot.getDate()), new PatternDateConverter("dd.MM.YY", false)));
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
				
				cellItem.add(new EnumLabel<OperationType>(componentId, slot.getType()));
			}

			@Override
			protected String getColumnPropertyName() {
				return "type";
			}
			
		});
		columns.add(new OPSlotColumn() {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
					String componentId, IModel<OPSlot> rowModel) {
				OPSlot slot = rowModel.getObject();
				
				cellItem.add(new Label(componentId, slot.getHospital().getName()));
			}

			@Override
			protected String getColumnPropertyName() {
				return "hospital";
			}
			
		});
		columns.add(new OPSlotColumn() {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<OPSlot>> cellItem,
					String componentId, IModel<OPSlot> rowModel) {
				OPSlot slot = rowModel.getObject();
				
				cellItem.add(new Label(componentId, slot.getDoctor().getName()));
			}

			@Override
			protected String getColumnPropertyName() {
				return "doctor";
			}
			
		});
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
		
		return columns;
	}
	
	private ISortableDataProvider<OPSlot, String> getDataProvider() {
		return new SortableDataProvider<OPSlot, String>() {
			private static final long serialVersionUID = 1L;

			@Override
			public Iterator<? extends OPSlot> iterator(long first, long count) {
				return opSlotService.getOPSlots(getSort(), first, count).iterator();
			}

			@Override
			public IModel<OPSlot> model(OPSlot object) {
				return new Model<OPSlot>(object);
			}

			@Override
			public long size() {
				return opSlotService.getOPSlotCount();
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
