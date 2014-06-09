package rest;

import static com.jayway.restassured.RestAssured.given;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import model.OperationType;
import model.dto.RestPatientDTO;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class LoadTest {

	private final int numberOfRequests = 1024;

	/**
	 * Should point to web instance in the format
	 * http://host:PORT/path
	 * when no host / port are given it defaults to localhost:8080
	 */
	//private final String baseURL = "operationManager-web";
	private final String baseURL = "http://operationmanager-web.cfapps.io:80";
	
	private List<RestPatientDTO> getPatients() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();  
		String json = given().param("username", "maria").param("password", "test09").get(baseURL + "/rest/getPatients/").asString();
		List<RestPatientDTO> result = mapper.readValue(json, new TypeReference<List<RestPatientDTO>>() {});
		return result;
	}
	

	private class ReservationWorker implements Runnable {

		private String patientId;
		private String operationType;
		
		public ReservationWorker(String patientId, String operationType) {
			this.patientId = patientId;
			this.operationType = operationType;
		}
		
		@Override
		/***
		 * Worker thread, sends the request 
		 */
		public void run() {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
			given().param("username", "maria").
				param("password", "test09").
				param("patientId", patientId).
				param("operationType", operationType).
				param("from", dateFormat.format(new Date())).
				param("to", "01.01.2016").
				param("distance", "150").
		    post(baseURL + "/rest/reserveOPSlot/");
		}
	}
	
	@Test
	/**
	 * Flood rest service with reservation requests
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void loadTest() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		ExecutorService executor = Executors.newCachedThreadPool();
		List<RestPatientDTO> patients = getPatients();
		for (int i = 0; i < numberOfRequests; i++) {
			RestPatientDTO p = patients.get(i % patients.size());
			String type = OperationType.values()[i % OperationType.values().length].toString();
			executor.execute(new ReservationWorker(p.getId(), type));
		}
		executor.shutdown();
		executor.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);
	}
}
