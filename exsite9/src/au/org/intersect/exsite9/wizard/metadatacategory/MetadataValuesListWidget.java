/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.wizard.metadatacategory;

import java.util.List;

import org.eclipse.swt.widgets.Composite;

import au.org.intersect.exsite9.domain.MetadataValue;

/**
 * 
 */
public final class MetadataValuesListWidget extends org.eclipse.swt.widgets.List
{
    private final List<MetadataValue> metadataValues;

    public MetadataValuesListWidget(final Composite parent, final int style, final List<MetadataValue> metadataValues)
    {
        super(parent, style);
        this.metadataValues = metadataValues;
        for (final MetadataValue metadataValue : metadataValues)
        {
            super.add(metadataValue.getValue());
        }
    }

    @Override
    public void add(final String string)
    {
        super.add(string);
        this.metadataValues.add(new MetadataValue(string));
    }

    @Override
    public void remove(int index)
    {
        super.remove(index);
        this.metadataValues.remove(index);
    }
    
    
    @Override
    public void setItem(int index, String string)
    {
        super.setItem(index, string);
        this.metadataValues.get(index).setValue(string);
    }

    @Override
    protected void checkSubclass()
    {
        
    }
}
