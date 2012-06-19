/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view.widgets;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import au.org.intersect.exsite9.domain.MetadataCategory;
import au.org.intersect.exsite9.domain.MetadataValue;

/**
 * A Button that holds a metadataCategory and metadataValue - it make it easy to identify these characteristics
 * of a button that was pressed. 
 */
public final class MetadataButtonWidget extends Button
{
    private final MetadataCategory metadataCategory;
    private final MetadataValue metadataValue;

    public MetadataButtonWidget(final Composite parent, final int style, final MetadataCategory metadataCategory, final MetadataValue metadataValue)
    {
        super(parent, style);
        this.metadataCategory = metadataCategory;
        this.metadataValue = metadataValue;
    }

    public MetadataCategory getMetadataCategory()
    {
        return this.metadataCategory;
    }

    public MetadataValue getMetadataValue()
    {
        return this.metadataValue;
    }

    @Override
    protected void checkSubclass()
    {
        // screw you SWT!
    }
}
