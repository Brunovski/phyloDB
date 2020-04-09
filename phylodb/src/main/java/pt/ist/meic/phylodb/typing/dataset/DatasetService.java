package pt.ist.meic.phylodb.typing.dataset;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pt.ist.meic.phylodb.typing.dataset.model.Dataset;
import pt.ist.meic.phylodb.typing.profile.ProfileRepository;
import pt.ist.meic.phylodb.typing.schema.SchemaRepository;
import pt.ist.meic.phylodb.typing.schema.model.Schema;
import pt.ist.meic.phylodb.utils.db.EntityRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DatasetService {

	private DatasetRepository datasetRepository;
	private SchemaRepository schemaRepository;
	private ProfileRepository profileRepository;

	public DatasetService(DatasetRepository datasetRepository, SchemaRepository schemaRepository, ProfileRepository profileRepository) {
		this.datasetRepository = datasetRepository;
		this.schemaRepository = schemaRepository;
		this.profileRepository = profileRepository;
	}

	@Transactional(readOnly = true)
	public Optional<List<Dataset>> getDatasets(int page, int limit) {
		return datasetRepository.findAll(page, limit);
	}

	@Transactional(readOnly = true)
	public Optional<Dataset> getDataset(UUID id, int version) {
		return datasetRepository.find(id, version);
	}

	@Transactional
	public boolean saveDataset(Dataset dataset) {
		Schema.PrimaryKey schemaKey = dataset.getSchema().getPrimaryKey();
		if (!schemaRepository.exists(schemaKey))
			return false;
		Optional<Dataset> dbDataset = datasetRepository.find(dataset.getPrimaryKey(), EntityRepository.CURRENT_VERSION_VALUE);
		if (!dbDataset.isPresent()) {
			datasetRepository.save(dataset);
			return true;
		} else if (!schemaKey.equals(dbDataset.get().getSchema().getPrimaryKey()) &&
				profileRepository.findAll(0, 1, dataset.getPrimaryKey()).get().size() > 0)
			return false;
		return datasetRepository.save(dataset);
	}

	@Transactional
	public boolean deleteDataset(UUID id) {
		return datasetRepository.remove(id);
	}

}
