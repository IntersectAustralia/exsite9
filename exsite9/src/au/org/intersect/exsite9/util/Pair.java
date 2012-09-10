/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.util;

import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.base.Objects;

/**
 * A pair. May be used generically to associate a pair of items together.
 */
public class Pair<F, S>
{
    private final F first;
    private final S second;

    public Pair(final F first, final S second)
    {
        this.first = first;
        this.second = second;
    }

    public F getFirst()
    {
        return this.first;
    }

    public S getSecond()
    {
        return this.second;
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof Pair<?, ?>))
        {
            return false;
        }
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        return Objects.equal(this.first, other.first) && Objects.equal(this.second, other.second);
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(this.first).append(this.second).toHashCode();
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("first", this.first).append("second", this.second).toString();
    }
}
