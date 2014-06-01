package service;

import model.dto.NotificationDTO;
import model.dto.OPSlotDTO;

public interface IQueueService {

	/**
	 * Sends the OPSlot data to the georesolver queue
	 * 
	 * @param slot
	 */
	public void sendToGeoResolver(OPSlotDTO slot);

	/**
	 * Sends the resulting opslot to the newsbeeper queue
	 * 
	 * @param slot
	 */
	public void sendToNewsBeeper(NotificationDTO notification);

	/**
	 * Registers an async listener which receives messages from the queue
	 * 
	 * @param queueName
	 * @param listener
	 */
	public void registerListener(String queueName, IQueueListener listener);
	
	/**
	 * Unregisters an async listener which receives messages from the queue
	 * 
	 * @param queueName
	 * @param listener
	 */
	public void unregisterListener(String queueName, IQueueListener listener);	
}
