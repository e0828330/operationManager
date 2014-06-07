package webGui;

import model.OPSlot;

import org.apache.wicket.Session;
import org.apache.wicket.util.tester.FormTester;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import service.IOPSlotService;
import session.OperationManagerWebSession;

public class TestCreateOPSlotPage extends AbstractBaseTest {
	
	@Autowired
	IOPSlotService opSlotService;
	
	@Test
	public void test_access_right() {
		tester.startPage(CreateOPSlotPage.class);
		//default user is not allowed to view this page, so there should be a redirect to the start page
		tester.assertRenderedPage(StartPage.class);
	}
	
	@Test
	public void test_save_button() {
		OperationManagerWebSession session = (OperationManagerWebSession)Session.get();
		session.authenticate("hospital", "a");
		
		tester.startPage(CreateOPSlotPage.class);
		tester.assertRenderedPage(CreateOPSlotPage.class);
		
		FormTester formTester = tester.newFormTester("form");
		formTester.select("type", 0);
		formTester.setValue("date", DateTimeFormat.forPattern("dd.MM.YY").print(new DateTime().plusDays(1)));
		formTester.setValue("from", DateTimeFormat.forPattern("HH:mm").print(new DateTime()));
		formTester.setValue("to", DateTimeFormat.forPattern("HH:mm").print(new DateTime().plusHours(1)));
		
		formTester.submit();
		
		Mockito.verify(opSlotService).saveOPSlot(Mockito.any(OPSlot.class));
		
		tester.assertRenderedPage(StartPage.class);
	}
}
