/**
 * Copyright (C) Intersect 2012.
 *
 * This module contains Proprietary Information of Intersect,
 * and should be treated as Confidential.
 */
package au.org.intersect.exsite9.view.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import au.org.intersect.exsite9.domain.MetadataAttributeValue;

/**
 * A Widget to select Metadata Attribute Values
 */
public final class MetadataAttributeValuesComboWidget extends Combo
{

    private final List<MetadataAttributeValue> values = new ArrayList<MetadataAttributeValue>();
    private MetadataAttributeValue selected;

    /**
     * @param parent The parent.
     * @param style The style.
     */
    public MetadataAttributeValuesComboWidget(final Composite parent, final int style)
    {
        super(parent, style);
        
    }

    public void setItems(final List<MetadataAttributeValue> values)
    {
        this.values.clear();
        this.values.addAll(values);
        final List<String> valuesString = new ArrayList<String>(values.size() + 1);
        valuesString.add("");
        for (final MetadataAttributeValue value : values)
        {
            valuesString.add(value.getValue());
        }
        setItems(valuesString.toArray(new String[]{}));
    }

    public MetadataAttributeValue getSelectedMetadataAttributeValue()
    {
        final int selectedIndex = getSelectionIndex();
        if (selectedIndex <= 0)
        {
            return null;
        }
        return values.get(selectedIndex - 1);
    }

    public void select(final MetadataAttributeValue value)
    {
        final int index = this.values.indexOf(value);
        if (index == -1)
        {
            return;
        }
        select(index + 1);
    }

    public MetadataAttributeValue getMetadataAttributeValue()
    {
        return selected;
    }

    public void setMetadataAttributeValue(final MetadataAttributeValue value)
    {
        this.selected = value;
    }

    @Override
    protected void checkSubclass()
    {
        // screw you SWT!
    }
}
