package pt.ist.meic.phylodb.phylogeny.taxon.model;

import pt.ist.meic.phylodb.utils.service.Entity;

public class Taxon extends Entity<String> {

	private final String description;

	public Taxon(String id, int version, boolean deprecated, String description) {
		super(id, version, deprecated);
		this.description = description;
	}

	public Taxon(String id, String description) {
		this(id, -1, false, description);
	}

	public String getDescription() {
		return description;
	}

}
