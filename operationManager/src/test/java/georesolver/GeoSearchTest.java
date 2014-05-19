package georesolver;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import model.OperationType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.Distance;
import org.springframework.data.mongodb.core.geo.Metrics;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import repository.OPSlotRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-config.xml" })
public class GeoSearchTest {

	@Autowired
	private OPSlotRepository repo;

	@Test
	public void findNearShouldHit() {
		// Wien - Mitte -  (48.2065, 16.384821)
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, 1);
		assertNotNull(repo.findBestInRange(new Point(48.2065, 16.384821), new Distance(10, Metrics.KILOMETERS), OperationType.cardio, new Date(), cal.getTime()));
	}

	@Test
	public void findNearShouldNotHit() {
		// Rom -- (41.865586, 12.476183)
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, 1);
		assertNull(repo.findBestInRange(new Point(41.865586, 12.476183), new Distance(10, Metrics.KILOMETERS), OperationType.cardio, new Date(), cal.getTime()));
	}
}
