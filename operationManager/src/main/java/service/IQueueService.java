package service;

import model.Notification;
import model.OPSlot;

public interface IQueueService {

	/**
	 * Sends the OPSlot data to the georesolver queue
	 * 
	 * @param slot
	 */
	public void sendToGeoResolver(OPSlot slot);

	/**
	 * Sends the resulting opslot to the newsbeeper queue
	 * 
	 * @param slot
	 */
	public void sendToNewsBeeper(Notification notification);

	/**
	 * Registers an async listener which receives messages from the queue
	 * 
	 * @param queueName
	 * @param listener
	 */
	public void registerListener(String queueName, IQueueListener listener);
}
