package service;

import model.OPSlot;

public interface IQueueListener {
	/**
	 * Called when a new message arrives at the queue
	 * 
	 * @param slot
	 */
	public void onMessage(OPSlot slot);
}
