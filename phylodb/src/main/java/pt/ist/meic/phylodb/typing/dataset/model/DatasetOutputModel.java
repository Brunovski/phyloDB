package pt.ist.meic.phylodb.typing.dataset.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pt.ist.meic.phylodb.io.output.OutputModel;
import pt.ist.meic.phylodb.utils.service.VersionedEntity;

import java.util.Objects;

/**
 * A DatasetOutputModel is an output model for a dataset
 * <p>
 * A DatasetOutputModel contains the {@link #project_id}, and {@link #id} fields which identify the dataset, and the {@link #version}, and {@link #deprecated}
 * fields which are the version of the dataset, and the existence status respectively.
 */
public class DatasetOutputModel implements OutputModel {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	protected String project_id;
	protected String id;
	protected long version;
	protected boolean deprecated;

	public DatasetOutputModel() {
	}

	public DatasetOutputModel(Dataset dataset) {
		this.project_id = dataset.getPrimaryKey().getProjectId();
		this.id = dataset.getPrimaryKey().getId();
		this.version = dataset.getVersion();
		this.deprecated = dataset.isDeprecated();
	}

	public DatasetOutputModel(VersionedEntity<Dataset.PrimaryKey> dataset) {
		this.project_id = dataset.getPrimaryKey().getProjectId();
		this.id = dataset.getPrimaryKey().getId();
		this.version = dataset.getVersion();
		this.deprecated = dataset.isDeprecated();
	}

	public String getProject_id() {
		return project_id;
	}

	public String getId() {
		return id;
	}

	public long getVersion() {
		return version;
	}

	public boolean isDeprecated() {
		return deprecated;
	}

	@Override
	public ResponseEntity<DatasetOutputModel> toResponseEntity() {
		return ResponseEntity.status(HttpStatus.OK).body(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DatasetOutputModel that = (DatasetOutputModel) o;
		return version == that.version &&
				deprecated == that.deprecated &&
				Objects.equals(project_id, that.project_id) &&
				Objects.equals(id, that.id);
	}

	/**
	 * A DatasetOutputModel.Resumed is the resumed information of a dataset output model
	 * <p>
	 * A DatasetOutputModel.Resumed is constituted by the {@link #id} field which is the id of the dataset,
	 * and by the {@link #version} field which is the version of the dataset.
	 */
	@JsonIgnoreProperties({"project_id", "deprecated"})
	public static class Resumed extends DatasetOutputModel {

		public Resumed() {
		}

		public Resumed(VersionedEntity<Dataset.PrimaryKey> dataset) {
			super(dataset);
		}

	}

}
