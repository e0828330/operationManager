package webGui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.Spring;

import model.OPSlot;

import org.apache.wicket.spring.SpringWebApplicationFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.asm.SpringAsmInfo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import service.IOPSlotService;
import service.real.OPSlotService;

public class TestSimplePage {
	
	public static void main(String[] args) throws Exception {
		
		Server server = new Server(8080);
		 
        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/test");
        webapp.setWar("src/main/webtest");
        server.setHandler(webapp);
 
		server.start();
        server.join();
	}

	private static IOPSlotService mockOPSlotService() {
		IOPSlotService opSlotService = Mockito.mock(OPSlotService.class);
		Mockito.when(opSlotService.getOPSlots(Mockito.anyLong(), Mockito.anyLong())).thenAnswer(new Answer<List<OPSlot>>() {

			@Override
			public List<OPSlot> answer(InvocationOnMock invocation)
					throws Throwable {
				List<OPSlot> opSlots = new ArrayList<OPSlot>();
				
				OPSlot slot1 = new OPSlot();
				slot1.setDate(new Date());
				
				return opSlots;
			}
			
		});
		
		Mockito.when(opSlotService.getOPSlotCount()).thenReturn(1L);
		
		return opSlotService;
	}

}
