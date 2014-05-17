package service;

import model.dto.Message;

public interface IQueueListener {
	/**
	 * Called when a new message arrives at the queue
	 * 
	 * @param slot
	 */
	public void handleMessage(Message m);
}
