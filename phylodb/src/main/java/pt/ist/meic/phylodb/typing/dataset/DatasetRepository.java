package pt.ist.meic.phylodb.typing.dataset;

import org.neo4j.ogm.model.Result;
import org.neo4j.ogm.session.Session;
import org.springframework.stereotype.Repository;
import pt.ist.meic.phylodb.typing.dataset.model.Dataset;
import pt.ist.meic.phylodb.typing.schema.model.Schema;
import pt.ist.meic.phylodb.utils.db.EntityRepository;
import pt.ist.meic.phylodb.utils.db.Query;
import pt.ist.meic.phylodb.utils.service.Reference;

import java.util.Map;
import java.util.UUID;

@Repository
public class DatasetRepository extends EntityRepository<Dataset, UUID> {

	public DatasetRepository(Session session) {
		super(session);
	}

	@Override
	protected Result getAll(int page, int limit, Object... filters) {
		String statement = "MATCH (d:Dataset)-[r1:CONTAINS_DETAILS]->(dd:DatasetDetails)-[h:HAS]->(s:Schema)-[r2:CONTAINS_DETAILS]->(sd:SchemaDetails)\n" +
				"WHERE d.deprecated = false AND NOT EXISTS(r1.to) AND r2.version = h.version\n" +
				"MATCH (sd)-[:HAS]->(l:Locus)<-[:CONTAINS]-(t:Taxon)\n" +
				"WITH d, dd, s, t, collect(l) as loci\n" +
				"RETURN d.id as datasetId, d.deprecated as deprecated, r1.version as version, " +
				"dd.description as description, t.id as taxonId, s.id as schemaId, h.version as schemaVersion, s.deprecated as schemaDeprecated\n" +
				"SKIP $ LIMIT $";
		return query(new Query(statement, page, limit));
	}

	@Override
	protected Result get(UUID id, int version) {
		String where = version == CURRENT_VERSION_VALUE ? "NOT EXISTS(r.to)" : "r.version = $";
		String statement = "MATCH (d:Dataset {id: $})-[r:CONTAINS_DETAILS]->(dd:DatasetDetails)-[h:HAS]->(s:Schema)-[r2:CONTAINS_DETAILS]->(sd:SchemaDetails)\n" +
				"WHERE r2.version = h.version AND " + where + "\n" +
				"MATCH (sd)-[:HAS]->(l:Locus)<-[:CONTAINS]-(t:Taxon)\n" +
				"WITH d, dd, s, t, collect(l) as loci\n" +
				"RETURN d.id as datasetId, d.deprecated as deprecated, r1.version as version, " +
				"dd.description as description, t.id as taxonId, s.id as schemaId, h.version as schemaVersion, s.deprecated as schemaDeprecated";
		return query(new Query(statement, id));
	}

	@Override
	protected Dataset parse(Map<String, Object> row) {
		Reference<Schema.PrimaryKey> schema = new Reference<>(new Schema.PrimaryKey((String) row.get("taxonId"),
				(String) row.get("schemaId")),
				(int) row.get("schemaVersion"),
				(boolean) row.get("schemaDeprecated"));
		return new Dataset(UUID.fromString(row.get("datasetId").toString()),
				(int) row.get("version"),
				(boolean)row.get("deprecated"),
				(String) row.get("description"),
				schema);
	}

	@Override
	protected boolean isPresent(UUID key) {
		String statement = "MATCH (d:Dataset {id: $})\n" +
				"RETURN d.deprecated = false";
		return query(Boolean.class, new Query(statement, key));
	}

	@Override
	protected void store(Dataset dataset) {
		String statement = "MERGE (d:Dataset {id : $}) SET a.deprecated = false, WITH d\n" +
				"OPTIONAL MATCH (d)-[r:CONTAINS_DETAILS]->(dd:DatasetDetails)" +
				"WHERE NOT EXISTS(r.to) SET r.to = datetime()\n" +
				"WITH d, COALESCE(MAX(r.version), 0) + 1 as v\n" +
				"CREATE (d)-[:CONTAINS_DETAILS {from: datetime(), version: v}]->(dd:DatasetDetails {description: $}) WITH dd\n" +
				"MATCH (s:Schema {id: $})-[r:CONTAINS_DETAILS]->(sd:SchemaDetails)-[:HAS]->(l:Locus)<-[:CONTAINS]-(t:Taxon {id: $})\n" +
				"WHERE NOT EXISTS(r.to)\n" +
				"WITH dd, s, r\n" +
				"CREATE (dd)-[:HAS {version: r.version}]->(s)";
		Schema.PrimaryKey schemaKey = dataset.getSchema().getId();
		Query query = new Query(statement, dataset.getId(), dataset.getDescription(), schemaKey.getId(), schemaKey.getTaxonId());
		execute(query);
	}

	@Override
	protected void delete(UUID id) {
		String statement = "MATCH (d:Dataset {id: $}) SET d.deprecated = true WITH d\n" +
				"MATCH (d)-[:CONTAINS]->(p:Profile) SET p.deprecated = true WITH d\n" +
				"MATCH (d)-[:CONTAINS]->(i:Isolate) SET i.deprecated = true";
		execute(new Query(statement, id));
	}
}