package au.org.intersect.exsite9.util;

import com.google.common.base.Predicate;

import au.org.intersect.exsite9.domain.Group;
import au.org.intersect.exsite9.domain.IMetadataAssignable;
import au.org.intersect.exsite9.domain.NewFilesGroup;

/**
 * A Predicate that can be used to identify {@link NewFilesGroup}s in a Collection of {@link Group}s.
 */
public final class NewFilesGroupPredicate implements Predicate<IMetadataAssignable>
{
    public static final NewFilesGroupPredicate INSANCE = new NewFilesGroupPredicate();

    private NewFilesGroupPredicate()
    {
    }

    @Override
    public boolean apply(final IMetadataAssignable metadataAssignable)
    {
        return metadataAssignable instanceof NewFilesGroup;
    }
}
