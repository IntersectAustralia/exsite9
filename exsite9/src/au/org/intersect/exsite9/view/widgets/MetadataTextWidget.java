package au.org.intersect.exsite9.view.widgets;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import au.org.intersect.exsite9.domain.MetadataValue;

/**
 * A Text field that remembers the MetadataValue that it was associated with.
 */
public final class MetadataTextWidget extends Text
{
    private MetadataValue metadataValue;

    public MetadataTextWidget(final Composite parent, final int style)
    {
        super(parent, style);
    }

    public void setMetadataValue(final MetadataValue metadataValue)
    {
        this.metadataValue = metadataValue;
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
