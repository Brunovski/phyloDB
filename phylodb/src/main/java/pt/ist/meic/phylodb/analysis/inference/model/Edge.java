package pt.ist.meic.phylodb.analysis.inference.model;

import pt.ist.meic.phylodb.typing.profile.model.Profile;
import pt.ist.meic.phylodb.utils.service.VersionedEntity;

import java.util.Objects;

/**
 * An edge is used to represent distances between 2 profiles, and exist within an {@link Inference inference}
 * <p>
 * An edge is constituted by the {@link #from} and {@link #to} profiles references, and the {@link #weight} between them.
 */
public class Edge {

	private final VersionedEntity<Profile.PrimaryKey> from;
	private final VersionedEntity<Profile.PrimaryKey> to;
	private final long weight;

	public Edge(VersionedEntity<Profile.PrimaryKey> from, VersionedEntity<Profile.PrimaryKey> to, long weight) {
		this.from = from;
		this.to = to;
		this.weight = weight;
	}

	public VersionedEntity<Profile.PrimaryKey> getFrom() {
		return from;
	}

	public VersionedEntity<Profile.PrimaryKey> getTo() {
		return to;
	}

	public long getWeight() {
		return weight;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Edge edge = (Edge) o;
		return weight == edge.weight &&
				Objects.equals(from, edge.from) &&
				Objects.equals(to, edge.to);
	}

}

