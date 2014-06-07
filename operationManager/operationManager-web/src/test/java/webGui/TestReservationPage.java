package webGui;

import model.dto.OPSlotDTO;

import org.apache.wicket.Session;
import org.apache.wicket.util.tester.FormTester;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import service.IOPSlotService;
import session.OperationManagerWebSession;

public class TestReservationPage extends AbstractBaseTest {
	
	@Autowired
	IOPSlotService opSlotService;
	
	@Test
	public void test_access_right() {
		tester.startPage(ReservationPage.class);
		//default user is not allowed to view this page, so there should be a redirect to the start page
		tester.assertRenderedPage(StartPage.class);
	}
	
	@Test
	public void test_save_button() {
		OperationManagerWebSession session = (OperationManagerWebSession)Session.get();
		session.authenticate("doctor", "a");
		
		tester.startPage(ReservationPage.class);
		tester.assertRenderedPage(ReservationPage.class);
		
		FormTester formTester = tester.newFormTester("form");
		formTester.select("patient", 0);
		formTester.select("type", 0);
		formTester.setValue("from", DateTimeFormat.forPattern("dd.MM.YY").print(new DateTime()));
		formTester.setValue("to", DateTimeFormat.forPattern("dd.MM.YY").print(new DateTime().plusHours(1)));
		formTester.setValue("distance", "10");
		
		formTester.submit();
		
		Mockito.verify(opSlotService).reserveOPSlot(Mockito.any(OPSlotDTO.class));
		
		tester.assertRenderedPage(StartPage.class);
	}
}
