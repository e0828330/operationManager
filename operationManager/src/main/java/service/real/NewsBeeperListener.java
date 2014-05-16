package service.real;

import model.dto.Message;
import service.IQueueListener;

public class NewsBeeperListener implements IQueueListener {

	@Override
	public void handleMessage(Message m) {
		System.out.println("received notificaton");		
	}

}
