package pt.ist.meic.phylodb.phylogeny.taxon.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pt.ist.meic.phylodb.io.output.OutputModel;
import pt.ist.meic.phylodb.utils.service.VersionedEntity;

import java.util.Objects;

/**
 * A TaxonOutputModel is an output model for a taxon
 * <p>
 * A TaxonOutputModel contains the {@link #id} field which identify the taxon,
 * and also contains the {@link #version}, and {@link #deprecated} fields which are the version of the taxon, and the existence status respectively.
 */
public class TaxonOutputModel implements OutputModel {

	protected String id;
	protected long version;
	protected boolean deprecated;

	public TaxonOutputModel() {
	}

	public TaxonOutputModel(Taxon taxon) {
		this.id = taxon.getPrimaryKey();
		this.version = taxon.getVersion();
		this.deprecated = taxon.isDeprecated();
	}

	public TaxonOutputModel(VersionedEntity<String> taxon) {
		this.id = taxon.getPrimaryKey();
		this.version = taxon.getVersion();
		this.deprecated = taxon.isDeprecated();
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
	public ResponseEntity<TaxonOutputModel> toResponseEntity() {
		return ResponseEntity.status(HttpStatus.OK).body(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TaxonOutputModel that = (TaxonOutputModel) o;
		return version == that.version &&
				deprecated == that.deprecated &&
				Objects.equals(id, that.id);
	}

	/**
	 * A TaxonOutputModel.Resumed is the resumed information of a taxon output model
	 * <p>
	 * A TaxonOutputModel.Resumed is constituted by the {@link #id} field which is the id of the taxon,
	 * and by the {@link #version} field which is the version of the taxon.
	 */
	@JsonIgnoreProperties({"deprecated"})
	public static class Resumed extends TaxonOutputModel {

		public Resumed() {
		}

		public Resumed(VersionedEntity<String> taxon) {
			super(taxon);
		}

	}

}
