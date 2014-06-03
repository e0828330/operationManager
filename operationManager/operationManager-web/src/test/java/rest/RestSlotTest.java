package rest;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.List;

import model.OPSlot;
import model.dto.RestErrorDTO;
import model.dto.RestOPSlotDTO;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import repository.OPSlotRepository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-config.xml" })
public class RestSlotTest {
	
	@Autowired
	private OPSlotRepository slotRepo;
	
	@Test
	/**
	 * Tests  whether the rest service returns no patients when called without a user
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void test_getSlots_shouldContainNoPatient() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();  
		String json = get("operationManager-web/rest/getSlots/").asString();
		List<RestOPSlotDTO> result = mapper.readValue(json, new TypeReference<List<RestOPSlotDTO>>() {});
		
		for(RestOPSlotDTO dto : result) {
			assertNull(dto.getPatient());
		}
		
	}
	
	@Test
	/**
	 * Tests  whether the rest service returns all slots when called without a user
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void test_getSlots_shouldFindAll() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();  
		String json = get("operationManager-web/rest/getSlots/").asString();
		List<RestOPSlotDTO> result = mapper.readValue(json, new TypeReference<List<RestOPSlotDTO>>() {});
		List<OPSlot> dbSlots = (List<OPSlot>) slotRepo.findAll();
		
		assertEquals(dbSlots.size(), result.size());
	}

	@Test
	/**
	 * Tests whether the service rejects wrong login credentials
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void test_getSlots_wrongUser() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();  
		String json = given().param("username", "wrong").param("password", "wrong").get("operationManager-web/rest/getSlots/").asString();
		RestErrorDTO dto = mapper.readValue(json, RestErrorDTO.class);
		assertEquals("Invalid username or password!", dto.getError());
	}
	
	@Test(expected=JsonMappingException.class)
	/**
	 * Tests whether the service does not reject correct login credentials
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void test_getSlots_correctUser() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();  
		String json = given().param("username", "abesser").param("password", "test01").get("operationManager-web/rest/getSlots/").asString();
		mapper.readValue(json, RestErrorDTO.class);
	}

	@Test
	/**
	 * Tests the addSlot request, should successfully add a slot
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void test_addSlot_shouldSucceed() throws JsonParseException, JsonMappingException, IOException  {
		ObjectMapper mapper = new ObjectMapper();  
		String json = given().param("username", "lkhbaden").
					  param("password", "test12").
					  param("date", "01.01.2015").
					  param("from", "08:00").
					  param("to", "12:00").
					  post("operationManager-web/rest/addSlot/").asString();

		RestOPSlotDTO slot = mapper.readValue(json, RestOPSlotDTO.class);
		assertNotNull(slot);
		assertNotNull(slot.getId());
		slotRepo.delete(slot.getId());
	}

	@Test
	/**
	 * Tests the addSlot request, should fail because of wrong login
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void test_addSlot_shouldFail() throws JsonParseException, JsonMappingException, IOException  {
		ObjectMapper mapper = new ObjectMapper();  
		String json = given().param("username", "lkhbaden").
					  param("password", "wrong").
					  param("date", "01.01.2015").
					  param("from", "08:00").
					  param("to", "12:00").
					  post("operationManager-web/rest/addSlot/").asString();

		RestErrorDTO dto = mapper.readValue(json, RestErrorDTO.class);
		assertEquals("Invalid username or password!", dto.getError());
	}

	@Test
	/**
	 * Tests the addSlot request, should fail because of of insufficient permission
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void test_addSlot_shouldFail_permission() throws JsonParseException, JsonMappingException, IOException  {
		ObjectMapper mapper = new ObjectMapper();  
		String json = given().param("username", "franz").
					  param("password", "test04").
					  param("date", "01.01.2015").
					  param("from", "08:00").
					  param("to", "12:00").
					  post("operationManager-web/rest/addSlot/").asString();

		RestErrorDTO dto = mapper.readValue(json, RestErrorDTO.class);
		assertEquals("Only hospitals can add slots.", dto.getError());
	}
}
	