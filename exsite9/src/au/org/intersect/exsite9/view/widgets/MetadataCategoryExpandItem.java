/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view.widgets;

import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;

import au.org.intersect.exsite9.domain.MetadataCategory;

/**
 * An ExpandItem that knows about the Metadata Category it is for.
 */
public final class MetadataCategoryExpandItem extends ExpandItem
{
    private final MetadataCategory metadataCategory;

    public MetadataCategoryExpandItem(final ExpandBar parent, final int style, final MetadataCategory metadataCategory)
    {
        super(parent, style);
        this.metadataCategory = metadataCategory;
    }

    public MetadataCategory getMetadataCategory()
    {
        return this.metadataCategory;
    }

    @Override
    protected void checkSubclass()
    {
        // screw you SWT!
    }
}
