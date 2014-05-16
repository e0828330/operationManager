package service;

import model.dto.MessageDTO;

public interface IQueueListener {
	/**
	 * Called when a new message arrives at the queue
	 * 
	 * @param slot
	 */
	public void onMessage(MessageDTO slot);
}
