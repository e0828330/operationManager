package service.real;

import model.dto.Message;
import service.IQueueListener;

public class GeoResolverListener implements IQueueListener {

	@Override
	public void handleMessage(Message m) {
		System.out.println("GEORESOLVER LIStener");
	}

}

