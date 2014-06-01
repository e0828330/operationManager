package rest;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import model.OPSlot;
import model.OperationStatus;
import model.dto.RestErrorDTO;

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
public class RestGetSlotTest {
	
	@Autowired
	private OPSlotRepository slotRepo;
	
	@Test
	/**
	 * Tests  whether the rest service returns all free slots when called without a user
	 * 
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public void test_getSlots_shouldFindAllFree() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();  
		String json = get("operationManager/rest/getSlots/").asString();
		List<OPSlot> result = mapper.readValue(json, new TypeReference<List<OPSlot>>() {});
		
		int numFree = 0;
		List<OPSlot> dbSlots = (List<OPSlot>) slotRepo.findAll();
		for (OPSlot slot : dbSlots) {
			if (slot.getStatus().equals(OperationStatus.free)) {
				numFree++;
			}
		}

		assertEquals(numFree, result.size());
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
		String json = given().param("username", "wrong").param("password", "wrong").get("operationManager/rest/getSlots/").asString();
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
		String json = given().param("username", "abesser").param("password", "test01").get("operationManager/rest/getSlots/").asString();
		mapper.readValue(json, RestErrorDTO.class);
	}
}
	