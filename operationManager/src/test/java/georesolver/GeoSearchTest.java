package georesolver;

import static org.junit.Assert.*;

import java.util.List;

import model.Hospital;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Distance;
import org.springframework.data.mongodb.core.geo.Metrics;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import repository.HospitalRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/application-config.xml" })
public class GeoSearchTest {
	@Autowired
	private HospitalRepository repo;

	@Autowired
	MongoTemplate template;

	private Hospital hospital;

	@Before
	public void setUp() {
		template.indexOps(Hospital.class).ensureIndex(new GeospatialIndex("position"));
		hospital = new Hospital();
		hospital.setName("SMZ Ost");
		hospital.setPosition(new Point(48.219011, 16.464193));
		repo.save(hospital);

	}

	@After
	public void tearDown() {
		repo.delete(hospital);
	}

	@Test
	public void findNearShouldHit() {
		// Wien - Mitte
		List<Hospital> result = repo.findByPositionNear(new Point(48.2065, 16.384821), new Distance(10, Metrics.KILOMETERS));
		assertEquals(result.get(0), hospital);
	}

	@Test
	public void findNearShouldNotHit() {
		// St. Poelten
		List<Hospital> result = repo.findByPositionNear(new Point(48.203609, 15.637509), new Distance(10, Metrics.KILOMETERS));
		assertFalse(result.contains(hospital));
	}
}
