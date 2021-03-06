package pt.ist.meic.phylodb.typing.profile.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pt.ist.meic.phylodb.io.output.OutputModel;
import pt.ist.meic.phylodb.utils.service.VersionedEntity;

import java.util.Objects;

/**
 * A ProfileOutputModel is an output model for a profile
 * <p>
 * A ProfileOutputModel contains the {@link #project_id}, {@link #dataset_id}, {@link #id} fields which identify the profile,
 * and also contains the {@link #version}, and {@link #deprecated} fields which are the version of the profile, and the existence status respectively.
 */
public class ProfileOutputModel implements OutputModel {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	protected String project_id;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	protected String dataset_id;
	protected String id;
	protected long version;
	protected boolean deprecated;

	public ProfileOutputModel() {
	}

	public ProfileOutputModel(Profile profile) {
		this.project_id = profile.getPrimaryKey().getProjectId();
		this.dataset_id = profile.getPrimaryKey().getDatasetId();
		this.id = profile.getPrimaryKey().getId();
		this.version = profile.getVersion();
		this.deprecated = profile.isDeprecated();
	}

	public ProfileOutputModel(VersionedEntity<Profile.PrimaryKey> profile) {
		this.project_id = profile.getPrimaryKey().getProjectId();
		this.dataset_id = profile.getPrimaryKey().getDatasetId();
		this.id = profile.getPrimaryKey().getId();
		this.version = profile.getVersion();
		this.deprecated = profile.isDeprecated();
	}

	public String getProject_id() {
		return project_id;
	}

	public String getDataset_id() {
		return dataset_id;
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
	public ResponseEntity<ProfileOutputModel> toResponseEntity() {
		return ResponseEntity.status(HttpStatus.OK).body(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ProfileOutputModel that = (ProfileOutputModel) o;
		return version == that.version &&
				deprecated == that.deprecated &&
				Objects.equals(project_id, that.project_id) &&
				Objects.equals(dataset_id, that.dataset_id) &&
				Objects.equals(id, that.id);
	}

	/**
	 * A ProfileOutputModel.Resumed is the resumed information of a profile output model
	 * <p>
	 * A ProfileOutputModel.Resumed is constituted by the {@link #id} field which is the id of the profile,
	 * and by the {@link #version} field which is the version of the profile.
	 */
	@JsonIgnoreProperties({"project_id", "dataset_id", "deprecated"})
	public static class Resumed extends ProfileOutputModel {

		public Resumed() {
		}

		public Resumed(Profile profile) {
			super(profile);
		}

		public Resumed(VersionedEntity<Profile.PrimaryKey> profile) {
			super(profile);
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			ProfileOutputModel that = (ProfileOutputModel) o;
			return version == that.version &&
					Objects.equals(id, that.id);
		}

	}

}
