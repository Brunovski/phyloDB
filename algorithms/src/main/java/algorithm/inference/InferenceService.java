package algorithm.inference;

import algorithm.utils.Service;
import algorithm.inference.implementation.GoeBURST;
import algorithm.inference.model.Inference;
import algorithm.inference.model.Matrix;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.logging.Log;

/**
 * Class that contains operations to execute inference algorithms
 * <p>
 * The service responsibility is to obtain the data from the db, execute the algorithm, and store the result in the db
 */
public class InferenceService extends Service {

	public InferenceService(GraphDatabaseService database, Log log) {
		super(database, log);
	}

	/**
	 * Executes the goeBURST algorithm, with the data resulting from the inference identified in the parameters, and stores the result
	 *
	 * @param project  project id
	 * @param dataset  dataset id
	 * @param analysis inference id
	 * @param lvs      number of lvs
	 */
	public void goeBURST(String project, String dataset, String analysis, long lvs) {
		InferenceRepository repository = new InferenceRepository(database);
		GoeBURST algorithm = new GoeBURST();
		algorithm.init(project, dataset, analysis, lvs);
		Matrix matrix;
		try (Transaction tx1 = database.beginTx()) {
			matrix = repository.read(tx1, project, dataset);
			tx1.commit();
		}
		Inference inference = algorithm.compute(matrix);
		try (Transaction tx2 = database.beginTx()) {
			repository.write(tx2, inference);
			tx2.commit();
		}
	}

}
