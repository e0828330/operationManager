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
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
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
															Criteria.where("date").gte(from).lte(to));
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
				int distanceDiff = Double.compare(s1.getDistance().getValue(), s2.getDistance().getValue());
				if (distanceDiff == 0) {
					return s1.getContent().getFrom().compareTo(s2.getContent().getFrom());
				}
				else {
					return distanceDiff;
				}

			}
		});
		
		/* Return the best (i.e first) result */
		return results.get(0).getContent();
	}

}
