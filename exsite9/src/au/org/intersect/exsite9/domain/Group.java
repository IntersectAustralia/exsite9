package au.org.intersect.exsite9.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Represents a Group.
 *
 * @author <a href="mailto:daniel@intersect.org.au">Daniel Yazbek</a>
 */
public final class Group extends Node {

	/**
	 * Constructor
	 * @param name The name of the group.
	 */
	public Group(final String name) {
		super(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Group)) {
			return false;
		}
		return super.equals(obj);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		final ToStringBuilder tsb = new ToStringBuilder(this);
		tsb.appendSuper(super.toString());
		return tsb.toString();
	}
}
