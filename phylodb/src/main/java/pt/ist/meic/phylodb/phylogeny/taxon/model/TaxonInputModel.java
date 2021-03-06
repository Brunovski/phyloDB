package pt.ist.meic.phylodb.phylogeny.taxon.model;

import pt.ist.meic.phylodb.io.input.InputModel;

import java.util.Optional;

/**
 * An TaxonInputModel is the input model for a taxon
 * <p>
 * An TaxonInputModel is constituted by the {@link #id} field to identify the taxon
 * and the {@link #description} which is a description of this taxon.
 */
public class TaxonInputModel implements InputModel<Taxon> {

	private String id;
	private String description;

	public TaxonInputModel() {
	}

	public TaxonInputModel(String id, String description) {
		this.id = id;
		this.description = description;
	}

	public String getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public Optional<Taxon> toDomainEntity(String... params) {
		return !params[0].equals(id) ? Optional.empty() : Optional.of(new Taxon(id, description));
	}

}
