package repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import model.OPSlot;
import model.OperationStatus;
import model.OperationType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoResult;
import org.springframework.data.mongodb.core.geo.GeoResults;
import org.springframework.data.mongodb.core.geo.Point;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

import repository.OPSlotRepositoryCustom;

public class OPSlotRepositoryImpl implements OPSlotRepositoryCustom {

	@Autowired
	private MongoTemplate template;

	@Override
	public OPSlot findBestInRange(Point point, Distance distance, OperationType type, Date from, Date to) {
		/* Filter date, type and status */
		Query query = new Query();
		Criteria queryCriteria = new Criteria().andOperator(Criteria.where("type").is(OperationType.values()[type.ordinal()]),
															Criteria.where("status").is(OperationStatus.values()[OperationStatus.free.ordinal()]),
															Criteria.where("date").gte(from).lt(to));
		query.addCriteria(queryCriteria);
		

		/* Do the geo query */
		NearQuery nearQuery = NearQuery.near(point).spherical(true).maxDistance(distance).query(query);
		GeoResults<OPSlot> rawResults = ((MongoOperations) template).geoNear(nearQuery, OPSlot.class);

		/* No results -> return null */
		if (rawResults.getContent().size() == 0) {
			return null;
		}
		
		/* Sort results by date if they have the same distance */
		List<GeoResult<OPSlot>> results = new ArrayList<>(rawResults.getContent());
		Collections.sort(results, new Comparator<GeoResult<OPSlot>>() {
			@Override
			public int compare(GeoResult<OPSlot> s1, GeoResult<OPSlot> s2) {
				if (s1.getDistance().equals(s2.getDistance())) {
					return 0;
				}
				else {
					return s1.getContent().getDate().compareTo(s2.getContent().getDate());
				}
			}
		});

		/* Return the best (i.e first) result */
		return results.get(0).getContent();
	}

}
