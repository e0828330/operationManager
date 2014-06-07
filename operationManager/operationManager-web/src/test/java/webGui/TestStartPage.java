package webGui;

import model.OPSlot;

import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.markup.html.basic.EnumLabel;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.FormTester;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import service.IOPSlotService;

import com.googlecode.wicket.kendo.ui.form.datetime.DatePicker;
import com.googlecode.wicket.kendo.ui.form.datetime.TimePicker;

public class TestStartPage extends AbstractBaseTest {

	@Autowired
	IOPSlotService opSlotService;
	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8080);
		 
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/test");
        webapp.setWar("src/main/webapp");
        webapp.setOverrideDescriptor("src/main/webapp/WEB-INF/web-test.xml");
        server.setHandler(webapp);
 
		server.start();
        server.join();
	}
	
	/**
	 * asserts the necessary form components, fills the user name and a password, and submits the form
	 * @param user
	 */
	private void login(String user) {
		tester.assertComponent("loginForm:username", TextField.class);
		tester.assertComponent("loginForm:password", PasswordTextField.class);
		
		FormTester formTester = tester.newFormTester("loginForm");
		formTester.setValue("username", user);
		formTester.setValue("password", "a");
		formTester.submit();
	}
	
	@Test
	public void page_renders() {
		tester.startPage(StartPage.class);
		
		tester.assertRenderedPage(StartPage.class);
	}
	
	@Test
	public void table_renders() {
		page_renders();
		
		tester.assertComponent("overview:overviewTable", DataTable.class);
	}
	
	@Test
	public void all_components_render_role_default() {
		page_renders();
		
		tester.assertComponent("overview:filterForm", Form.class);
		
		tester.assertComponent("overview:filterForm:date", DatePicker.class);
		tester.assertComponent("overview:filterForm:from", TimePicker.class);
		tester.assertComponent("overview:filterForm:to", TimePicker.class);
		tester.assertInvisible("overview:filterForm:patient");
		tester.assertComponent("overview:filterForm:hospital", TextField.class);
		tester.assertComponent("overview:filterForm:doctor", TextField.class);
		tester.assertComponent("overview:filterForm:type", DropDownChoice.class);
		tester.assertComponent("overview:filterForm:status", DropDownChoice.class);
		
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:1:cell", DateLabel.class); //Date
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:2:cell", DateLabel.class); //From
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:3:cell", DateLabel.class); //To
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:4:cell", EnumLabel.class); //Type
		tester.assertLabel("overview:overviewTable:body:rows:1:cells:5:cell", "SMZ"); //Hospital
		tester.assertLabel("overview:overviewTable:body:rows:1:cells:6:cell", "Dr. Augfehler"); //Doctor
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:7:cell", EnumLabel.class); //Status
	}
	
	@Test
	public void all_components_render_role_patient() {
		page_renders();
		login("patient");
		
		tester.assertComponent("overview:filterForm", Form.class);
		
		tester.assertComponent("overview:filterForm:date", DatePicker.class);
		tester.assertComponent("overview:filterForm:from", TimePicker.class);
		tester.assertComponent("overview:filterForm:to", TimePicker.class);
		tester.assertInvisible("overview:filterForm:patient");
		tester.assertComponent("overview:filterForm:hospital", TextField.class);
		tester.assertComponent("overview:filterForm:doctor", TextField.class);
		tester.assertComponent("overview:filterForm:type", DropDownChoice.class);
		tester.assertInvisible("overview:filterForm:status");
		
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:1:cell", DateLabel.class); //Date
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:2:cell", DateLabel.class); //From
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:3:cell", DateLabel.class); //To
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:4:cell", EnumLabel.class); //Type
		tester.assertLabel("overview:overviewTable:body:rows:1:cells:5:cell", "SMZ"); //Hospital
		tester.assertLabel("overview:overviewTable:body:rows:1:cells:6:cell", "Dr. Augfehler"); //Doctor
	}
	
	@Test
	public void all_components_render_role_hospital() {
		page_renders();
		login("hospital");
		
		tester.assertComponent("overview:filterForm", Form.class);
		
		tester.assertComponent("overview:filterForm:date", DatePicker.class);
		tester.assertComponent("overview:filterForm:from", TimePicker.class);
		tester.assertComponent("overview:filterForm:to", TimePicker.class);
		tester.assertComponent("overview:filterForm:patient", TextField.class);
		tester.assertInvisible("overview:filterForm:hospital");
		tester.assertComponent("overview:filterForm:doctor", TextField.class);
		tester.assertComponent("overview:filterForm:type", DropDownChoice.class);
		tester.assertInvisible("overview:filterForm:status");
		
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:1:cell", DateLabel.class); //Date
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:2:cell", DateLabel.class); //From
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:3:cell", DateLabel.class); //To
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:4:cell", EnumLabel.class); //Type
		tester.assertLabel("overview:overviewTable:body:rows:1:cells:5:cell", "Dr. Augfehler"); //Doctor
		tester.assertLabel("overview:overviewTable:body:rows:1:cells:6:cell", "Adelheid Abesser"); //Patient
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:7:cell", Link.class); //remove link
	}
	
	@Test
	public void all_components_render_role_doctor() {
		page_renders();
		login("doctor");
		
		tester.assertComponent("overview:filterForm", Form.class);
		
		tester.assertComponent("overview:filterForm:date", DatePicker.class);
		tester.assertComponent("overview:filterForm:from", TimePicker.class);
		tester.assertComponent("overview:filterForm:to", TimePicker.class);
		tester.assertComponent("overview:filterForm:patient", TextField.class);
		tester.assertComponent("overview:filterForm:hospital", TextField.class);
		tester.assertInvisible("overview:filterForm:doctor");
		tester.assertComponent("overview:filterForm:type", DropDownChoice.class);
		tester.assertInvisible("overview:filterForm:status");
		
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:1:cell", DateLabel.class); //Date
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:2:cell", DateLabel.class); //From
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:3:cell", DateLabel.class); //To
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:4:cell", EnumLabel.class); //Type
		tester.assertLabel("overview:overviewTable:body:rows:1:cells:5:cell", "SMZ"); //Hospital
		tester.assertLabel("overview:overviewTable:body:rows:1:cells:6:cell", "Adelheid Abesser"); //Patient
		tester.assertComponent("overview:overviewTable:body:rows:1:cells:7:cell", Link.class); //remove link

		tester.assertComponent("overview:opSlotButton", Link.class);
	}
	
	@Test
	public void test_reservation_button() {
		page_renders();
		login("doctor");
		
		tester.clickLink("overview:opSlotButton");
		
		tester.assertRenderedPage(ReservationPage.class);
	}
	
	@Test
	public void test_cancel_reservation_link() {
		page_renders();
		login("doctor");
		
		tester.clickLink("overview:overviewTable:body:rows:1:cells:7:cell");
		
		Mockito.verify(opSlotService).cancelReservation(Mockito.any(OPSlot.class));
	}
	
	@Test
	public void test_create_button() {
		page_renders();
		login("hospital");
		
		tester.clickLink("overview:opSlotButton");
		
		tester.assertRenderedPage(CreateOPSlotPage.class);
	}
	
	@Test
	public void test_delete_slot_link() {
		page_renders();
		login("hospital");
		
		tester.clickLink("overview:overviewTable:body:rows:1:cells:7:cell");
		
		Mockito.verify(opSlotService).deleteOPSlot(Mockito.any(OPSlot.class));
	}
}
