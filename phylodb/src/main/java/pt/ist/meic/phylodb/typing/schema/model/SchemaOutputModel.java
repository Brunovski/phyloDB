package pt.ist.meic.phylodb.typing.schema.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pt.ist.meic.phylodb.io.output.OutputModel;
import pt.ist.meic.phylodb.utils.service.VersionedEntity;

import java.util.Objects;

/**
 * A SchemaOutputModel is an output model for a schema
 * <p>
 * A SchemaOutputModel contains the {@link #taxon_id}, and {@link #id} fields which identify the schema,
 * and also contains the {@link #version}, and {@link #deprecated} fields which are the version of the schema, and the existence status respectively.
 */
public class SchemaOutputModel implements OutputModel {

	@JsonInclude(JsonInclude.Include.NON_NULL)
	protected String taxon_id;
	protected String id;
	protected long version;
	protected boolean deprecated;

	public SchemaOutputModel() {
	}

	public SchemaOutputModel(Schema schema) {
		this.taxon_id = schema.getPrimaryKey().getTaxonId();
		this.id = schema.getPrimaryKey().getId();
		this.version = schema.getVersion();
		this.deprecated = schema.isDeprecated();
	}

	public SchemaOutputModel(VersionedEntity<Schema.PrimaryKey> reference) {
		this.taxon_id = reference.getPrimaryKey().getTaxonId();
		this.id = reference.getPrimaryKey().getId();
		this.version = reference.getVersion();
		this.deprecated = reference.isDeprecated();
	}

	public String getTaxon_id() {
		return taxon_id;
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
	public ResponseEntity<SchemaOutputModel> toResponseEntity() {
		return ResponseEntity.status(HttpStatus.OK).body(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SchemaOutputModel that = (SchemaOutputModel) o;
		return version == that.version &&
				deprecated == that.deprecated &&
				Objects.equals(taxon_id, that.taxon_id) &&
				Objects.equals(id, that.id);
	}

	/**
	 * A SchemaOutputModel.Resumed is the resumed information of a schema output model
	 * <p>
	 * A SchemaOutputModel.Resumed is constituted by the {@link #id} field which is the id of the schema,
	 * and by the {@link #version} field which is the version of the schema.
	 */
	@JsonIgnoreProperties({"taxon_id", "deprecated"})
	public static class Resumed extends SchemaOutputModel {

		public Resumed() {
		}

		public Resumed(VersionedEntity<Schema.PrimaryKey> schema) {
			super(schema);
		}

	}

}
